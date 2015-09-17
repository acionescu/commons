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
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Is using the value of another parameter to deduce the value of this parameter
 * @author adi
 *
 */
public class DynamicConfigParameter implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 6181101147833256596L;
    /**
     * name of this parameter
     */
    private String name;
    /**
     * The name of the parameter used to deduce the value of this parameter
     */
    private String sourceParamName;
    /**
     * key - the value of the source param
     * </br>
     * value - the value of this parameter
     */
    private Map<String,GenericNameValue> mappedValues = new LinkedHashMap<String, GenericNameValue>();
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the sourceParamName
     */
    public String getSourceParamName() {
        return sourceParamName;
    }
    /**
     * @return the mappedValues
     */
    public Map<String, GenericNameValue> getMappedValues() {
        return mappedValues;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @param sourceParamName the sourceParamName to set
     */
    public void setSourceParamName(String sourceParamName) {
        this.sourceParamName = sourceParamName;
    }
    /**
     * @param mappedValues the mappedValues to set
     */
    public void setMappedValues(Map<String, GenericNameValue> mappedValues) {
        this.mappedValues = mappedValues;
    }
    
    
}
