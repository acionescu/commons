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
package net.segoia.util.data.type;

import java.util.List;

import net.segoia.util.data.NameValue;
import net.segoia.util.data.NameValueContext;
import net.segoia.util.parser.ParserException;
import net.segoia.util.parser.event.AssociationEvent;
import net.segoia.util.parser.event.GroupEvent;
import net.segoia.util.parser.event.ParseEventHandler;

public class ParameterTypeParseHandler implements ParseEventHandler {

    public Object handleAssociationEvent(AssociationEvent event) {
	return new NameValue<ParameterType>(event.getPrefixValue().toString(), (ParameterType) event.getPostfixValue());
    }

    public Object handleEmptyString(String content)  {
	try {
	    return ParameterType.fromString(content);
	} catch (ParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    public Object handleGroupEvent(GroupEvent event) {
	String prevValue = event.getPrefixValue().toString();
	if (ParameterType.MAP_TYPE.equals(prevValue)) {
	    MapType mt = new MapType();
	    // mt.setNestedTypes(event.getObjects());
	    NameValueContext<ParameterType> typesContext = new NameValueContext<ParameterType>();
	    typesContext.putAll((List)event.getObjects());
	    mt.setTypesContext(typesContext);
	    return mt;
	} else if (ParameterType.LIST_TYPE.equals(prevValue)) {
	    ListType lt = new ListType();
	    lt.setNestedType((ParameterType) event.getObjects().get(0));
	    return lt;
	}
	return null;
    }

}
