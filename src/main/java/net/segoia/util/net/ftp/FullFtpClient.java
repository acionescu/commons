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
package net.segoia.util.net.ftp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;



public class FullFtpClient extends FTPClient {
    
    public FullFtpClient() {
	
    }
    
//    public FullFtpClient(String host) throws IOException {
////	super(host);
//	this.connect(host);
//    }
    
    
    public void openServer(String host) throws SocketException, IOException {
	super.connect(host);
    }
    
    public void closeServer() throws IOException {
	super.disconnect();
    }
    
    public OutputStream put(String fileName) throws IOException {
	return super.storeFileStream(fileName);
    }
    
    public boolean serverIsOpen() {
	return super.isConnected();
    }

    public int mkDir(String remoteDirectory) throws IOException {
	if (remoteDirectory == null || "".equals(remoteDirectory))
	    return -1;
//	return issueCommand("MKD "+remoteDirectory);
	return super.mkd(remoteDirectory);
    }
    
    public int cd(String remoteDirectory) throws IOException{
	if (remoteDirectory == null || "".equals(remoteDirectory))
	    return -1;
	return super.cwd(remoteDirectory);
    }
    
    public List<String> getFilesList() throws IOException {
	List<String> files = new ArrayList<String>();
	for(FTPFile ff : super.listFiles()) {
	    files.add(ff.getName());
	}
	return files;
    }
    
    public int rmDir(String remoteDirectory) throws IOException {
	if (remoteDirectory == null || "".equals(remoteDirectory))
	    return -1;
//	return issueCommand("RMD "+remoteDirectory);
	return super.rmd(remoteDirectory);
    }
    
    public String getResponseString() {
	return super.getReplyString();
    }
    
    public int delete(String remoteFile) throws IOException {
	if (remoteFile == null || "".equals(remoteFile))
	    return -1;
//	return issueCommand("DELE "+remoteFile);
	return super.dele(remoteFile);
    }
}
