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
package net.segoia.util.data.translation;

import java.util.Map;

public class DataTranslation {
    private String sourceType;
    private String destinationType;
    private Map<String, EntityMapping> dataMappings;
    /* the generator used to generate unique ids for the passed values */
    private UniqueIdGenerator<Object> idGenerator;

    /**
     * @return the sourceType
     */
    public String getSourceType() {
	return sourceType;
    }

    /**
     * @return the destinationType
     */
    public String getDestinationType() {
	return destinationType;
    }

    /**
     * @param sourceType
     *            the sourceType to set
     */
    public void setSourceType(String sourceType) {
	this.sourceType = sourceType;
    }

    /**
     * @param destinationType
     *            the destinationType to set
     */
    public void setDestinationType(String destinationType) {
	this.destinationType = destinationType;
    }

    /**
     * @return the dataMappings
     */
    public Map<String, EntityMapping> getDataMappings() {
        return dataMappings;
    }

    /**
     * @param dataMappings the dataMappings to set
     */
    public void setDataMappings(Map<String, EntityMapping> dataMappings) {
        this.dataMappings = dataMappings;
    }

    /**
     * @return the idGenerator
     */
    public UniqueIdGenerator<Object> getIdGenerator() {
        return idGenerator;
    }

    /**
     * @param idGenerator the idGenerator to set
     */
    public void setIdGenerator(UniqueIdGenerator<Object> idGenerator) {
        this.idGenerator = idGenerator;
    }
    
}
