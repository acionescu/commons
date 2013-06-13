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

import ro.zg.util.data.GenericNameValue;
import ro.zg.util.parser.ParseEventHandlerConfig;
import ro.zg.util.parser.SymbolFlag;

public class DefaultParseEventHandler implements ParseEventHandler {
    

    public DefaultParseEventHandler() {
	super();
    }
    
    

    public Object handleAssociationEvent(AssociationEvent event) {
	Object prefix = event.getPrefixValue();
	Object postfix = event.getPostfixValue();
	if (prefix != null) {
	    return new GenericNameValue(prefix.toString(), postfix);
	}
	return null;
    }

    public Object handleEmptyString(String content) {
	return content;
    }

    public Object handleGroupEvent(GroupEvent event) {
	List<?> objects = event.getObjects();
//	if (SymbolFlags.SIMPLE.equals(event.getStartSymbol().getAction())) {
	if(event.getStartSymbol().containsFlag(SymbolFlag.SIMPLE)) {
	    if (objects.size() > 0) {
		return objects.get(0);
	    }
	    return "";
	}
	return objects;
    }

}
