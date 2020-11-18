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

public abstract class AbstractLogger implements Logger {
    private boolean enabled = true;
    private LoggingLevel logLevel = LoggingLevel.INFO;

    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.util.logging.Logger#isEnabled()
     */
    @Override
    public boolean isEnabled() {
	return enabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.util.logging.Logger#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
	return (logLevel.getLevel() <= LoggingLevel.DEBUG.getLevel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.util.logging.Logger#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
	return (logLevel.getLevel() <= LoggingLevel.INFO.getLevel());
    }

    public boolean isLogLevelAllowed(LoggingLevel ll) {
	return logLevel.getLevel() <= ll.getLevel();
    }

    /**
     * @return the logLevel
     */
    @Override
    public LoggingLevel getLogLevel() {
	return logLevel;
    }

    /**
     * @param enabled
     *            the enabled to set
     */
    @Override
    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    /**
     * @param logLevel
     *            the logLevel to set
     */
    @Override
    public void setLogLevel(LoggingLevel logLevel) {
	this.logLevel = logLevel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.util.logging.Logger#trace(net.segoia.util.logging.LoggingLevel, java.lang.Object,
     * java.lang.Throwable)
     */
    @Override
    public boolean trace(LoggingLevel ll, Object message, Throwable t) {
	if (!isLogLevelAllowed(ll)) {
	    return false;
	}

	append(ll, message, t);
	return true;
    }

    abstract void append(LoggingLevel ll, Object message, Throwable t);

}
