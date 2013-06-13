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

public class KeyValue<K,V> {
    private K key;
    private V value;
    
    public KeyValue() {
	
    }
    
    public KeyValue(K key, V value) {
	this.key = key;
	this.value = value;	
    }
    
    /**
     * @return the key
     */
    public K getKey() {
        return key;
    }
    /**
     * @return the value
     */
    public V getValue() {
        return value;
    }
    /**
     * @param key the key to set
     */
    public void setKey(K key) {
        this.key = key;
    }
    /**
     * @param value the value to set
     */
    public void setValue(V value) {
        this.value = value;
    }
    
}
