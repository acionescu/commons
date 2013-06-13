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
package ro.zg.commons.exceptions;

import java.util.List;

import ro.zg.util.data.NameValue;

public class ContextAwareException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 9139117229862930474L;

    public static int INFO = 1;
    public static int WARNING = 2;
    public static int ERROR = 3;

    private String type;

    protected ExceptionContext exceptionContext;

    private String message;

    private int level = ERROR;

    public ContextAwareException(String type, Throwable cause, ExceptionContext exceptionContext) {
	super(cause);
	this.type = type;
	this.exceptionContext = exceptionContext;
    }

    // private void setCauseString(Throwable t) {
    // StringBuffer bf = new StringBuffer(256);
    // LoggingUtil.printThrowable(t, bf);
    // causeString = bf.toString();
    // }

    public ContextAwareException(String type, ExceptionContext exceptionContext) {
	this.type = type;
	this.exceptionContext = exceptionContext;
    }

    public ContextAwareException(String type) {
	this.type = type;
	this.exceptionContext = new ExceptionContext();
    }
    
    public ContextAwareException(String type, int level) {
	this.type = type;
	this.level= level;
	this.exceptionContext = new ExceptionContext();
    }

    public ContextAwareException(String type, String message) {
	this(type);
	this.message = message;
    }

    public ContextAwareException(Throwable cause) {
	// setCauseString(cause);
	super(cause);
	this.type = "UNKNOWN_ERROR";
    }

    public ContextAwareException(String type, Throwable cause) {
	// setCauseString(cause);
	super(cause);
	this.type = type;
    }

    public String getMessage() {
	if (message == null) {
	    message = createMessage();
	    if (message == null) {
		message = defaulErrorMessage();
	    }
	}
	return message;
    }

    protected String createMessage() {
	return defaulErrorMessage();
    }

    private String defaulErrorMessage() {
	StringBuffer sb = new StringBuffer(type);
	sb.append(getDescription());
	return sb.toString();
    }

    private String getDescription() {
	if (exceptionContext != null) {
	    List<NameValue<Object>> params = exceptionContext.getParametersAsList();
	    if (params != null) {
		StringBuffer sb = new StringBuffer(128);
		sb.append("\nError Context: ");
		sb.append(params.toString());
		return sb.toString();
	    }
	}
	return "";
    }

    public String getType() {
	return type;
    }

    public ExceptionContext getExceptionContext() {
	return exceptionContext;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    
    
    // public void printStackTrace(PrintWriter s) {
    // synchronized (s) {
    // s.println(this);
    //
    // if (causeString != null)
    // s.println("Caused by:\n" + causeString);
    // }
    // }
    //
    // public void printStackTrace(PrintStream s) {
    // synchronized (s) {
    // s.println(this);
    //
    // if (causeString != null)
    // s.println("Caused by:\n" + causeString);
    // }
    // }
}
