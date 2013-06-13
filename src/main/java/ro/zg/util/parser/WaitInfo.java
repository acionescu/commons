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

public class WaitInfo {
    /**
     * Number of elements in the available stack when this wait info was created
     */
    private int availableStackIndex;
    /**
     * associated symbol
     */
    private Symbol symbol;
    /**
     * the value situated before the associated symbol
     */
    private Object previousValue;
    /**
     * The index where the symbol was found
     */
    private int foundIndex;
    
    public WaitInfo(Symbol symbol, Object prevValue, int stackIndex){
	this.symbol = symbol;
	this.previousValue = prevValue;
	this.availableStackIndex = stackIndex;
    }
    
    public String toString(){
	return symbol.getSequence() +" at "+foundIndex;
    }
    
    
    /**
     * @return the availableStackIndex
     */
    public int getAvailableStackIndex() {
        return availableStackIndex;
    }
    /**
     * @return the symbol
     */
    public Symbol getSymbol() {
        return symbol;
    }
    /**
     * @return the previousValue
     */
    public Object getPreviousValue() {
        return previousValue;
    }
    /**
     * @param availableStackIndex the availableStackIndex to set
     */
    public void setAvailableStackIndex(int availableStackIndex) {
        this.availableStackIndex = availableStackIndex;
    }
    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
    /**
     * @param previousValue the previousValue to set
     */
    public void setPreviousValue(Object previousValue) {
        this.previousValue = previousValue;
    }


    /**
     * @return the foundIndex
     */
    public int getFoundIndex() {
        return foundIndex;
    }


    /**
     * @param foundIndex the foundIndex to set
     */
    public void setFoundIndex(int foundIndex) {
        this.foundIndex = foundIndex;
    }
    
}
