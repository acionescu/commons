/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.util.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.util.calculator.event.MathExpressionParserEventHandler;
import ro.zg.util.data.ListMap;
import ro.zg.util.parser.event.DefaultParseEventHandler;
import ro.zg.util.parser.event.ParseEventHandler;

public class Parser {
    // /**
    // * the root symbol set
    // */
    // private SymbolSet symbols = new SymbolSet();
    // private boolean useEscapeCharacterOn;

    private ParseContextConfig parseContextConfig = new ParseContextConfig();

    public static final String DEFAULT_ESCAPE_CHARACTER = "\\";
    private List<String> listOfStringSymbols = new ArrayList<String>();
    private ParserHandlerFactory handlerFactory = new ParserHandlerFactory(new DefaultParseEventHandler());

    public ParseResponse parse(String input, ParseEventHandler parseEventHandler) throws ParserException {
	ParseHandler handler = new ParseHandler(parseEventHandler);
	return parse(input, handler);
    }

    public ParseResponse parse(String input) throws ParserException {
	ParseHandler handler = handlerFactory.createParseHandler();
	return parse(input, handler);
    }

    private ParseResponse parse(String input, ParseHandler handler) throws ParserException {
	SymbolSet symbols = parseContextConfig.getNestedSymbols();

	if (input == null || "".equals(input) || symbols == null || symbols.size() == 0) {
	    return new ParseResponse(new ArrayDeque<Object>());
	}

	if (parseContextConfig.isDocStartEventOn()) {
	    handler.onStartOfFile(parseContextConfig);
	}

	StringTraverser source = new StringTraverser(input);

	try {
	    // int startIndex = 0;
	    // int currentIndex = 1;
	    // int previousMatchIndex = 0;
	    // while (currentIndex <= input.length()) {
	    // String substring = null;
	    // substring = input.substring(startIndex, currentIndex);
	    Match prevMatch = null;

	    while (!source.exhausted()) {
		ParseContextConfig currentParseContext = handler.getActiveContext(parseContextConfig);
		if (currentParseContext.isUseEscapeCharacterOn()) {
		    String next = source.peek(1);
		    if (next.equals(currentParseContext.getEscapeCharacter())) {
			/* skip the escape character */
			source.addSkipPos(source.getOffset());
			/* move position over the escape char and the escaped char */
			source.move(2);

			// source.moveCheckPoint(1);
		    }
		}
		/*
		 * List<Symbol> foundSymbols = getMatch(substring, handler); if (foundSymbols != null &&
		 * foundSymbols.size() > 0) { String content = ""; if (startIndex > previousMatchIndex) { content =
		 * input.substring(previousMatchIndex, startIndex); } for (Symbol foundSymbol : foundSymbols) {
		 * handleSymbol(handler, foundSymbol, content, startIndex);
		 * 
		 * } previousMatchIndex = currentIndex; // startIndex = currentIndex; currentIndex++; continue; }
		 * boolean potentialMatch = checkPotentialMatch(substring, handler); if (!potentialMatch) {
		 * startIndex++; currentIndex = startIndex + 1; } potentialMatch |= (startIndex == currentIndex); if
		 * (potentialMatch) { currentIndex++; }
		 */
		prevMatch = searchAndHandle(handler, source, prevMatch);
	    }
	    // handler.onEndOfFile(input.substring(previousMatchIndex));

	    handler.onEndOfFile(source.readFromCheckPoint(input.length() - source.getCheckPoint()));
	} catch (Exception pe) {
	    throw new ParserException("Failed to parse '" + input + "'", pe);
	}
	return new ParseResponse(handler.getObjectsStack());
    }

    private Match searchAndHandle(ParseHandler handler, StringTraverser source, Match prevMatch)
	    throws ContextAwareException {
	SymbolSet activeSymbolSet = handler.getActiveSymbolSet();
	if (activeSymbolSet == null) {
	    activeSymbolSet = parseContextConfig.getNestedSymbols();
	}
	/* get a fragment from the source with a length equal with the max sequence lenght form the active symbol set */
	int fragmentLenght = activeSymbolSet.getMaxSequeceLength();
	fragmentLenght = Math.min(fragmentLenght, source.remained());
	String fragment = source.peek(fragmentLenght);
	List<Symbol> foundSymbols = activeSymbolSet.getSymbolsForSequence(fragment);
	boolean groupEndFound = false;
	Match match = prevMatch;

	if (foundSymbols != null) {
	    String seq = null;
	    for (Symbol s : foundSymbols) {
		seq = s.getSequence();
		String content = "";
		if (source.getOffset() > source.getCheckPoint()) {
		    content = source.readFromCheckPoint(source.getOffset() - source.getCheckPoint());
		}
		if (!s.containsFlag(SymbolFlag.REPEATABLE)) {
		    /*
		     * if the previous found symbol is repeatable handle this one first, because it wasn't handled
		     * before
		     */
		    if (prevMatch != null && prevMatch.getSymbol().containsFlag(SymbolFlag.REPEATABLE)) {
			handleSymbol(handler, prevMatch.getSymbol(), prevMatch.getContent(), prevMatch.getPosition());
		    }
		    match = new Match(s, content, seq, source.getOffset());
		    handleSymbol(handler, s, content, source.getOffset());
		    // if (SymbolFlags.MULTIPLE.equals(s.getAction())) {
		    if (s.containsFlag(SymbolFlag.MULTIPLE)) {
			groupEndFound = true;
		    }
		}
		/* if this symbol is repeatable */
		else {
		    /* if the previous one is also repeatable */
		    if (prevMatch != null && prevMatch.getSymbol().containsFlag(SymbolFlag.REPEATABLE)) {
			/*
			 * this match follows immediately after the previous match, no characters in between. They form
			 * one continuous string.
			 */
			if (source.getOffset() == prevMatch.getEndPosition()) {
			    match = new Match(prevMatch, seq);
			} else {
			    /* handler the previous symbol */
			    handleSymbol(handler, prevMatch.getSymbol(), prevMatch.getContent(),
				    prevMatch.getPosition());
			    match = new Match(s, content, seq, source.getOffset());
			}
		    } else {
			match = new Match(s, content, seq, source.getOffset());
		    }
		    break;
		}
	    }
	    source.setCheckPoint(source.getOffset() + seq.length());
	    if (!groupEndFound) {
		source.move(seq.length());
	    }
	} else {
	    source.move(1);
	}

	return match;
    }

    private void handleSymbol(ParseHandler handler, Symbol symbol, String content, int startIndex)
	    throws ContextAwareException {
	// handler.handleStartedGroups(symbol);
	handler.onNewSymbolFound(symbol, startIndex, content);
    }

    public void addSymbol(String sequence, SymbolType type) {
	// symbols.put(sequence, new Symbol(sequence, type));
	parseContextConfig.getNestedSymbols().add(sequence, new Symbol(sequence, type));
    }

    public void addSymbol(String sequence, SymbolType type, int priority) {
	// symbols.put(sequence, new Symbol(sequence, type, priority));
	parseContextConfig.getNestedSymbols().add(sequence, new Symbol(sequence, type, priority));
    }

    public void addSymbol(String symbolString) throws ParserException {
	Symbol s = Symbol.fromString(symbolString);
	// symbols.put(s.getSequence(), s);
	parseContextConfig.getNestedSymbols().add(s.getSequence(), s);
    }

    /**
     * use '\\'' to escape ' and ',' to escape , use '\\\' to escape \
     * 
     * @param string
     * @throws ParserException
     */
    public void addSymbols(String string) throws ParserException {
	List<Symbol> sl = Symbol.fromString2(string);
	for (Symbol s : sl) {
	    if (s.getType().equals(SymbolType.DOC_START)) {
		parseContextConfig.configFromSymbol(s);
		continue;
	    }
	    parseContextConfig.getNestedSymbols().add(s.getSequence(), s);
	}
    }

    public void addSymbol(Symbol s) {
	if (s.getType().equals(SymbolType.DOC_START)) {
	    parseContextConfig.configFromSymbol(s);
	} else {
	    parseContextConfig.getNestedSymbols().add(s.getSequence(), s);
	}
    }

    private List<Symbol> getMatch(String s, ParseHandler handler) {
	if (handler.hasStartedGroups()) {
	    return handler.getNestedSymbol(s);
	}
	return parseContextConfig.getNestedSymbols().get(s);
    }

    private boolean checkPotentialMatch(String s, ParseHandler handler) {
	if (handler.hasStartedGroups()) {
	    return handler.checkPotentialNestedSymbolMatch(s);
	} else {
	    for (String key : parseContextConfig.getNestedSymbols().keySet()) {
		if (key.startsWith(s)) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * @return the handlerFactory
     */
    public ParserHandlerFactory getHandlerFactory() {
	return handlerFactory;
    }

    /**
     * @param handlerFactory
     *            the handlerFactory to set
     */
    public void setHandlerFactory(ParserHandlerFactory handlerFactory) {
	this.handlerFactory = handlerFactory;
    }

    /**
     * @return the symbols
     */
    public ListMap<String, Symbol> getSymbols() {
	return parseContextConfig.getNestedSymbols();
    }

    /**
     * @return the listOfStringSymbols
     */
    public List<String> getListOfStringSymbols() {
	return listOfStringSymbols;
    }

    /**
     * @return the useEscapeCharacterOn
     */
    public boolean isUseEscapeCharacterOn() {
	return parseContextConfig.isUseEscapeCharacterOn();
    }

    /**
     * @param useEscapeCharacterOn
     *            the useEscapeCharacterOn to set
     */
    public void setUseEscapeCharacterOn(boolean useEscapeCharacterOn) {
	parseContextConfig.setUseEscapeCharacterOn(useEscapeCharacterOn);
    }

    /**
     * @param listOfStringSymbols
     *            the listOfStringSymbols to set
     * @throws ParserException
     */
    public void setListOfStringSymbols(List<String> listOfStringSymbols) throws ParserException {
	this.listOfStringSymbols = listOfStringSymbols;
	if (listOfStringSymbols == null) {
	    return;
	}
	for (String s : listOfStringSymbols) {
	    addSymbols(s);
	}
    }

    /**
     * @return the parseContextConfig
     */
    public ParseContextConfig getParseContextConfig() {
	return parseContextConfig;
    }

    /**
     * @param parseContextConfig
     *            the parseContextConfig to set
     */
    public void setParseContextConfig(ParseContextConfig parseContextConfig) {
	this.parseContextConfig = parseContextConfig;
    }

    public static void main(String[] args) throws Exception {
	Parser parser = new Parser();
	parser.setHandlerFactory(new ParserHandlerFactory(new MathExpressionParserEventHandler()));

	parser.addSymbol("+", SymbolType.ASSOCIATE, 5);
	parser.addSymbol("-", SymbolType.ASSOCIATE, 5);
	parser.addSymbol("*", SymbolType.ASSOCIATE, 3);
	parser.addSymbol("/", SymbolType.ASSOCIATE, 3);
	parser.addSymbol("sqrt", SymbolType.ASSOCIATE, 2);
	parser.addSymbol("^", SymbolType.ASSOCIATE, 2);
	parser.addSymbol("ln", SymbolType.ASSOCIATE, 2);
	parser.addSymbol("lg", SymbolType.ASSOCIATE, 2);
	parser.addSymbol("(", SymbolType.GROUP_START, 0);
	parser.addSymbol(")", SymbolType.GROUP_END, 0);

	// parser.parse("(((2+8)*(3-2))+10)/2");
	// parser.parse("ln(5)");

	Parser p = new Parser();
	p.addSymbols("['\\'',GROUP_START,apos_start,true,comma,apos_end,tab],['\\'',GROUP_END,apos_end],[ ,SEPARATE:REPEATABLE,comma,true],[\t,SEPARATE:REPEATABLE,tab,true]");
	// p.addSymbols("[:,SEPARATE,comma,true]");
	ParseResponse pr = p.parse("'a 		    	  b c',,,'e f     g'");
	System.out.println(pr.getObjectsList());

    }

    // class SymbolComparator implements Comparator<String> {
    //
    // public int compare(String s1, String s2) {
    // Symbol o1 = symbols.get(s1);
    // Symbol o2 = symbols.get(s2);
    //
    // int p1 = o1.getPriority();
    // int p2 = o2.getPriority();
    // if (p1 < p2) {
    // return -1;
    // } else if (p1 > p2) {
    // return 1;
    // }
    // /* if they are equal the highest priority will have the one with the longest length of sequence */
    //
    // int l1 = s1.length();
    // int l2 = s2.length();
    // if (l1 > l2)
    // return -1;
    // if (l1 < l2)
    // return 1;
    //
    // return s1.compareTo(s2);
    // }
    // }
}
