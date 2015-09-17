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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurationData implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 8130503542347343421L;
    private Map<String, UserInputParameter> userInputParams = new LinkedHashMap<String, UserInputParameter>();
    private Map<String, GenericNameValue> staticConfigParams = new LinkedHashMap<String, GenericNameValue>();
    private Map<String, DynamicConfigParameter> dynamicParams = new LinkedHashMap<String, DynamicConfigParameter>();
    private String templateId;
    /**
     * @return the userInputParams
     */
    public Map<String, UserInputParameter> getUserInputParams() {
	return userInputParams;
    }

    /**
     * @return the staticConfigParams
     */
    public Map<String, GenericNameValue> getStaticConfigParams() {
	return staticConfigParams;
    }

    /**
     * @return the dynamicParams
     */
    public Map<String, DynamicConfigParameter> getDynamicParams() {
	return dynamicParams;
    }

    /**
     * @param userInputParams
     *            the userInputParams to set
     */
    public void setUserInputParams(Map<String, UserInputParameter> userInputParams) {
	this.userInputParams = userInputParams;
    }

    /**
     * @param staticConfigParams
     *            the staticConfigParams to set
     */
    public void setStaticConfigParams(Map<String, GenericNameValue> staticConfigParams) {
	this.staticConfigParams = staticConfigParams;
    }

    /**
     * @param dynamicParams
     *            the dynamicParams to set
     */
    public void setDynamicParams(Map<String, DynamicConfigParameter> dynamicParams) {
	this.dynamicParams = dynamicParams;
    }

    public Object getParameterValue(String name) {
	if (userInputParams.containsKey(name)) {
	    return userInputParams.get(name).getValue();
	} else if (staticConfigParams.containsKey(name)) {
	    return staticConfigParams.get(name).getValue();
	} else if (dynamicParams.containsKey(name)) {
	    DynamicConfigParameter dcp = dynamicParams.get(name);
	    return getDynamicParamValue(dcp);
	}
	return null;
    }

    private Object getDynamicParamValue(DynamicConfigParameter dcp) {
	String sourceName = dcp.getSourceParamName();
	if (userInputParams.containsKey(sourceName)) {
	    return dcp.getMappedValues().get(userInputParams.get(sourceName).getValue());
	}
	return null;
    }

    public Map<String,Object> getValuesMap(){
	Map<String,Object> valuesMap = new HashMap<String, Object>();
	for(UserInputParameter uip : userInputParams.values()) {
	    valuesMap.put(uip.getName(), uip.getValue());
	}
	for(DynamicConfigParameter p : dynamicParams.values()) {
	    valuesMap.put(p.getName(), getDynamicParamValue(p));
	}
	for(GenericNameValue p : staticConfigParams.values()) {
	    valuesMap.put(p.getName(), p.getValue());
	}
	return valuesMap;
    }

    public void addUserIntpuParam(UserInputParameter uip) {
	userInputParams.put(uip.getName(), uip);
    }

    public UserInputParameter getUserInputParam(String name) {
	return userInputParams.get(name);
    }
   
    
    /**
     * @return the templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId the templateId to set
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((dynamicParams == null) ? 0 : dynamicParams.hashCode());
	result = prime * result + ((staticConfigParams == null) ? 0 : staticConfigParams.hashCode());
	result = prime * result + ((templateId == null) ? 0 : templateId.hashCode());
	result = prime * result + ((userInputParams == null) ? 0 : userInputParams.hashCode());
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ConfigurationData other = (ConfigurationData) obj;
	if (dynamicParams == null) {
	    if (other.dynamicParams != null)
		return false;
	} else if (!dynamicParams.equals(other.dynamicParams))
	    return false;
	if (staticConfigParams == null) {
	    if (other.staticConfigParams != null)
		return false;
	} else if (!staticConfigParams.equals(other.staticConfigParams))
	    return false;
	if (templateId == null) {
	    if (other.templateId != null)
		return false;
	} else if (!templateId.equals(other.templateId))
	    return false;
	if (userInputParams == null) {
	    if (other.userInputParams != null)
		return false;
	} else if (!userInputParams.equals(other.userInputParams))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "ConfigurationData [userInputParams=" + userInputParams
		+ ", staticConfigParams=" + staticConfigParams
		+ ", dynamicParams=" + dynamicParams + "]";
    }

    
}
