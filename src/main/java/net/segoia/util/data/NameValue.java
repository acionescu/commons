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

public class NameValue<V> implements Serializable, Cloneable, Comparable<NameValue<V>>{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1004468423483367851L;

    protected String name;
    
    protected V value;
    
    public NameValue(){
	
    }
    
    public NameValue(String name, V value){
	this.name = name;
	this.value = value;
    }
    
    public static NameValue<?> copy(NameValue<?> source){
	if(source == null){
	    return null;
	}
	NameValue<Object> c = new NameValue<Object>();
	c.setName(source.getName());
	c.setValue(ObjectsUtil.copy(source.getValue()));
	return c;
    }
    
    public NameValue<V> copy(){
	return (NameValue<V>)NameValue.copy(this);
    }

    public String getName() {
        return name;
    }

    public V getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String toString(){
	return name+" "+value;
    }
    
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((value == null) ? 0 : value.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	NameValue<?> other = (NameValue<?>) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (value == null) {
	    if (other.value != null)
		return false;
	} else if (!value.equals(other.value))
	    return false;
	return true;
    }

    public int compareTo(NameValue<V> o) {
	if(this == o){
	    return 0;
	}
	if(value != null && o != null && o.value != null){
	    return ((Comparable<V>)value).compareTo(o.value);
	}
	
	return 0;
    }
    
}
