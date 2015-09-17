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

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaLogger {
    public static void main(String[] args) {
	Logger logger = Logger.getLogger("test.test");
	logger.setLevel(Level.INFO);
	
	ConsoleHandler ch = new ConsoleHandler();
	ch.setLevel(Level.INFO);

	logger.addHandler(ch);
	logger.setUseParentHandlers(false);
	while (true) {
	    logger.info("blabla");
//	    logger.log(Level.FINE, "fine");
//	    logger.log(Level.FINER, "finer");
//	    logger.log(Level.FINEST, "finest");
//	    logger.log(Level.WARNING, "warn");
	    	    
	 
	}
    }
}
