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

import java.util.List;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.util.data.type.ParameterType;
import ro.zg.util.parser.ParserException;

public class GenericNameValue extends NameValue<Object> {

    /**
     * 
     */
    private static final long serialVersionUID = 6410803957937674441L;

    private String type;

    private ParameterType complexType;

    public GenericNameValue(String name, Object value) {
	super(name, value);
	syncTypeWithValue();
    }

    public GenericNameValue(String name) {
	super(name, null);
    }

    public GenericNameValue() {

    }

    private void syncTypeWithValue() {
	if (value != null) {
	    setType(value.getClass().getSimpleName());
	}
    }

    /**
     * For complex types like List and Context, in case the value is null create
     * an instance of {@link GenericNameValueContext}
     */
    public void initializeComplexValues() {

	if (complexType != null) {
	    String typeType = complexType.getParameterTypeType();
	    if (type.equals("String")) {
		if (value == null) {
		    value = "";
		}
	    } else if (type.equals("Number")) {
		if (value == null) {
		    value = 0;
		}
	    } else if (typeType.equals(ParameterType.MAP_TYPE)) {
		if (value == null) {
		    value = new GenericNameValueContext();
		}
		else if (!(value instanceof GenericNameValueContext)){
		    try {
			value = GenericNameValueContextUtil.convertToKnownType(value);
		    } catch (ContextAwareException e) {
			e.printStackTrace();
		    }
		}
	    } else if (typeType.equals(ParameterType.LIST_TYPE)) {
		if(value==null){
		value = new GenericNameValueList();
		}
		else if(!(value instanceof GenericNameValueList)){
		    try {
			value = GenericNameValueContextUtil.convertToKnownType(value);
		    } catch (ContextAwareException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
    }

    /**
     * Returns a copy of the source instance
     * 
     * @param source
     * @return
     */
    public static GenericNameValue copy(GenericNameValue source) {
	GenericNameValue target = new GenericNameValue(source.getName(),
		ObjectsUtil.copy(source.getValue()));
	return target;
    }

    public String toString() {
	return name + " " + getType() + " " + value;
    }

    public String getTypeForMatchingRule(String rule) {
	if (complexType == null) {
	    return null;
	}
	return complexType.getTypeForMatchingRule(rule);
    }

    public String getType() {
	if (complexType != null) {
	    type = complexType.toString();
	}
	return type;
    }

    public void setType(String type) {
	this.type = type;
	try {
	    this.complexType = ParameterType.fromString(type);
	} catch (ParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void setValue(Object value) {
	this.value = value;
	if (getType() == null) {
	    syncTypeWithValue();
	}
    }

    public void setValueAndOverrideType(Object value) {
	this.value = value;
	syncTypeWithValue();
    }

    public GenericNameValue copy() {
	return GenericNameValue.copy(this);
    }

    /**
     * @return the complexType
     */
    public ParameterType getComplexType() {
	return complexType;
    }

    /**
     * @param complexType
     *            the complexType to set
     */
    public void setComplexType(ParameterType complexType) {
	if (complexType != null) {
	    this.complexType = complexType;
	    type = complexType.toString();
	}

    }

    public boolean isMap() {
	return (value != null && value instanceof Map);
    }

    public boolean isList() {
	return (value != null && value instanceof List);
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
	result = prime * result
		+ ((complexType == null) ? 0 : complexType.hashCode());
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
	GenericNameValue other = (GenericNameValue) obj;
	if (complexType == null) {
	    if (other.complexType != null)
		return false;
	} else if (!complexType.equals(other.complexType))
	    return false;
	return true;
    }

}
