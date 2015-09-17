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
package net.segoia.util.persistence;

import java.util.Map;

public class PersistentValuesSet extends PersistentEntity{
    private Map<String, KeyNameType> nameTypeKeysMap;

    /**
     * @return the nameTypeKeysMap
     */
    public Map<String, KeyNameType> getNameTypeKeysMap() {
        return nameTypeKeysMap;
    }

    /**
     * @param nameTypeKeysMap the nameTypeKeysMap to set
     */
    public void setNameTypeKeysMap(Map<String, KeyNameType> nameTypeKeysMap) {
        this.nameTypeKeysMap = nameTypeKeysMap;
    }
    
    
}
