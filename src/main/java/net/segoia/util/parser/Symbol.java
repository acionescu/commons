/**
 * commons - Various Java Utils
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.util.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.segoia.util.parser.utils.JavaCollectionMapParser;
import net.segoia.util.parser.workers.AbstractParseWorkerFactory;
import net.segoia.util.parser.workers.ParseWorker;
import net.segoia.util.strings.StringUtil;

/**
 * Defines a simple entity to be searched in a string
 * 
 * @author adi
 * 
 */
public class Symbol extends ParseContextConfig {
    /**
     * Sequence of characters that define this symbol
     */
    private String sequence;
    /**
     * the name for this symbol
     */
    private String name;
    // /**
    // * Defines a special kind of action associeated with this symbol
    // */
    // private String action;

    private int priority = -1;
    /**
     * The type of this symbol
     */
    private SymbolType type;
    /**
     * The pair symbols list for a group delimiter symbol
     */
    private List<Symbol> pairSymbols = new ArrayList<Symbol>();

    private List<ParseWorker> workers = new ArrayList<ParseWorker>();

    private static Parser symbolDefinitionParser;

    private static JavaCollectionMapParser collectionsParser = new JavaCollectionMapParser();

    static {
	symbolDefinitionParser = new Parser();
	symbolDefinitionParser.setUseEscapeCharacterOn(true);
	symbolDefinitionParser.addSymbol(",", SymbolType.SEPARATE, 0);

	Symbol apos = new Symbol("\'", SymbolType.GROUP_START, 0);
	apos.addNestedSymbol(new Symbol("\'", SymbolType.GROUP_END));
	apos.addFlag(SymbolFlag.SIMPLE);

	Symbol startGroup = new Symbol("[", SymbolType.GROUP_START, 1);
	startGroup.addNestedSymbol(new Symbol("]", SymbolType.GROUP_END, 1));
	startGroup.addNestedSymbol(new Symbol(",", SymbolType.SEPARATE, 0));
	startGroup.addNestedSymbol(startGroup);
	startGroup.addNestedSymbol(apos);
	symbolDefinitionParser.addSymbol(startGroup);
    }

    public Symbol() {

    }

    public Symbol(String sequence, SymbolType type) {
	this.sequence = sequence;
	this.type = type;
    }

    public Symbol(String sequence, SymbolType type, int priority) {
	this.sequence = sequence;
	this.type = type;
	this.priority = priority;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
	return sequence;
    }

    // /**
    // * @return the action
    // */
    // public String getAction() {
    // return action;
    // }

    /**
     * @return the type
     */
    public SymbolType getType() {
	return type;
    }

    /**
     * @param sequence
     *            the sequence to set
     */
    public void setSequence(String sequence) {
	this.sequence = unescapeSequence(sequence);
    }

    // /**
    // * @param action
    // * the action to set
    // */
    // public void setAction(String action) {
    // this.action = action;
    // }

    /**
     * @param type
     *            the type to set
     */
    public void setType(SymbolType type) {
	this.type = type;
    }

    /**
     * @return the pairSymbols
     */
    public List<Symbol> getPairSymbols() {
	return pairSymbols;
    }

    /**
     * @param pairSymbols
     *            the pairSymbols to set
     */
    public void setPairSymbols(List<Symbol> pairSymbols) {
	this.pairSymbols = pairSymbols;
    }

    public void addPairSymbol(Symbol s) {
	pairSymbols.add(s);
    }

    public void addWorker(ParseWorker worker) {
	workers.add(worker);
    }

    public Object applyWorkers(Collection<Object> input) {
	Object result = input;
	for (ParseWorker w : workers) {
	    result = w.process((Collection<Object>) result);
	}

	return result;
    }

    public boolean hasWorkers() {
	return workers.size() > 0;
    }

    // /**
    // * @return the flags
    // */
    // public List<String> getFlags() {
    // return flags;
    // }
    //
    // /**
    // * @param flags
    // * the flags to set
    // */
    // public void setFlags(List<String> flags) {
    // this.flags = flags;
    // }

    private String unescapeSequence(String input) {
	if (input.startsWith(Parser.DEFAULT_ESCAPE_CHARACTER)) {
	    return input.substring(1);
	}
	return input;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
	return priority;
    }

    /**
     * @param priority
     *            the priority to set
     */
    public void setPriority(int priority) {
	this.priority = priority;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    public void addNestedSymbol(Symbol s) {
	String seq = s.getSequence();
	getNestedSymbols().add(seq, s);
	if (type.equals(SymbolType.GROUP_START)) {
	    SymbolType nsType = s.getType();
	    if (nsType.equals(SymbolType.GROUP_END) || nsType.equals(SymbolType.GROUP_END_START)) {
		addPairSymbol(s);
		s.addPairSymbol(this);
	    }
	}
    }

    public String toString() {
	// return sequence + "," + type + "," + name + "," + action + "," + nestedSymbols.keySet();
	return sequence + "," + type + "," + name + "," + getNestedSymbols().keySet();
    }

    public static Symbol fromString(String symbolString) throws ParserException {
	ParseResponse resp = symbolDefinitionParser.parse(symbolString);
	Deque<Object> objects = resp.getObjects();
	int index = 0;
	Symbol symbol = new Symbol();
	while (!objects.isEmpty()) {
	    Object o = objects.pollLast();
	    if (index == 0) {
		symbol.setSequence(o.toString());
	    } else if (index == 1) {
		symbol.setType(SymbolType.valueOf(o.toString().trim()));
	    } else if (index == 2) {
		symbol.setPriority(Integer.parseInt(o.toString().trim()));
	    } else if (index == 3) {
		symbol.setName(o.toString());
	    } else {
		String ss = o.toString();
		symbol.addNestedSymbol(Symbol.fromString(ss.substring(1, ss.length())));
	    }
	    index++;
	}

	return symbol;
    }

    public static List<Symbol> fromString2(String symbolString) throws ParserException {
	ParseResponse resp = symbolDefinitionParser.parse(symbolString);
	Deque<Object> objects = resp.getObjects();

	Map<String, Symbol> symbols = new HashMap<String, Symbol>();
	Map<String, List<String>> nestedSymbols = new HashMap<String, List<String>>();
	List<Symbol> mainSymbols = new ArrayList<Symbol>();
	Map<String, ParseWorker> workers = new HashMap<String, ParseWorker>();

	while (!objects.isEmpty()) {
	    Object symbolsList = objects.pollLast();
	    Symbol symbol = new Symbol();
	    ArrayList<String> nestedSymbolNames = new ArrayList<String>();
	    int index = 0;
	    
	    for (Object elem : (List) symbolsList) {
		String o = null;
		if (elem instanceof List) {
		    o = ((List) elem).get(0).toString();
		} else if (elem != null) {
		    o = elem.toString();
		}
		if (index == 0) {
		    if (o != null) {
			symbol.setSequence(StringUtil.unescapeJava(o.toString()));
		    }
		} else if (index == 1) {
		    String[] str = o.toString().split(":");
		    symbol.setType(SymbolType.valueOf(str[0].trim()));
		    // if (str.length > 1) {
		    // symbol.setAction(str[1]);
		    // }
		    for (int i = 1; i < str.length; i++) {

			symbol.addFlag(str[i]);
		    }

		} else if (index == 2) {
		    symbol.setName(o.toString());
		} else if (index == 3) {
		    if ("true".equals(o)) {
			mainSymbols.add(symbol);
		    }
		} else {
		    // String ss = o.toString();
		    // symbol.addNestedSymbol(Symbol.fromString(ss.substring(1,ss.length())));
		    nestedSymbolNames.add(o.toString());
		}
		index++;
	    }

	    if (symbol.getType().equals(SymbolType.WORKER)) {
		/*
		 * expects the type of the worker as the first flag, and the parameters as the sequence in the format
		 * '{name1=value1,name2=value2}'
		 */
		ParseWorker worker = AbstractParseWorkerFactory.createWorker(symbol.getFlags().get(0),
			(Map<Object, Object>) collectionsParser.parse(symbol.getSequence()));
		workers.put(symbol.getName(), worker);
	    }

	    symbols.put(symbol.getName(), symbol);
	    nestedSymbols.put(symbol.getName(), nestedSymbolNames);
	}

	for (Map.Entry<String, List<String>> entry : nestedSymbols.entrySet()) {
	    String symbolName = entry.getKey();
	    Symbol currentSymbol = symbols.get(symbolName);
	    int index = 0;
	    for (String nestedName : entry.getValue()) {
		Symbol symb = symbols.get(nestedName);
		if (symb == null) {
		    throw new ParserException("No symbol found for name " + nestedName);
		}
		if (symb.getType().equals(SymbolType.WORKER)) {
		    currentSymbol.addWorker(workers.get(nestedName));
		} else {
		    currentSymbol.addNestedSymbol(symb);
		}
		index++;
	    }
	}

	return mainSymbols;
    }

    public static void main(String[] args) throws ParserException {
	Symbol.fromString2("[&lt;a ,GROUP_START,start_a,true,end_a,end_a2,script_start,apos_start,quot_start,comma,equals],[&gt;,GROUP_END,end_a],[/&gt;,GROUP_END,end_a2],[script&gt;,GROUP_START,script_start,false,script_end],[&lt;/script,GROUP_END,script_end],['\\'',GROUP_START,apos_start,false,apos_end],['\\'',GROUP_END,apos_end],[\\\",GROUP_START,quot_start,false,quot_end],[\\\",GROUP_END,quot_end],[ ,SEPARATE,comma],[=,ASSOCIATE,equals]");
    }
}
