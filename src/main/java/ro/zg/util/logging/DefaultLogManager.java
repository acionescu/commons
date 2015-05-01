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
package ro.zg.util.logging;

import java.util.Hashtable;
import java.util.Map;

public class DefaultLogManager implements LogManager{
    private LoggerFactory defaultLoggerFactory = new SystemOutLoggerFactory();
    private Logger defaultLogger = new SystemOutLogger("DefaultLogger");
    private Map<String,Logger> loggers = new Hashtable<String, Logger>();
    

    public DefaultLogManager(LoggerFactory defaultLoggerFactory, Logger defaultLogger) {
	super();
	this.defaultLoggerFactory = defaultLoggerFactory;
	this.defaultLogger = defaultLogger;
    }

    public DefaultLogManager() {
	super();
	// TODO Auto-generated constructor stub
    }

    public synchronized Logger getLogger(String name) {
	Logger logger = loggers.get(name);
	if(logger == null){
	    if(defaultLoggerFactory == null){
		return defaultLogger;
	    }
	    logger = defaultLoggerFactory.getLogger(name);
	    loggers.put(name, logger);
	}
	return logger;
    }

    public Logger getLogger(Class<?> clazz) {
	return getLogger(clazz.getName());
    }

    public LoggerFactory getDefaultLoggerFactory() {
        return defaultLoggerFactory;
    }

    public Logger getDefaultLogger() {
        return defaultLogger;
    }

    public void setDefaultLoggerFactory(LoggerFactory defaultLoggerFactory) {
        this.defaultLoggerFactory = defaultLoggerFactory;
    }
}
