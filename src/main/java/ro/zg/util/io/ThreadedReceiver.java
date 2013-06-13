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
package ro.zg.util.io;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionHandler;

public class ThreadedReceiver extends Thread{
    private Receiver receiver;
    private ExceptionHandler<?> exceptionHandler;
    
    public ThreadedReceiver(Receiver receiver){
	this.receiver = receiver;
    }

    public void run(){
	try {
	    receiver.listen();
	} catch (Exception e) {
	    if(exceptionHandler != null){
		try {
		    exceptionHandler.handle(new ContextAwareException(e));
		} catch (Exception e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    }
	    e.printStackTrace();
	}
    }
}