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
package ro.zg.util.persistence;

public enum PersistentValueType {
    STRING(0), LONG(1), DOUBLE(2), BYTE_ARRAY(3), VALUES_SET(4);

    private long id;

    private PersistentValueType(long id) {
	this.id = id;
    }

    /**
     * @return the id
     */
    public long getId() {
	return id;
    }

}
