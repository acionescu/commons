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
package net.segoia.util.parser.utils;

public class MapTypeDefinition<M, K, V> extends MultivaluedTypeDefinition<M, V> {
    private Class<K> keyType;

    public MapTypeDefinition() {
	super();
	// TODO Auto-generated constructor stub
    }

    public MapTypeDefinition(Class<M> multivaluedType, Class<K> keyType, Class<V> nestedType) {
	super(multivaluedType, nestedType);
	this.keyType = keyType;
    }

    /**
     * @return the keyType
     */
    public Class<K> getKeyType() {
        return keyType;
    }

    /**
     * @param keyType the keyType to set
     */
    public void setKeyType(Class<K> keyType) {
        this.keyType = keyType;
    }

    
    
}
