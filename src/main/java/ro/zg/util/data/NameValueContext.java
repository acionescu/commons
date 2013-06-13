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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



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
    
}
