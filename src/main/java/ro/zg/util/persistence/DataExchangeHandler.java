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

public interface DataExchangeHandler {
    /**
     * Stores a value in the cache and returns the associated key 
     * by which this value can be retrieved afterwords
     * @param value
     * @return
     */
    String writeValue(Object value);
    
    /**
     * Returns the value associated with the specified key
     * null if none exists
     * @param key
     * @return
     */
    Object getValue(String key);
}
