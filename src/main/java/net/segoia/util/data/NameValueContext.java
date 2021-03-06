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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;



public class NameValueContext<P> extends ParameterContext<NameValue<P>>{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1464476755174748477L;
    
    public void copyTo(NameValueContext target){
	for(NameValue<?> p : parameters.values()){
	    target.put(p.copy());
	}
    }
    
    public NameValue<P> put(NameValue<P> parameter) {
	if (parameter == null) {
	    return null;
	}
	String name = parameter.getName();
	return put(name, parameter);
    }

    /**
     * Adds a new parameter to the context. In case another parameter with the same name exists already on the context,
     * it will be removed and returned at the end of the function. Note: this is not synchronized because it should not
     * be any case when multiple threads access the same {@link ParameterContext} instance
     * 
     * @param name
     * @param p
     * @return - the old value for the parameter with the specified name already existing on the context
     */
    public NameValue<P> put(String name, NameValue<P> p) {
	/* check if another parameter with this name exists */
	NameValue<P> oldValue = remove(name);
	if(p == null || p.getValue() == null){
	    return oldValue;
	}
	p.setName(name);
	parameters.put(name, p);
	return oldValue;
    }
    
    public P getValue(String name){
	NameValue<P> p = get(name);
	if(p != null){
	    return p.getValue();
	}
	return null;
    }
    
    public P removeValue(String name) {
	NameValue<P> p = remove(name);
	if(p != null){
	    return p.getValue();
	}
	return null;
    }
    
    public List<P> getValues(List<String> names){
	List<P> values = new ArrayList<P>();
	for(String name : names){
	    NameValue<P> p = parameters.get(name);
	    if(p != null){
		values.add(p.getValue());
	    }
	    else{
		values.add(null);
	    }
	}
	return values;
    }
    
    public List<P> getValues(){
	List<P> values = new ArrayList<P>();
	for(NameValue<P> nv : parameters.values()) {
	    values.add(nv.getValue());
	}
	return values;
    }
    
    public Map<String,Object> getNameValuesAsMap(){
	LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	for(Map.Entry<String, NameValue<P>> entry : parameters.entrySet()){
	    NameValue<P> p = entry.getValue();
	    map.put(p.getName(), p.getValue());
	}
	return map;
    }
    
    public Map<String, Object> getNameValuesAsMapFull(){
	LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	for(Map.Entry<String, NameValue<P>> entry : parameters.entrySet()){
	    NameValue<P> p = entry.getValue();
	    P value = p.getValue();
	    if(value instanceof NameValueContext) {
		NameValueContext c = (NameValueContext)value;
		map.put(p.getName(), c.getNameValuesAsMapFull());
	    }
	    else {
		map.put(p.getName(), value);
	    }
	    
	}
	return map;
    }
    
    public String toJsonString() {
	return new Gson().toJson(getNameValuesAsMap());
    }
    
    public String toJsonStringFull() {
	return new Gson().toJson(getNameValuesAsMapFull());
    }
    
    public boolean containsValue(P value) {
	if(value == null) {
	    return false;
	}
	for(NameValue<P> nv : parameters.values()) {
	    if(value.equals(nv.getValue())) {
		return true;
	    }
	}
	return false;
    }
    
    public boolean matches(String regex) {
	if(regex == null) {
	    return false;
	}
	for(NameValue<P> nv : parameters.values()) {
	    if(nv.getValue().toString().matches(regex)) {
		return true;
	    }
	}
	return false;
    }
    
    public static void main(String[] args) {
	GenericNameValueList l = new GenericNameValueList();
	
	String s = "USER_TYPE:HAWPI:admin-RESOURCE_TYPE-ACCESS_TYPE:WRITE";
	
	l.addValue(s);
	
	System.out.println(l.matches(".*-RESOURCE_TYPE-ACCESS_TYPE:WRITE"));
    }
}
