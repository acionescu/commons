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
package ro.zg.util.resources;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.util.net.ftp.FullFtpClient;

public class ResourcesUtil {

    public static List<String> listFiles(URL url) throws Exception {
	String protocol = url.getProtocol();
	if (protocol.equals("file")) {
	    return listSystemFiles(url);
	} else if (protocol.equals("ftp")) {
	    return listFtpFiles(url);
	}
	throw new UnsupportedOperationException("unknown protocol " + protocol);
    }

    public static List<String> listSystemFiles(URL url) throws Exception {
	File f = new File(url.toURI());
	if (f.exists() && f.isDirectory()) {
	    return Arrays.asList(f.list());
	}
	return new ArrayList<String>();
    }

    public static List<String> listFtpFiles(URL url) throws Exception {
	List<String> response = new ArrayList<String>();
	FullFtpClient ftpClient = new FullFtpClient();
	try {
	    ftpClient.openServer(url.getHost());
	    String[] userInfo = url.getUserInfo().split(":");
	    ftpClient.login(userInfo[0], userInfo[1]);

	    String path = url.getPath();
	    if (path.startsWith("/")) {
		path = path.substring(1);
	    }

	    ftpClient.cd(path);
	    String cdResponse = ftpClient.getResponseString();
	    String responseCode = getFtpResponseCode(cdResponse);
	    if (!responseCode.equals("250")) {
		return null;
	    }
//	    BufferedReader reader = new BufferedReader(new InputStreamReader(ftpClient.list()));
//	    String line = null;
//	    while ((line = reader.readLine()) != null) {
//		int li = line.lastIndexOf(" ");
//		response.add(line.substring(li + 1));
//	    }
//	    reader.close();
	    
	    response.addAll(ftpClient.getFilesList());
	} finally {
	    if (ftpClient != null && ftpClient.serverIsOpen()) {
		ftpClient.closeServer();
	    }
	}
	return response;
    }

    private static String getFtpResponseCode(String response) {
	int i = response.indexOf(" ");
	if (i >= 0) {
	    return response.substring(0, i);
	}
	return null;
    }

    public static URL getURL(String baseDir, String path, ClassLoader loader, boolean create)
	    throws ContextAwareException {
	if (!baseDir.endsWith(File.separator)&& !baseDir.endsWith("/")) {
	    /* mark it as a directory */
	    baseDir += File.separator;
	}
	URL baseDirUrl = loader.getResource(baseDir);
	if (baseDirUrl == null) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("resourceName", baseDir);
	    throw new ContextAwareException("UNKNOWN_RESOURCE", ec);
	}
	if(path.startsWith(File.separator)) {
	    path = path.substring(1);
	}
	
	String fullPath = baseDir + path;
	URL pathUrl = loader.getResource(fullPath);
	if (pathUrl == null && create) {
	    
	    String urlString;
	    try {
		urlString = URLDecoder.decode(baseDirUrl.toString(),"UTF-8") + path;
	    } catch (UnsupportedEncodingException e1) {
		ExceptionContext ec = new ExceptionContext();
		ec.put("url", baseDirUrl);
		throw new ContextAwareException("INVALID_URL",e1,ec);
	    }
	    try { 
		pathUrl = new URL(urlString);
		createUrl(pathUrl);
	    } catch (MalformedURLException e) {
		ExceptionContext ec = new ExceptionContext();
		ec.put("url", urlString);
		throw new ContextAwareException("INVALID_URL", ec);
	    }
	}
	return pathUrl;
    }

    public static boolean createUrl(URL url) throws ContextAwareException {
	String protocol = url.getProtocol();
	try {
	    if (protocol.equals("file")) {
		return createFileUrl(url);
	    } else if (protocol.equals("ftp")) {
		return createFtpUrl(url);
	    }
	} catch (Exception e) {
	    
	    throw new ContextAwareException("ERROR_CREATING_RESOURCE", e);
	}
	return false;
    }

    public static boolean createFileUrl(URL url) throws IOException {
	String path = url.getPath();
	File f = new File(path);
	if (f.exists()) {
	    return true;
	}
	if (checkIsDirectory(path)) {/* it's a directory */
	    return f.mkdirs();
	} else { /* should be a file */
	    String fileName = path;
	    int parentIndex = getDirectoryEndIndex(path);
	    if (parentIndex >= 0) {
		String parentDir = path.substring(0, parentIndex);
		new File(parentDir).mkdirs();
		fileName = path.substring(parentIndex + 1);
	    }
	    return new File(fileName).createNewFile();
	}
    }

    public static boolean createFtpUrl(URL url) throws IOException {
	String path = url.getPath();
	int parentIndex = getDirectoryEndIndex(path);
	FullFtpClient ftpClient = new FullFtpClient();
	try {
	    ftpClient.openServer(url.getHost());
	    String userInfo[] = url.getUserInfo().split(":");
	    ftpClient.login(userInfo[0], userInfo[1]);
	    String fileName = path;
	    if (parentIndex >= 0) {
		String[] dirPath = path.substring(0, parentIndex).split(File.separator);
		fileName = path.substring(parentIndex + 1);
		for (String dir : dirPath) {
		    try {
			ftpClient.cd(dir);
		    } catch (Exception e) {
			/*
			 * probably the directory does not exist, we'll read the response code and decide what to do
			 * next
			 */
		    }
		    String resp = ftpClient.getResponseString();
		    String respCode = getFtpResponseCode(resp);
		    if (!respCode.equals("250")) {
			ftpClient.mkDir(dir);
			resp = ftpClient.getResponseString();
			respCode = getFtpResponseCode(resp);
			if (respCode.equals("257")) {/* dir created */
			    ftpClient.cd(dir);
			    resp = ftpClient.getResponseString();
			    respCode = getFtpResponseCode(resp);
			    if (!respCode.equals("250")) {/* try to change dir */
				throw new IOException("Failed to create dir " + dir);
			    }
			}
		    }
		}
	    }
	    if (!path.endsWith(File.separator)) {/* create the file if there is one */
		OutputStream os = ftpClient.put(fileName);
		/* put nothing */
		os.close();
	    }
	    ftpClient.closeServer();
	    return true;
	} finally {
	    if (ftpClient != null && ftpClient.serverIsOpen()) {
		ftpClient.closeServer();
	    }
	}
    }

    public static boolean removeResource(URL url) throws ContextAwareException {
	String protocol = url.getProtocol();
	try {
	    if (protocol.equals("file")) {
		return removeFileResource(url);
	    } else if (protocol.equals("ftp")) {
		return removeFtpResource(url);
	    }
	} catch (Exception e) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("resourceName",url.toString());
	    throw new ContextAwareException("FAILED_TO_REMOVE_RESOURCE",e,ec);
	}
	return false;
    }

    public static boolean removeFileResource(URL url) {
//	File f = new File(url.getPath());
	File f;
	try {
	    f = new File(url.toURI());
	} catch (URISyntaxException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return false;
	}
	if (!f.exists()) {
	    return true;
	}
	return f.delete();
    }

    public static boolean removeFtpResource(URL url) throws IOException {
	FullFtpClient ftpClient = new FullFtpClient();
	String path = url.getPath();
	if(path.startsWith(File.separator)) {
	    path = path.substring(1);
	}
	try {
	    ftpClient.openServer(url.getHost());
	    String userInfo[] = url.getUserInfo().split(":");
	    ftpClient.login(userInfo[0], userInfo[1]);
	   
	    if (path.endsWith(File.separator)) {/* it's a directory */
		ftpClient.rmDir(path);
	    } else { /* it's a file */
		ftpClient.delete(path);
	    }
	    String resp = ftpClient.getResponseString();
	    String respCode = getFtpResponseCode(resp);
	    return (respCode.equals("250"));
	} finally {
	    if (ftpClient != null && ftpClient.serverIsOpen()) {
		ftpClient.closeServer();
	    }
	}
    }
    
    public static boolean checkIsDirectory(String path){
	return path.endsWith(File.separator) || path.endsWith("/");
    }
    
    public static int getDirectoryEndIndex(String path){
	return Math.max(path.lastIndexOf(File.separator), path.lastIndexOf("/"));
    }

    public static void main(String[] args) {

    }
}
