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
package ro.zg.util.data;

import java.util.HashMap;
import java.util.Map;

public class MapMap<K1,K2,V> extends HashMap<K1,Map<K2,V>>{
    
    public V getNestedValue(K1 key1, K2 key2) {
	Map<K2,V> nestedMap = get(key1);
	if(nestedMap == null) {
	    return null;
	}
	return nestedMap.get(key2);
    }
    
    public V putNestedValue(K1 key1, K2 key2, V value) {
	Map<K2,V> nestedMap = get(key1);
	if(nestedMap == null) {
	    nestedMap=new HashMap<K2, V>();
	    put(key1,nestedMap);
	}
	return nestedMap.put(key2, value);
    }
}
