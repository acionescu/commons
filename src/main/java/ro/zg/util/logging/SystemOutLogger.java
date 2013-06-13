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

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemOutLogger implements Logger {
    private String name;
    private PrintStream out = System.out;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 

    public SystemOutLogger(String name) {
	this.name = name;
    }

    public void debug(Object message, Throwable t) {
	trace(LoggingLevel.DEBUG,message,t);
    }

    public void debug(Object message) {
	trace(LoggingLevel.DEBUG,message,null);
    }

    public void error(Object message, Throwable t) {
	trace(LoggingLevel.ERROR,message,t);
    }

    public void error(Object message) {
	trace(LoggingLevel.ERROR,message,null);
    }

    public void fatal(Object message, Throwable t) {
	trace(LoggingLevel.FATAL,message,t);
    }

    public void fatal(Object message) {
	trace(LoggingLevel.FATAL,message,null);
    }

    public void info(Object message, Throwable t) {
	trace(LoggingLevel.INFO,message,t);
    }

    public void info(Object message) {
	trace(LoggingLevel.INFO,message,null);
    }

    public boolean isDebugEnabled() {
	// TODO Auto-generated method stub
	return false;
    }

    public boolean isEnabled() {
	// TODO Auto-generated method stub
	return false;
    }

    public boolean isInfoEnabled() {
	// TODO Auto-generated method stub
	return false;
    }

    public void warn(Object message, Throwable t) {
	trace(LoggingLevel.WARN,message,t);    }

    public void warn(Object message) {
	trace(LoggingLevel.WARN,message,null);
    }
    
    public void append(Object message){
	out.println(message);
    }

    private void flush(String output) {
	out.println(output);
    }

    private void trace(LoggingLevel ll, Object message, Throwable t) {
	StringBuffer sb = new StringBuffer(256);
	sb.append(name);
	sb.append("[").append(ll.name()).append("]");
	printDate(sb);
	sb.append(":").append(message);
	if (t != null) {
	    sb.append(" - ");
	    LoggingUtil.printThrowable(t, sb);
	}
	flush(sb.toString());
    }
    
    private void printDate(StringBuffer sb) {
	sb.append("[").append(dateFormat.format(new Date())).append("]");
    }
}
