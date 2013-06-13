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

import java.util.List;

import ro.zg.util.data.ListMap;

public class SymbolSet extends ListMap<String,Symbol>{
    /**
     * 
     */
    private static final long serialVersionUID = 3953425950224588975L;
    private int maxSequeceLength;
    private boolean caseInsensitive;
    
    public void add(String sequence, Symbol symbol) {
	super.add(sequence,symbol);
	updateMaxSequenceLength(sequence.length());
	
    }
    
    public void add(Symbol symbol) {
	add(symbol.getSequence(),symbol);
    }
    
    private void updateMaxSequenceLength(int seqLength) {
	if(maxSequeceLength < seqLength) {
	    maxSequeceLength = seqLength;
	}
    }

    public List<Symbol> getSymbolsForSequence(String sequence){
	if(isCaseInsensitive()) {
	    sequence=sequence.toLowerCase();
	}
	/* iterate over the symbols from this set */
	for(String symbSeq : this.keySet()) {
//	    System.out.println("sequence: '"+sequence+"' +symb: '"+symbSeq+"'");
	    if(sequence.startsWith(symbSeq)) {
		/* the first sequence found is also the one with the highest priority, so return all the 
		 * symbols with that sequence */
		return get(symbSeq);
	    }
	}
	return null;
    }
    
    /**
     * @return the maxSequeceLength
     */
    public int getMaxSequeceLength() {
        return maxSequeceLength;
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
    
}
