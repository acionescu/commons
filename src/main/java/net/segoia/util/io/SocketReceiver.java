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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.segoia.util.logging.Logger;
import net.segoia.util.logging.MasterLogManager;

public class SocketReceiver extends AbstractReceiver {
    private static Logger logger = MasterLogManager.getLogger(SocketReceiver.class.getName());

    private int listenPort;
    private SocketProcessor processor;
    private boolean running = false;
    private ServerSocket serverSocket;

    public SocketReceiver() {

    }

    public SocketReceiver(int port) {
	listenPort = port;
    }

    public void listen() throws Exception {
	if (!running) {
	    try {
		serverSocket = new ServerSocket(listenPort);
	    } catch (IOException e) {
		throw new IOException("Failed to start " + getName() + " on port " + getListenPort(), e);
	    }
	    running = true;
	    logger.info("Started " + getName() + " on port " + getListenPort());
	    while (running) {
		Socket client = serverSocket.accept();
		processor.process(client);
	    }
	} else {
	    throw new RuntimeException("Socket receiver already running on port " + listenPort);
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

    public void shutdown() throws IOException {
	if (running) {
	    serverSocket.close();
	    running = false;
	}

    }
}
