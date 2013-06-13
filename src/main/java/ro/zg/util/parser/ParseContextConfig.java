/*******************************************************************************
 * Copyright 2012 Adrian Cristian Ionescu
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParseContextConfig {
    private SymbolSet nestedSymbols = new SymbolSet();
    private String escapeCharacter = "\\";
    private boolean useEscapeCharacterOn;
    private boolean overrideSuperContextConfig;
    private boolean docStartEventOn;

    private List<String> flags = new ArrayList<String>();
    
    public void configFromSymbol(Symbol s) {
	addFlags(s.getFlags());
	setDocStartEventOn(true);
    }
    

    public List<Symbol> getNestedSymbol(String sequence) {
	return nestedSymbols.get(sequence);
    }

    public boolean checkPotentialNestedSymbolMatch(String sequence) {
	for (String s : nestedSymbols.keySet()) {
	    if (s.startsWith(sequence)) {
		return true;
	    }
	}
	return false;
    }

    private void addFlag(String flag, boolean update) {
	if (!flags.contains(flag)) {
	    flags.add(flag);
	    if (update) {
		update();
	    }
	}
    }

    public void addFlag(String flag) {
	addFlag(flag, true);
    }

    public void addFlags(Collection<String> flags) {
	for (String f : flags) {
	    addFlag(f,false);
	}
	update();
    }

    public boolean containsFlag(String flag) {
	return flags.contains(flag);
    }

    private void update() {
	getNestedSymbols().setCaseInsensitive(containsFlag(SymbolFlag.CASE_INSENSITIVE));
    }

    /**
     * @return the flags
     */
    public List<String> getFlags() {
	return flags;
    }

    /**
     * @return the nestedSymbols
     */
    public SymbolSet getNestedSymbols() {
	return nestedSymbols;
    }

    /**
     * @return the escapeCharacter
     */
    public String getEscapeCharacter() {
	return escapeCharacter;
    }

    /**
     * @return the useEscapeCharacterOn
     */
    public boolean isUseEscapeCharacterOn() {
	return useEscapeCharacterOn;
    }

    /**
     * @return the overrideSuperContextConfig
     */
    public boolean isOverrideSuperContextConfig() {
	return overrideSuperContextConfig;
    }

    /**
     * @param nestedSymbols
     *            the nestedSymbols to set
     */
    public void setNestedSymbols(SymbolSet nestedSymbols) {
	this.nestedSymbols = nestedSymbols;
    }

    /**
     * @param escapeCharacter
     *            the escapeCharacter to set
     */
    public void setEscapeCharacter(String escapeCharacter) {
	this.escapeCharacter = escapeCharacter;
    }

    /**
     * @param useEscapeCharacterOn
     *            the useEscapeCharacterOn to set
     */
    public void setUseEscapeCharacterOn(boolean useEscapeCharacterOn) {
	this.useEscapeCharacterOn = useEscapeCharacterOn;
    }

    /**
     * @param overrideSuperContextConfig
     *            the overrideSuperContextConfig to set
     */
    public void setOverrideSuperContextConfig(boolean overrideSuperContextConfig) {
	this.overrideSuperContextConfig = overrideSuperContextConfig;
    }

    /**
     * @return the docStartEventOn
     */
    public boolean isDocStartEventOn() {
        return docStartEventOn;
    }

    /**
     * @param docStartEventOn the docStartEventOn to set
     */
    public void setDocStartEventOn(boolean docStartEventOn) {
        this.docStartEventOn = docStartEventOn;
    }
    
    
}
