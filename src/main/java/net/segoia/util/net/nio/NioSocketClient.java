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
package net.segoia.util.net.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NioSocketClient {
    private boolean connected;
    private NioClient client;
    
    public synchronized void connect(String address, int port) throws UnknownHostException, IOException {
	if(!connected) {
	    client = new NioClient(InetAddress.getByName(address), port);
	    Thread t = new Thread(client);
	    t.setDaemon(true);
	    t.start();
	    connected=true;
	}
    }
    
    public byte[] sendAndReceive(byte[] data) throws IOException {
	RspHandler handler = new RspHandler();
	client.send(data, handler);
	return handler.getResponse();
    }
    
    public String sendAndReceive(String data) throws IOException {
	return new String(sendAndReceive(data.getBytes()));
    }
    
}
