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

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ParseResponse {
    Deque<Object> objects;
    
    public ParseResponse(Deque<Object> source){
	this.objects = source;
    }

    /**
     * @return the objects
     */
    public Deque<Object> getObjects() {
        return objects;
    }
    
    public List<Object> getObjectsList(){
	List<Object> list = new ArrayList<Object>();
	while(!objects.isEmpty()) {
	    list.add(objects.pollLast());
	}
	return list;
    }
    
}
