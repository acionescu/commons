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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class SimpleSocketWorker extends SocketWorker {

    public SimpleSocketWorker(Socket socket) {
	super(socket);
    }

    @Override
    public void work(Socket socket) throws Exception {
	while (true) {
	    try {
//		System.out.println("reading input:");
		String input = read(socket.getInputStream());
		if(input == null) {/* probably the other party quit without warning */
		    close(socket);
		    break;
		}
//		System.out.println(input);
//		System.out.println("Processing...");
		String output = process(input);
//		System.out.println("sending output: " + output);
		write(socket.getOutputStream(), output);
//		System.out.println("finished");
	    } catch (Exception e) {
		close(socket);
		break;
	    }
	}
    }

    private void close(Socket s) throws IOException {
	if(s.isConnected()) {
	    s.close();
	}
    }
    
    private String read(InputStream is) throws IOException {
	// byte[] buffer = new byte[4096];
	// int numRead;
	// long numWritten = 0;
	// StringBuilder out = new StringBuilder();
	// while ((numRead = is.read(buffer)) != -1) {
	// String read = new String(buffer, 0, numRead);
	// int indexOfTerminatorString = read.indexOf(terminatorString);
	// if (indexOfTerminatorString >= 0) {
	// out.append(read.subSequence(0, indexOfTerminatorString + 1));
	// break;
	// } else {
	// out.append(read);
	// numWritten += numRead;
	// }
	// }
	// return out.toString();
	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	return reader.readLine();
    }

    private void write(OutputStream os, String output) throws IOException {
	// os.write(output.getBytes());
	// os.flush();
	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
	writer.write(output);
	writer.newLine();
	writer.flush();
    }

    public abstract String process(String input) throws Exception;

}
