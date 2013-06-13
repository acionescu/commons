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
package ro.zg.util.persistence.cache;

import ro.zg.util.persistence.DataExchangeHandler;

public class CacheAgent implements DataExchangeHandler{
    private DataExchangeHandler psersistenceHandler;

    public Object getValue(String key) {
	// TODO Auto-generated method stub
	return null;
    }

    public String writeValue(Object value) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * @return the psersistenceHandler
     */
    public DataExchangeHandler getPsersistenceHandler() {
        return psersistenceHandler;
    }

    /**
     * @param psersistenceHandler the psersistenceHandler to set
     */
    public void setPsersistenceHandler(DataExchangeHandler psersistenceHandler) {
        this.psersistenceHandler = psersistenceHandler;
    }
}
