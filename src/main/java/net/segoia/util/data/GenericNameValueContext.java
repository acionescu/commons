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
package net.segoia.util.data;

import java.util.List;
import java.util.Map;


public class GenericNameValueContext extends NameValueContext<Object>{

    /**
     * 
     */
    private static final long serialVersionUID = 2420334225383573936L;
    
    public GenericNameValueContext(){
	
    }
    
    public GenericNameValueContext(List<NameValue<Object>> list){
	putAll(list);
    }

    public void put(String name, Object value){
	GenericNameValue wfp = new GenericNameValue(name,value);
	put(wfp);
    }
    
    public void putMap(Map<String,?> map){
	for(Map.Entry<String,?> entry : map.entrySet()){
	    put(new GenericNameValue(entry.getKey(),entry.getValue()));
	}
    }
    

    public GenericNameValueContext getNew(List<String> params){
	return new GenericNameValueContext(get(params));
    }
}
