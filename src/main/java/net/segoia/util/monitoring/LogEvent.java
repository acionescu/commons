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
package net.segoia.util.monitoring;

import net.segoia.util.data.GenericNameValueContext;

/**
 * The data to be logged
 * @author adi
 *
 */
public class LogEvent extends GenericNameValueContext{
    /**
     * 
     */
    private static final long serialVersionUID = -6312297505514293583L;
    /**
     * the identification key for this log event.
     * This will be used to categorize this logging event
     */
    private String loggingKey;
    
    
    public String toString(){
	return loggingKey+":"+super.toString();
    }
    
    
    /**
     * @return the loggingKey
     */
    public String getLoggingKey() {
        return loggingKey;
    }
    /**
     * @param loggingKey the loggingKey to set
     */
    public void setLoggingKey(String loggingKey) {
        this.loggingKey = loggingKey;
    }
    
}
