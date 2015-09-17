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

import java.util.List;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.util.data.reflection.ReflectionUtility;
import net.segoia.util.validation.ValidationRule;

/**
 * Defines an user input parameter
 * @author adi
 *
 */
public class UserInputParameter extends GenericNameValue{

    /**
     * 
     */
    private static final long serialVersionUID = 9185069769843446020L;
    
    public static final String UNKNOWN_VALUE="UNKNOWN_VALUE";
    public static final String UNKNOWN_LARGE_VALUE="UNKNOWN_LARGE_VALUE";
    public static final String VALUE_FROM_LIST="VALUE_FROM_LIST";
    public static final String SECRET_VALUE="SECRET_VALUE";
    public static final String MAP_VALUE="MAP_VALUE";
    public static final String LIST_VALUE="LIST_VALUE";
    
    private boolean mandatory;
    /** 
     * allowed values for this parameter
     */
    private List<String> allowedValues;
    
    private String innerName;
    
    private Object defaultValue;
    
    private List<ValidationRule> validationRules;
    
    private String inputType= UNKNOWN_VALUE;
    
    public void setValue(Object value){
	super.setValue(value);
	convertValueToType();
    }
    
    public void setType(String type){
	super.setType(type);
	convertValueToType();
    }
    
    private void convertValueToType(){
	Object value = getValue();
	String type = getType();
	if(value != null && type != null && !type.equals(value.getClass().getSimpleName())){
	    try {
		super.setValue(ReflectionUtility.createObjectByTypeAndValue(type, value.toString()));
	    } catch (ContextAwareException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } 
	}
    }
    
    /**
     * @return the mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * @return the allowedValues
     */
    public List<String> getAllowedValues() {
        return allowedValues;
    }

    /**
     * @param mandatory the mandatory to set
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * @param allowedValues the allowedValues to set
     */
    public void setAllowedValues(List<String> allowedValues) {
        this.allowedValues = allowedValues;
        if(allowedValues != null){
            inputType = VALUE_FROM_LIST;
        }
    }
    

    /**
     * @return the defaultValue
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return the validationRules
     */
    public List<ValidationRule> getValidationRules() {
        return validationRules;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @param validationRules the validationRules to set
     */
    public void setValidationRules(List<ValidationRule> validationRules) {
        this.validationRules = validationRules;
    }

    
    /**
     * @return the inputType
     */
    public String getInputType() {
        return inputType;
    }
    

    /**
     * @param inputType the inputType to set
     */
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
    

    /**
     * @return the innerName
     */
    public String getInnerName() {
        return innerName;
    }

    /**
     * @param innerName the innerName to set
     */
    public void setInnerName(String innerName) {
        this.innerName = innerName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((allowedValues == null) ? 0 : allowedValues.hashCode());
	result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
	result = prime * result + ((innerName == null) ? 0 : innerName.hashCode());
	result = prime * result + ((inputType == null) ? 0 : inputType.hashCode());
	result = prime * result + (mandatory ? 1231 : 1237);
	result = prime * result + ((validationRules == null) ? 0 : validationRules.hashCode());
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
	UserInputParameter other = (UserInputParameter) obj;
	if (allowedValues == null) {
	    if (other.allowedValues != null)
		return false;
	} else if (!allowedValues.equals(other.allowedValues))
	    return false;
	if (defaultValue == null) {
	    if (other.defaultValue != null)
		return false;
	} else if (!defaultValue.equals(other.defaultValue))
	    return false;
	if (innerName == null) {
	    if (other.innerName != null)
		return false;
	} else if (!innerName.equals(other.innerName))
	    return false;
	if (inputType == null) {
	    if (other.inputType != null)
		return false;
	} else if (!inputType.equals(other.inputType))
	    return false;
	if (mandatory != other.mandatory)
	    return false;
	if (validationRules == null) {
	    if (other.validationRules != null)
		return false;
	} else if (!validationRules.equals(other.validationRules))
	    return false;
	return true;
    }

}
