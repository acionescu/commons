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
package net.segoia.util.io;

import java.net.Socket;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionHandler;

public abstract class SocketWorker implements Runnable{
    private Socket socket;
    private ExceptionHandler<?> exceptionHandler;
    
    public SocketWorker(Socket socket){
	this.socket = socket;
    }
    
    public void run() {
	try {
	    work(socket);
	} catch (Exception e) {
	    if(exceptionHandler != null){
		try {
		    exceptionHandler.handle(new ContextAwareException(e));
		} catch (Exception e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    }
	    else{
		e.printStackTrace();
	    }
	}
    }
    
    public abstract void work(Socket socket) throws Exception;

    public ExceptionHandler<?> getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler<?> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
    
}
