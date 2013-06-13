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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ProxyMap<K,V> implements Map<K,V>{
    private Map<K,V> source;
    private Map<K,V> face;
    public ProxyMap(Map<K,V> source){
	this.source = source;
	face = new LinkedHashMap<K, V>(source);
    }
    
    public void clear() {
	source.clear();
	face.clear();
    }

    public boolean containsKey(Object key) {
	return source.containsKey(key);
    }

    public boolean containsValue(Object value) {
	return source.containsValue(value);
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
	return face.entrySet();
    }

    public V get(Object key) {
	return source.get(key);
    }

    public boolean isEmpty() {
	return source.isEmpty();
    }

    public Set<K> keySet() {
	return face.keySet();
    }

    public V put(K key, V value) {
	source.put(key, value);
	return face.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
	source.putAll(m);
	face.putAll(m);
	
    }

    public V remove(Object key) {
	source.remove(key);
	return face.remove(key);
    }

    public int size() {
	return source.size();
    }

    public Collection<V> values() {
	return face.values();
    }

    public String toString(){
	return face.toString();
    }
}
