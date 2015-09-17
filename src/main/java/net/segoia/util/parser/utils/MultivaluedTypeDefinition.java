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

public class MultivaluedTypeDefinition<M, N> {

    private Class<M> multivaluedType;
    private Class<N> nestedType;

    public MultivaluedTypeDefinition() {
	super();
    }
    

    public MultivaluedTypeDefinition(Class<M> multivaluedType, Class<N> nestedType) {
	super();
	this.multivaluedType = multivaluedType;
	this.nestedType = nestedType;
    }

    /**
     * @return the nestedType
     */
    public Class<N> getNestedType() {
	return nestedType;
    }

    /**
     * @return the multivaluedType
     */
    public Class<M> getMultivaluedType() {
	return multivaluedType;
    }

    /**
     * @param nestedType
     *            the nestedType to set
     */
    public void setNestedType(Class<N> nestedType) {
	this.nestedType = nestedType;
    }

    /**
     * @param multivaluedType
     *            the multivaluedType to set
     */
    public void setMultivaluedType(Class<M> multivaluedType) {
	this.multivaluedType = multivaluedType;
    }
}
