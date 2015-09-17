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
package net.segoia.log;

import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

public class Log {
	
	public static void debug(Class clazz,Object o,Throwable t) {
		Logger.getLogger(clazz).debug(o,t);
	}
	
	public static void info(Class clazz,Object o,Throwable t) {
		Logger.getLogger(clazz).info(o,t);
	}
	
	public static void warn(Class clazz,Object o,Throwable t) {
		Logger.getLogger(clazz).warn(o,t);
	}
	
	public static void error(Class clazz,Object o,Throwable t) {
		Logger.getLogger(clazz).error(o,t);
	}
	
	public static void fatal(Class clazz,Object o,Throwable t) {
		Logger.getLogger(clazz).fatal(o,t);
	}
	// without Throwable
	
	public static void debug(Class clazz,Object o) {
		Logger.getLogger(clazz).debug(o);
	}
	
	public static void info(Class clazz,Object o) {
		Logger.getLogger(clazz).info(o);
	}
	
	public static void warn(Class clazz,Object o) {
		Logger.getLogger(clazz).warn(o);
	}
	
	public static void error(Class clazz,Object o) {
		Logger.getLogger(clazz).error(o);
	}
	
	public static void fatal(Class clazz,Object o) {
		Logger.getLogger(clazz).fatal(o);
	}
	
	//with objects
	
	public static void debug(Object instance,Object o,Throwable t) {
		Logger.getLogger(instance.getClass()).debug(o,t);
	}
	
	public static void info(Object instance,Object o,Throwable t) {
		Logger.getLogger(instance.getClass()).info(o,t);
	}
	
	public static void warn(Object instance,Object o,Throwable t) {
		Logger.getLogger(instance.getClass()).warn(o,t);
	}
	
	public static void error(Object instance,Object o,Throwable t) {
		Logger.getLogger(instance.getClass()).error(o,t);
	}
	
	public static void fatal(Object instance,Object o,Throwable t) {
		Logger.getLogger(instance.getClass()).fatal(o,t);
	}
	// without Throwable
	
	public static void debug(Object instance,Object o) {
		Logger log = Logger.getLogger(instance.getClass());
		if(log.isDebugEnabled()){
			log.debug(o);
		}
		
	}
	
	public static void info(Object instance,Object o) {
		Logger.getLogger(instance.getClass()).info(o);
	}
	
	public static void warn(Object instance,Object o) {
		Logger.getLogger(instance.getClass()).warn(o);
	}
	
	public static void error(Object instance,Object o) {
		Logger.getLogger(instance.getClass()).error(o);
	}
	
	public static void fatal(Object instance,Object o) {
		Logger.getLogger(instance.getClass()).fatal(o);
	}
	
	public static void main(String[] args){
	    Logger logger = Logger.getLogger("blabla");
	    ConsoleAppender ca = new ConsoleAppender(new SimpleLayout());
	    ca.setName("cons");
	    logger.addAppender(ca);
	    
	    while(true){
	    logger.info("test "+new Date());
	    }
	}
}
