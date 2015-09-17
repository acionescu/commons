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
package net.segoia.util.parser.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.segoia.util.data.GenericNameValue;

public class GenericGroup {
    private List<Object> objectsByIndex = new ArrayList<Object>();
    private Map<String,Object> objectsByName = new HashMap<String, Object>();
    
    public GenericGroup(List<Object> o) {
    }

    private void processObjects(List<Object> objects) {
	for(Object o : objects) {
	    if(o instanceof GenericNameValue) {
		GenericNameValue gnv = (GenericNameValue)o;
		objectsByName.put(gnv.getName(), gnv.getValue());
	    }
	    else {
		objectsByIndex.add(o);
	    }
	}
    }
    
    public Object getObjectByIndex(int index) {
	return objectsByIndex.get(index);
    }
    
    public Object getObjectByName(String name) {
	return objectsByName.get(name);
    }
}
