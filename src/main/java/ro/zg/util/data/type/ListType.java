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
package ro.zg.util.data.type;

import ro.zg.util.parser.ParserException;

public class ListType extends ParameterType{
    /**
     * 
     */
    private static final long serialVersionUID = 3540058624249321343L;
    /**
     * Defines the type of the nested elements in the list
     */
    private ParameterType nestedType;
    
    public ListType(){
	setParameterTypeType(LIST_TYPE);
	setType("List");
    }
    
    public ListType(ParameterType nestedType){
	this();
	setNestedType(nestedType);
    }
    
    public String toString(){
	return super.toString()+"["+nestedType+"]";
    }
    
    public String fullGenericTypes() {
	return super.toString()+"["+nestedType.fullGenericTypes()+"]";
    }
    
    public boolean matches(ParameterType c) {
	if(c == null) {
	    return false;
	}
	if(getType().equals(c.getType())) {
	    /* accepts any values */
	    if(nestedType == null || nestedType.getType().equals("")) {
		return true;
	    }
	    
	    ListType cl=(ListType)c;
	    return getNestedType().matches(cl.getNestedType());
	}
	return false;
    }
    
    public static ParameterType fromString(String string) throws ParserException{
	int fi = string.indexOf("[");
	int li = string.indexOf("]");
	
	if(fi >= 0 && li >= 0){
	    return new ListType(ParameterType.fromString(string.substring(fi+1,li)));
	}
	return new ListType();
    }

    public String getTypeForMatchingRule(String rule){
	if("nestedType".equals(rule)){
	    return nestedType.toString();
	}
	return getType();
    }
    
    /**
     * @return the nestedType
     */
    public ParameterType getNestedType() {
        return nestedType;
    }

    /**
     * @param nestedType the nestedType to set
     */
    public void setNestedType(ParameterType nestedType) {
	if(nestedType == null) {
	    this.nestedType = new ParameterType("");
	}
	else {
	    this.nestedType = nestedType;
	}
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((nestedType == null) ? 0 : nestedType.hashCode());
	return result;
    }

    /* (non-Javadoc)
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
	ListType other = (ListType) obj;
	if (nestedType == null) {
	    if (other.nestedType != null)
		return false;
	} else if (!nestedType.equals(other.nestedType))
	    return false;
	return true;
    }

   
}
