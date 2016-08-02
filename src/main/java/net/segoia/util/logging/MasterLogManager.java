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
package net.segoia.util.logging;

public class MasterLogManager{
    private static LogManager defaultLogManager = new DefaultLogManager(new Log4jLoggerFactory(),null);
    

    public static Logger getLogger(Class<?> clazz) {
	return defaultLogManager.getLogger(clazz);
    }

    public static Logger getLogger(String name) {
	return defaultLogManager.getLogger(name);
    }
    
    public static Logger getLogger(String name, LoggerFactory loggerFactory) {
	return defaultLogManager.getLogger(name,loggerFactory);
    }

    public static LogManager getDefaultLogManager() {
        return defaultLogManager;
    }

    public static void setDefaultLogManager(LogManager defaultLogManager) {
        MasterLogManager.defaultLogManager = defaultLogManager;
    }

    public static void main(String[] args) throws Exception{
	Logger log = MasterLogManager.getLogger("Blabla");
	log.debug("debug");
	log.info("info");
	log.warn("warn");
	log.error("error");
	log.fatal("fatal");
	log.error("error thrown",new Exception("hop exception"));
	System.out.println(Long.MAX_VALUE);
    }
}
