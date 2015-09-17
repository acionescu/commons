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

public class ParserConfig {
    private String symbols;
    private boolean caseInsensitive;
    private ParseEventHandlerConfig parseEventHandlerConfig;/*=new ParseEventHandlerConfig("\\[{", "\\");*/

    public ParserConfig(String symbols, String outputCharsToBeEscaped, String outputEscapeChar) {
	super();
	this.symbols = symbols;
	if (outputCharsToBeEscaped != null && outputEscapeChar != null) {
	    this.parseEventHandlerConfig = new ParseEventHandlerConfig(outputCharsToBeEscaped, outputEscapeChar);
	}
    }

    /**
     * @return the symbols
     */
    public String getSymbols() {
	return symbols;
    }

    /**
     * @param symbols
     *            the symbols to set
     */
    public void setSymbols(String symbols) {
	this.symbols = symbols;
    }

    /**
     * @return the parseEventHandlerConfig
     */
    public ParseEventHandlerConfig getParseEventHandlerConfig() {
	return parseEventHandlerConfig;
    }

    /**
     * @param parseEventHandlerConfig
     *            the parseEventHandlerConfig to set
     */
    public void setParseEventHandlerConfig(ParseEventHandlerConfig parseEventHandlerConfig) {
	this.parseEventHandlerConfig = parseEventHandlerConfig;
    }

    /**
     * @return the caseInsensitive
     */
    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    /**
     * @param caseInsensitive the caseInsensitive to set
     */
    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (caseInsensitive ? 1231 : 1237);
	result = prime * result + ((parseEventHandlerConfig == null) ? 0 : parseEventHandlerConfig.hashCode());
	result = prime * result + ((symbols == null) ? 0 : symbols.hashCode());
	return result;
    }

    /* (non-Javadoc)
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
	ParserConfig other = (ParserConfig) obj;
	if (caseInsensitive != other.caseInsensitive)
	    return false;
	if (parseEventHandlerConfig == null) {
	    if (other.parseEventHandlerConfig != null)
		return false;
	} else if (!parseEventHandlerConfig.equals(other.parseEventHandlerConfig))
	    return false;
	if (symbols == null) {
	    if (other.symbols != null)
		return false;
	} else if (!symbols.equals(other.symbols))
	    return false;
	return true;
    }

   

}
