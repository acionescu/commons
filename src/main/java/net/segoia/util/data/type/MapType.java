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
package net.segoia.util.data.type;

import java.util.ArrayList;
import java.util.List;

import net.segoia.util.data.NameValue;
import net.segoia.util.data.NameValueContext;

public class MapType extends ParameterType {
    /**
     * 
     */
    private static final long serialVersionUID = 184523013577895001L;
    private NameValueContext<ParameterType> typesContext = new NameValueContext<ParameterType>();
    private List<NameValue<ParameterType>> typesList = new ArrayList<NameValue<ParameterType>>();

    public MapType() {
	setParameterTypeType(MAP_TYPE);
	setType("Context");
    }

    public String toString() {
	getTypesContext();
	String output = super.toString() + "[";
	if (typesContext != null) {
	    int index = 0;
	    for (NameValue<ParameterType> np : typesContext.getParameters().values()) {
		if (index++ != 0) {
		    output += ",";
		}
		output += np.getName() + "=" + np.getValue();
	    }
	}
	output += "]";
	return output;
    }
    
    public boolean matches(ParameterType c) {
	if(c == null) {
	    return false;
	}
	if(getType().equals(c.getType())) {
	    /* accepts any values */
	    if(typesList==null || typesList.size()==0) {
		return true;
	    }
	    MapType mt = (MapType)c;
	   NameValueContext<ParameterType> mtTypes = mt.getTypesContext();
	    for(NameValue<ParameterType> p : typesList) {
		/* accept any type */
		if(p.getName().equals(".*")) {
		    if(p.getValue().getType().equals(ParameterType.GENERIC_TYPE)) {
			return true;
		    }
		    /* check all nested types for the argument */
		    for(ParameterType ct : mtTypes.getValues()) {
			boolean nestedTypeMatches = p.getValue().matches(ct);
			if(!nestedTypeMatches) {
			    return false;
			}
		    }
		}
		boolean nestedTypeMatches = p.getValue().matches(mtTypes.getValue(p.getName()));
		if(!nestedTypeMatches) {
		    return false;
		}
	    }
	    
	}
	return true;
    }

    public String getTypeForMatchingRule(String rule){
	if(rule != null ){
	    NameValue<ParameterType> nt = typesContext.get(rule);
	    if(nt != null){
		return nt.getValue().getType();
	    }
	    return null;
	}
	return getType();
    }

    /**
     * @return the typesContext
     */
    public NameValueContext<ParameterType> getTypesContext() {
	if (typesList != null) {
	    typesContext.clear();
	    typesContext.putAll(typesList);
	}
	return typesContext;
    }

    public String fullGenericTypes() {
	return super.toString()+"[]";
    }
    
    
    /**
     * @param typesContext
     *            the typesContext to set
     */
    public void setTypesContext(NameValueContext<ParameterType> typesContext) {
	this.typesContext = typesContext;
	typesList = typesContext.getParametersAsList();
    }

    /**
     * @return the typesList
     */
    public List<NameValue<ParameterType>> getTypesList() {
	return typesList;
    }

    /**
     * @param typesList
     *            the typesList to set
     */
    public void setTypesList(List<NameValue<ParameterType>> typesList) {
	this.typesList = typesList;
	typesContext.clear();
	typesContext.putAll(typesList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((typesList == null) ? 0 : typesList.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	MapType other = (MapType) obj;
	if (typesList == null) {
	    if (other.typesList != null)
		return false;
	} else if (!typesList.equals(other.typesList))
	    return false;
	return true;
    }

}
