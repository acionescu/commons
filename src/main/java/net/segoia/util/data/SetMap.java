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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SetMap<K, V> implements Map<K, Set<V>>, Serializable {
    private Map<K, Set<V>> nestedMap;
    private SetFactory<V> nestedSetFactory;

    /**
     * 
     */
    private static final long serialVersionUID = -7712790543550126181L;

    public SetMap() {
	nestedMap = new HashMap<K, Set<V>>();
	nestedSetFactory = new HashSetFactory<V>();
    }

    public SetMap(Map<K, Set<V>> nestedMap, SetFactory<V> setFactory) {
	this.nestedMap = nestedMap;
	this.nestedSetFactory = setFactory;
    }

    public static <K, V> SetMap<K, V> createHashMapWithHashSet() {

	return new SetMap<K, V>(new HashMap<K, Set<V>>(),
		new SetMap.HashSetFactory<V>());
    }

    public static <K, V> SetMap<K, V> createTreeMapWithTreeSet() {
	return new SetMap<K, V>(new TreeMap<K, Set<V>>(),
		new SetMap.TreeSetFactory<V>());
    }

    public void add(K key, V value) {
	Set<V> nestedSet = getNestedSet(key);
	nestedSet.add(value);
    }

    public Set<V> getNestedSet(K key) {
	Set<V> nestedSet = get(key);
	if (nestedSet == null) {
	    nestedSet = nestedSetFactory.createSet();
	    put(key, nestedSet);
	}
	return nestedSet;
    }

    public void putAllNestedValues(K key, Collection<V> values) {
	Set<V> nestedSet = getNestedSet(key);
	nestedSet.addAll(values);
    }

    public boolean containsNestedValue(K key, V value) {
	Set<V> nested = get(key);
	if (nested == null) {
	    return false;
	}
	return nested.contains(value);
    }

    public Set<V> getIntersection() {
	Set<V> intersection = new HashSet<V>();
	boolean first = true;
	for (Set<V> s : values()) {
	    if (first) {
		intersection = s;
		first = false;
	    } else {
		intersection.retainAll(s);
	    }
	}
	return intersection;
    }

    public Set<V> getUnion() {
	Set<V> union = new HashSet<V>();
	for (Set<V> s : values()) {
	    union.addAll(s);
	}
	return union;
    }
    
    public Map<K,Set<V>> getAll(){
	return nestedMap;
    }

    @Override
    public int size() {
	return nestedMap.size();
    }

    @Override
    public boolean isEmpty() {
	return nestedMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
	return nestedMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
	return nestedMap.containsValue(value);
    }

    @Override
    public Set<V> get(Object key) {
	return nestedMap.get(key);
    }

    @Override
    public Set<V> put(K key, Set<V> value) {
	return nestedMap.put(key, value);
    }

    @Override
    public Set<V> remove(Object key) {
	return nestedMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends Set<V>> m) {
	nestedMap.putAll(m);
    }

    @Override
    public void clear() {
	nestedMap.clear();
    }

    @Override
    public Set<K> keySet() {
	return nestedMap.keySet();
    }

    @Override
    public Collection<Set<V>> values() {
	return nestedMap.values();
    }

    @Override
    public Set<java.util.Map.Entry<K, Set<V>>> entrySet() {
	return nestedMap.entrySet();
    }

    public static interface SetFactory<T> extends Serializable{
	Set<T> createSet();
    }

    public static class HashSetFactory<T> implements SetFactory<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5493174035691826492L;

	@Override
	public Set<T> createSet() {
	    return new HashSet<T>();
	}

    }

    public static class TreeSetFactory<T> implements SetFactory<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1403320248186453906L;

	@Override
	public Set<T> createSet() {
	    return new TreeSet<T>();
	}

    }
    
    public static class LinkedHashSetFactory<T> implements SetFactory<T> {

	@Override
	public Set<T> createSet() {
	    return new LinkedHashSet<T>();
	}
	
    }
}
