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

public class Log4jLogger implements Logger{
    private org.apache.log4j.Logger log4jLogger;

    public Log4jLogger(org.apache.log4j.Logger log4jLogger) {
	super();
	this.log4jLogger = log4jLogger;
    }

    @Override
    public void debug(Object message, Throwable t) {
	log4jLogger.debug(message, t);
    }

    @Override
    public void info(Object message, Throwable t) {
	log4jLogger.info(message, t);
	
    }

    @Override
    public void warn(Object message, Throwable t) {
	log4jLogger.warn(message, t);
	
    }

    @Override
    public void error(Object message, Throwable t) {
	log4jLogger.error(message, t);
	
    }

    @Override
    public void fatal(Object message, Throwable t) {
	log4jLogger.fatal(message, t);
    }

    @Override
    public void debug(Object message) {
	log4jLogger.debug(message);
    }

    @Override
    public void info(Object message) {
	log4jLogger.info(message);
    }

    @Override
    public void warn(Object message) {
	log4jLogger.warn(message);
    }

    @Override
    public void error(Object message) {
	log4jLogger.error(message);
    }

    @Override
    public void fatal(Object message) {
	log4jLogger.fatal(message);
    }

    @Override
    public void append(Object message) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled() {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDebugEnabled() {
	return log4jLogger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
	return log4jLogger.isInfoEnabled();
    }

}
