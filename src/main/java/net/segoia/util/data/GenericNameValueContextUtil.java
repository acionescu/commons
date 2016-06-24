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
package net.segoia.util.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.util.parser.Parser;
import net.segoia.util.parser.ParserHandlerFactory;
import net.segoia.util.parser.Symbol;
import net.segoia.util.parser.SymbolFlag;
import net.segoia.util.parser.SymbolType;
import net.segoia.util.parser.event.AssociationEvent;
import net.segoia.util.parser.event.GroupEvent;
import net.segoia.util.parser.event.ParseEventHandler;

public class GenericNameValueContextUtil {
    private static Parser parser;

    static {
	ContextParserHandler handler = new ContextParserHandler();
	parser = new Parser();
	parser.setHandlerFactory(new ParserHandlerFactory(handler));

	Symbol commaSymbol = new Symbol(",", SymbolType.SEPARATE);

	Symbol listSymbol = new Symbol("[", SymbolType.GROUP_START);
	listSymbol.addNestedSymbol(new Symbol("]", SymbolType.GROUP_END));
	listSymbol.addNestedSymbol(listSymbol);
	listSymbol.addNestedSymbol(commaSymbol);
	listSymbol.addFlag(SymbolFlag.IGNORE_EMPTY);

	Symbol mapSymbol = new Symbol("{", SymbolType.GROUP_START);
	mapSymbol.addNestedSymbol(new Symbol("}", SymbolType.GROUP_END));
	mapSymbol.addNestedSymbol(commaSymbol);
	mapSymbol.addNestedSymbol(new Symbol("=", SymbolType.ASSOCIATE));
	mapSymbol.addNestedSymbol(mapSymbol);
	mapSymbol.addFlag(SymbolFlag.IGNORE_EMPTY);

	mapSymbol.addNestedSymbol(listSymbol);
	listSymbol.addNestedSymbol(mapSymbol);

	parser.addSymbol(listSymbol);
	parser.addSymbol(mapSymbol);
	parser.setUseEscapeCharacterOn(true);
    }

    public static GenericNameValueContext parse(String input) {
	try {
	    Deque<Object> objects = parser.parse(input).getObjects();
	    if (objects.size() > 0) {
		return (GenericNameValueContext) objects.pop();
	    } else {
		// String in = input.trim();
		// GenericNameValueList c = new GenericNameValueList();
		// if (!"".equals(in)) {
		// c.addValue(in);
		// return c;
		// }
		// return c;
		return new GenericNameValueContext();
	    }
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * Basically this will convert lists and maps to {@link GenericNameValueContext} and will leave other types of
     * objects alone
     * 
     * @param target
     * @return
     * @throws ContextAwareException
     */
    public static Object convertToKnownType(Object target, boolean parseString) throws ContextAwareException {
	if (target instanceof List<?>) {
	    return listToContext((List<?>) target);
	} else if (target instanceof Map<?, ?>) {
	    return mapToContext((Map<?, ?>) target);
	} else if (target instanceof GenericNameValue) {
	    GenericNameValueContext mapContext = new GenericNameValueContext();
	    mapContext.put((GenericNameValue) target);
	    return mapContext;
	} else if (target instanceof Object[]) {
	    return listToContext(Arrays.asList((Object[]) target));
	} else if (target instanceof String) {
	    if (parseString) {
		GenericNameValueContext c = parse(target.toString());
		if (c.size() > 0) {
		    return c;
		}
	    }
	    else {
		return target;
	    }
	}
	return target;
    }

    public static Object convertToKnownType(Object target) throws ContextAwareException {
	return convertToKnownType(target,true);
    }

    public static GenericNameValueContext listToContext(List list) throws ContextAwareException {

	GenericNameValueList context = new GenericNameValueList();
	if (list != null && list.size() > 0) {
	    /*
	     * check to see the content of the list, if the elements are of type GenericNameValue then return a
	     * GenericNameValueContext not a GenericNameValueList
	     */
	    if (list.get(0) instanceof GenericNameValue) {
		GenericNameValueContext mapContext = new GenericNameValueContext();
		try {
		    mapContext.putAll(list);
		    return mapContext;
		} catch (ClassCastException e) {
		    /* do nothing just fallback to the default */
		} catch (Exception e) {

		    ExceptionContext ec = new ExceptionContext();
		    ec.put("list", list);
		    throw new ContextAwareException("CONVERSION_ERROR", e, ec);
		}
	    }
	    for (Object o : list) {
		context.addValue(convertToKnownType(o,false));
	    }
	}
	return context;
    }

    public static GenericNameValueContext mapToContext(Map<?, ?> map) throws ContextAwareException {
	GenericNameValueContext context = new GenericNameValueContext();
	for (Object key : map.keySet()) {
	    context.put(key.toString(), convertToKnownType(map.get(key),false));
	}
	return context;
    }

    public static Object getValueFromContext(GenericNameValueContext context, String param) {
	List<String> list = new ArrayList(Arrays.asList(param.split("\\.")));
	return getValueFromContext(context, list);
    }

    public static Object getValueFromContext(GenericNameValueContext context, List<String> names) {
	String currentName = names.remove(0);
	Object value = context.getValue(currentName);
	if (names.size() == 0) {
	    return value;
	} else {
	    return getValueFromContext((GenericNameValueContext) value, names);
	}
    }

    public static Object convertToType(Object source, String type) {
	if (source instanceof GenericNameValueList && type.equals("java.util.List")) {
	    return ((GenericNameValueList) source).getValues();
	}
	return source;
    }

    public static void main(String[] args) {
	
	String c = "{desc.type=String,pass.type=String,pass.secret=true,passagain.type=String,passagain.secret=true, pass.minLength=5, pass.maxLength=20, pass.regExp=/^(\\[0-9a-zA-Z\\])+$/}";
	
	GenericNameValueContextUtil.parse(c);
	    }

}

class ContextParserHandler implements ParseEventHandler {

    public Object handleAssociationEvent(AssociationEvent event) {
	return new GenericNameValue(event.getPrefixValue().toString(), event.getPostfixValue());
    }

    public Object handleEmptyString(String content) {
	return content;
    }

    public Object handleGroupEvent(GroupEvent event) {
	String closeSequence = event.getEndSymbol().getSequence();

	/* we have a list */
	if (closeSequence.equals("]")) {
	    GenericNameValueList newContext = new GenericNameValueList();
	    newContext = new GenericNameValueList();
	    List<?> objects = event.getObjects();
	    for (Object o : objects) {
		newContext.addValue(o);
	    }
	    return newContext;
	} else if (closeSequence.equals("}")) {
	    GenericNameValueContext newContext = new GenericNameValueContext();
	    newContext.putAll((List) event.getObjects());
	    return newContext;
	}
	return null;
    }

}
