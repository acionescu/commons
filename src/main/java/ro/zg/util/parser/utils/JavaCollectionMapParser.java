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
package ro.zg.util.parser.utils;

import ro.zg.util.parser.Parser;
import ro.zg.util.parser.ParserException;
import ro.zg.util.parser.ParserHandlerFactory;
import ro.zg.util.parser.Symbol;
import ro.zg.util.parser.SymbolFlag;
import ro.zg.util.parser.SymbolType;
import ro.zg.util.parser.event.ParseEventHandler;

public class JavaCollectionMapParser {
    private Parser parser;
    public static final String MAP_START="{";
    public static final String MAP_END="}";
    public static final String LIST_START="[";
    public static final String LIST_END="]";
    public static final String SEPARATE=",";
    public static final String ASSOCIATE="=";
    public static final String ESCAPE="\"";
    
    private CollectionMapParseEventHandler parseEventHandler;
    
    public JavaCollectionMapParser() {
	super();
	parser = new Parser();
	parseEventHandler = new CollectionMapParseEventHandler();
	parser.setHandlerFactory(new ParserHandlerFactory(parseEventHandler));
	init();
    }
    
    
    public JavaCollectionMapParser(MultivaluedTypeDefinition<?,?> collectionTypeDef, MapTypeDefinition<?,?,?> mapTypeDef) {
	parser = new Parser();
	parseEventHandler = new CollectionMapParseEventHandler(collectionTypeDef,mapTypeDef);
	parser.setHandlerFactory(new ParserHandlerFactory(parseEventHandler));
	init();
    }
    
    private void init() {
	Symbol startDoc = new Symbol(null,SymbolType.DOC_START);
	startDoc.addFlag(SymbolFlag.IGNORE_EMPTY);
	
	Symbol mapSymbol = new Symbol(MAP_START, SymbolType.GROUP_START);
	mapSymbol.addNestedSymbol(new Symbol(MAP_END, SymbolType.GROUP_END));
	mapSymbol.addNestedSymbol(new Symbol(ASSOCIATE, SymbolType.ASSOCIATE));
	mapSymbol.addNestedSymbol(new Symbol(SEPARATE, SymbolType.SEPARATE));
	mapSymbol.addNestedSymbol(mapSymbol);
	mapSymbol.addFlag(SymbolFlag.IGNORE_EMPTY);

	Symbol listSymbol = new Symbol(LIST_START, SymbolType.GROUP_START);
	listSymbol.addNestedSymbol(new Symbol(LIST_END, SymbolType.GROUP_END));
	listSymbol.addNestedSymbol(new Symbol(SEPARATE, SymbolType.SEPARATE));
	listSymbol.addNestedSymbol(listSymbol);
	listSymbol.addFlag(SymbolFlag.IGNORE_EMPTY);

	Symbol quotesSimbol = new Symbol(ESCAPE, SymbolType.GROUP_START);
	quotesSimbol.addNestedSymbol(new Symbol(ESCAPE, SymbolType.GROUP_END));
	
	/* no need to escape the escaped */
	quotesSimbol.setOverrideSuperContextConfig(true);
	quotesSimbol.setUseEscapeCharacterOn(false);
	
	mapSymbol.addNestedSymbol(quotesSimbol);
	listSymbol.addNestedSymbol(quotesSimbol);

	listSymbol.addNestedSymbol(mapSymbol);
	mapSymbol.addNestedSymbol(listSymbol);

	
	parser.setUseEscapeCharacterOn(true);
	parser.addSymbol(startDoc);
	parser.addSymbol(mapSymbol);
	parser.addSymbol(listSymbol);
    }
    

    public Object parse(String s) throws ParserException {
	return parser.parse(s).getObjects().pop();
    }
    
    public Object parse(String s, ParseEventHandler peh) throws ParserException {
	return parser.parse(s,peh).getObjects().pop();
    }
    
    public static void main(String[] args) throws ParserException {
//	String s = "[{name=sortList,value=\"popularity_index desc, total_votes desc,id\",lk=\"[1,   2,5]\"}]";
//	Object o = new JavaCollectionMapParser(ArrayList.class,LinkedHashMap.class).parse(s);
//	System.out.println(o);
//	System.out.println(o.getClass());
//	List<Object> list = (List) o;
//	for (Object e : list) {
//	    System.out.println(e.getClass());
//	}
//	String s ="[\"^[username\",\"^[\\w]{5,20}$\", \"email\"]";
//	Object o = new JavaCollectionMapParser().parse(s);
//	System.out.println(o);
//	System.out.println(new JavaCollectionMapParser().parse("[]"));
	System.out.println(new JavaCollectionMapParser().parse("{lt=<,gt=>,amp=&,quot=\\\",apos=\\'}"));
    }

}
