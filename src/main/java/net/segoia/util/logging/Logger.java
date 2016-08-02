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

public interface Logger {
    
    void debug(Object message, Throwable t);
    void info(Object message, Throwable t);
    void warn(Object message, Throwable t);
    void error(Object message, Throwable t);
    void fatal(Object message, Throwable t);
    
    void debug(Object message);
    void info(Object message);
    void warn(Object message);
    void error(Object message);
    void fatal(Object message);
    
    void append(Object message);
    
    boolean isEnabled();
    boolean isDebugEnabled();
    boolean isInfoEnabled();
    boolean trace(LoggingLevel ll, Object message, Throwable t);
    void setLogLevel(LoggingLevel logLevel);
    void setEnabled(boolean enabled);
    LoggingLevel getLogLevel();
    
}
