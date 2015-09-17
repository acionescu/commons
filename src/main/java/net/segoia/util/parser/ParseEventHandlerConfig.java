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

public class ParseEventHandlerConfig {
    private String outputCharsToBeEscaped;
    private String outputEscapeChar;
    private String startEscapeGroup="";
    private String endEscapeGroup="";
    
    public ParseEventHandlerConfig(String outputCharsToBeEscaped, String outputEscapeChar) {
	super();
	this.outputCharsToBeEscaped = outputCharsToBeEscaped;
	this.outputEscapeChar = outputEscapeChar;
    }
    
    
    public ParseEventHandlerConfig(String outputCharsToBeEscaped, String outputEscapeChar, String startEscapeGroup,
	    String endEscapeGroup) {
	super();
	this.outputCharsToBeEscaped = outputCharsToBeEscaped;
	this.outputEscapeChar = outputEscapeChar;
	this.startEscapeGroup = startEscapeGroup;
	this.endEscapeGroup = endEscapeGroup;
    }


    /**
     * @return the outputCharsToBeEscaped
     */
    public String getOutputCharsToBeEscaped() {
        return outputCharsToBeEscaped;
    }
    /**
     * @return the outputEscapeChar
     */
    public String getOutputEscapeChar() {
        return outputEscapeChar;
    }
    /**
     * @param outputCharsToBeEscaped the outputCharsToBeEscaped to set
     */
    public void setOutputCharsToBeEscaped(String outputCharsToBeEscaped) {
        this.outputCharsToBeEscaped = outputCharsToBeEscaped;
    }
    /**
     * @param outputEscapeChar the outputEscapeChar to set
     */
    public void setOutputEscapeChar(String outputEscapeChar) {
        this.outputEscapeChar = outputEscapeChar;
    }
    
    
    
    
    /**
     * @return the startEscapeGroup
     */
    public String getStartEscapeGroup() {
        return startEscapeGroup;
    }


    /**
     * @return the endEscapeGroup
     */
    public String getEndEscapeGroup() {
        return endEscapeGroup;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((endEscapeGroup == null) ? 0 : endEscapeGroup.hashCode());
	result = prime * result + ((outputCharsToBeEscaped == null) ? 0 : outputCharsToBeEscaped.hashCode());
	result = prime * result + ((outputEscapeChar == null) ? 0 : outputEscapeChar.hashCode());
	result = prime * result + ((startEscapeGroup == null) ? 0 : startEscapeGroup.hashCode());
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
	ParseEventHandlerConfig other = (ParseEventHandlerConfig) obj;
	if (endEscapeGroup == null) {
	    if (other.endEscapeGroup != null)
		return false;
	} else if (!endEscapeGroup.equals(other.endEscapeGroup))
	    return false;
	if (outputCharsToBeEscaped == null) {
	    if (other.outputCharsToBeEscaped != null)
		return false;
	} else if (!outputCharsToBeEscaped.equals(other.outputCharsToBeEscaped))
	    return false;
	if (outputEscapeChar == null) {
	    if (other.outputEscapeChar != null)
		return false;
	} else if (!outputEscapeChar.equals(other.outputEscapeChar))
	    return false;
	if (startEscapeGroup == null) {
	    if (other.startEscapeGroup != null)
		return false;
	} else if (!startEscapeGroup.equals(other.startEscapeGroup))
	    return false;
	return true;
    }
    
    
}
