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

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Simple LRU cache implementation based on LinkedHashMap
 * @author adi
 *
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    /**
     * 
     */
    private static final long serialVersionUID = 2179632799549660511L;
    
    /** 
     * Maximum number of items in the cache. 
     */
    private int capacity;

    public LRUCache(int capacity, float loadFactor) {
	/* call super constructor with access order set to true */
	super(capacity, loadFactor, true); 
	this.capacity = capacity;
    }
    
    public LRUCache(int capacity) {
	this(capacity,0.75f);
    }

    protected boolean removeEldestEntry(Entry<K, V> entry) {
	return (size() > capacity);
    }
}