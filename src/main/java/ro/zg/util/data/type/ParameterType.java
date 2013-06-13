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
package ro.zg.util.data.type;

import java.io.Serializable;

import ro.zg.util.data.GenericNameValue;
import ro.zg.util.parser.Parser;
import ro.zg.util.parser.ParserException;
import ro.zg.util.parser.ParserHandlerFactory;
import ro.zg.util.parser.Symbol;
import ro.zg.util.parser.SymbolType;

public class ParameterType implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -3552870341388896149L;
    public static final String SIMPLE_TYPE = "SIMPLE";
    public static final String LIST_TYPE = "List";
    public static final String MAP_TYPE = "Context";
    public static final String GENERIC_TYPE = "Any";
    private static Parser parser;

    /**
     * The type of {@link ParameterType} should identify the subclass
     */
    private String parameterTypeType = SIMPLE_TYPE;
    /**
     * Defines the type of the value held by the {@link GenericNameValue}
     */
    private String type;

    public ParameterType() {

    }

    public ParameterType(String type) {
	this.type = type;
    }

    public String toString() {
	return type;
    }

    private static void initParser() {
	if (parser != null) {
	    return;
	}
	ParameterTypeParseHandler handler = new ParameterTypeParseHandler();
	parser = new Parser();
	parser.setHandlerFactory(new ParserHandlerFactory(handler));

	Symbol symbol = new Symbol("[", SymbolType.GROUP_START);
	symbol.addNestedSymbol(new Symbol("]", SymbolType.GROUP_END));
	symbol.addNestedSymbol(new Symbol("=", SymbolType.ASSOCIATE));
	symbol.addNestedSymbol(new Symbol(",", SymbolType.SEPARATE));
	symbol.addNestedSymbol(symbol);

	parser.addSymbol(symbol);
    }

    public static ParameterType fromString(String string) throws ParserException {
	initParser();
	if (string == null || "".equals(string)) {
	    return null;
	}
	try {
	    if (string.startsWith(LIST_TYPE+"[")) {
		return (ParameterType) parser.parse(string).getObjects().pop();
	    } else if (string.startsWith(MAP_TYPE)) {
		return (ParameterType) parser.parse(string).getObjects().pop();
	    } else {
		return new ParameterType(string);
	    }
	} catch (Exception e) {
	    throw new ParserException("Failed to parse type '"+string+"'",e);
	}
    }

    /**
     * Check if the argument type is compatible with this one
     * 
     * @param c
     * @return
     */
    public boolean matches(ParameterType c) {
	if (GENERIC_TYPE.equals(type)) {
	    return true;
	}
	if (c == null) {
	    return false;
	}
	return type.equals(c.getType());
    }

    public String fullGenericTypes() {
	return "";
    }

    public String getTypeForMatchingRule(String rule) {
	return type;
    }

    public static void main(String[] args) throws ParserException {
	new ParameterType();
	String s = "Context[a=String,b=Number,c=List[Number],m=Date,d=Context[f=String,e=Number],z=h]";
	ParameterType pt = ParameterType.fromString(s);
	System.out.println("res is = " + pt);
	System.out.println(ParameterType.fromString("List[Number]"));
	MapType mp = (MapType) ParameterType.fromString("Context[b=String]");
	MapType mtc = (MapType) ParameterType.fromString("Context[]");
	System.out.println(mp.matches(mtc));

	ListType ml = (ListType) ParameterType.fromString("List[String]");
	ListType mlc = (ListType) ParameterType.fromString("List[Any]");
	System.out.println(ml.matches(mlc));
	// System.out.println(ParameterType.fromString("List[Context[id=String]]"));
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    /**
     * @return the parameterTypeType
     */
    public String getParameterTypeType() {
	return parameterTypeType;
    }

    /**
     * @param parameterTypeType
     *            the parameterTypeType to set
     */
    protected void setParameterTypeType(String parameterTypeType) {
	this.parameterTypeType = parameterTypeType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((parameterTypeType == null) ? 0 : parameterTypeType.hashCode());
	result = prime * result + ((type == null) ? 0 : type.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ParameterType other = (ParameterType) obj;
	if (parameterTypeType == null) {
	    if (other.parameterTypeType != null)
		return false;
	} else if (!parameterTypeType.equals(other.parameterTypeType))
	    return false;
	if (type == null) {
	    if (other.type != null)
		return false;
	} else if (!type.equals(other.type))
	    return false;
	return true;
    }

}
