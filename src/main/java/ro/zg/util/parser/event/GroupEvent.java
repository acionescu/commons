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
package ro.zg.util.parser.event;

import java.util.List;

import ro.zg.util.parser.Symbol;

public class GroupEvent{
    private List<?> objects;
    private Object prefixValue;
    private int startIndex;
    private Symbol startSymbol;
    private Symbol endSymbol;
    
    /**
     * @return the objects
     */
    public List<?> getObjects() {
        return objects;
    }

    /**
     * @param objects the objects to set
     */
    public void setObjects(List<?> objects) {
        this.objects = objects;
    }

    /**
     * @return the prefixValue
     */
    public Object getPrefixValue() {
        return prefixValue;
    }

    /**
     * @param prefixValue the prefixValue to set
     */
    public void setPrefixValue(Object prefixValue) {
        this.prefixValue = prefixValue;
    }

    /**
     * @return the startIndex
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * @return the startSymbol
     */
    public Symbol getStartSymbol() {
        return startSymbol;
    }

    /**
     * @return the endSymbol
     */
    public Symbol getEndSymbol() {
        return endSymbol;
    }

    /**
     * @param startIndex the startIndex to set
     */
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * @param startSymbol the startSymbol to set
     */
    public void setStartSymbol(Symbol startSymbol) {
        this.startSymbol = startSymbol;
    }

    /**
     * @param endSymbol the endSymbol to set
     */
    public void setEndSymbol(Symbol endSymbol) {
        this.endSymbol = endSymbol;
    }
    
    
}
