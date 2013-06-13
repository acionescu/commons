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

import java.net.ServerSocket;
import java.net.Socket;

public class SocketReceiver implements Receiver{
    private int listenPort;
    private SocketProcessor processor;
    private boolean running = false;

    public SocketReceiver() {

    }

    public SocketReceiver(int port) {
	listenPort = port;
    }

    public void listen() throws Exception {
	ServerSocket serverSocket = new ServerSocket(listenPort);
	running = true;
	System.out.println("Start listening on port "+listenPort);
	while (running) {
	    Socket client = serverSocket.accept();
	    processor.process(client);
	}
    }

    public int getListenPort() {
	return listenPort;
    }

    public SocketProcessor getProcessor() {
	return processor;
    }

    public void setListenPort(int listenPort) {
	this.listenPort = listenPort;
    }

    public void setProcessor(SocketProcessor processor) {
	this.processor = processor;
    }

    public void shutdown() {
	// TODO Auto-generated method stub
	
    }
}
