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

public class LoggingUtil {
    
    public static void printThrowable(Throwable t, StringBuffer out){
	StackTraceElement[] trace = t.getStackTrace();
	out.append("\n").append(t);
	for (int i=0; i < trace.length; i++)
            out.append("\n\tat " + trace[i]);

        Throwable cause = t.getCause();
        if (cause != null){
            getThrowableStringAsCause(cause, trace,out);
        }
    }
    
    public static String getThrowableAsString(Throwable t){
	StringBuffer out = new StringBuffer(256);
	printThrowable(t, out);
        return out.toString();
    }
    
    public static void getThrowableStringAsCause(Throwable t, StackTraceElement[] causedTrace, StringBuffer out){
	   StackTraceElement[] trace = t.getStackTrace();
	        int m = trace.length-1, n = causedTrace.length-1;
	        while (m >= 0 && n >=0 && trace[m].equals(causedTrace[n])) {
	            m--; n--;
	        }
	        int framesInCommon = trace.length - 1 - m;

	        out.append("\nCaused by: " + t);
	        for (int i=0; i <= m; i++)
	            out.append("\n\tat " + trace[i]);
	        if (framesInCommon != 0)
	            out.append("\n\t... " + framesInCommon + " more");

	        // Recurse if we have a cause
	        Throwable ourCause = t.getCause();
	        if (ourCause != null)
	            getThrowableStringAsCause(ourCause, trace, out);
    }
}
