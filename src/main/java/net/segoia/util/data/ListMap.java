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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListMap<K,T> implements Serializable, Cloneable{
    /**
     * 
     */
    private static final long serialVersionUID = -1703006626599612175L;
    private Map<K,List<T>> backedMap = new LinkedHashMap<K, List<T>>();
    
    public ListMap() {
	
    }
    
    public ListMap(ListMapFactory<K, T> factory) {
	backedMap = factory.createMap();
    }
    
    public void add(K key, T value){
	List<T> defList = backedMap.get(key);
	if(defList == null){
	    defList = new ArrayList<T>();
	    backedMap.put(key, defList);
	}
	if(!defList.contains(value)){
	    defList.add(value);
	}
    }
    
    public void add(K key, List<T> values){
	for(T value : values){
	    add(key,value);
	}
    }
    
    public void add(Map<K,List<T>> map){
	if(map == null || map.size() <= 0) {
	    return;
	}
	for(Map.Entry<K, List<T>> e : map.entrySet()){
	    add(e.getKey(),e.getValue());
	}
    }
    
    public List<T> remove(K key){
	return backedMap.remove(key);
    }
    
    public boolean remove(K key, T value) {
	List<T> values = get(key);
	if(values != null) {
	    return values.remove(value);
	}
	return false;
    }
    
    public void removeValue(T value) {
	for(List<T> list : backedMap.values()) {
	    list.remove(value);
	}
    }
    
    public List<T> get(K key){
	return backedMap.get(key);
    }
    
    public Set<K> keySet(){
	return backedMap.keySet();
    }
    
    public Map<K,List<T>> getAll(){
	return backedMap;
    }
    
    public Collection<List<T>> values(){
	return backedMap.values();
    }
    
    public void clear(){
	backedMap.clear();
    }
    
    public int getTotalSize() {
	int sum = 0;
	for(List<T> list : backedMap.values()) {
	    sum += list.size();
	}
	return sum;
    }
    
    public int size() {
	return backedMap.size();
    }
   
    public Set<Map.Entry<K,List<T>>> entrySet(){
	return backedMap.entrySet();
    }
    
    public boolean containsValueForKey(K key, T value) {
	List<T> values = get(key);
	if(values == null) {
	    return false;
	}
	return values.contains(value);
    }
 }
