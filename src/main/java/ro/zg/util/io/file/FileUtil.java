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
package ro.zg.util.io.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static void copyFile(File sourceFile, File destFile) throws IOException {
	if (sourceFile == null || destFile == null) {
	    throw new IllegalArgumentException("Source and destination files must be non null.");
	}
	File destParent = destFile.getParentFile();
	if (!destParent.exists()) {
	    destParent.mkdirs();
	}
	if (!destFile.exists()) {
	    destFile.createNewFile();
	}

	FileChannel source = null;
	FileChannel destination = null;
	try {
	    source = new FileInputStream(sourceFile).getChannel();
	    destination = new FileOutputStream(destFile).getChannel();
	    destination.transferFrom(source, 0, source.size());
	} finally {
	    if (source != null) {
		source.close();
	    }
	    if (destination != null) {
		destination.close();
	    }
	}
    }

    public static void writeToFile(String filePath, String data, boolean append) throws IOException {
	// Create file
	FileWriter fstream = new FileWriter(filePath, append);
	BufferedWriter out = new BufferedWriter(fstream);
	out.write(data);
	// Close the output stream
	out.close();
    }

    public static List<String> readFromFile(String filePath, int linesCount) throws IOException {
	FileInputStream fstream = new FileInputStream(filePath);
	BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	List<String> lines = new ArrayList<String>();
	int linesRead = 0;
	String line = null;
	while ((line = br.readLine()) != null && linesRead < linesCount) {
	    lines.add(line);
	    linesRead++;
	}
	// Close the input stream
	fstream.close();
	return lines;
    }
    
    public static List<String> readFromFile(String filePath) throws IOException {
	FileInputStream fstream = new FileInputStream(filePath);
	BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	List<String> lines = new ArrayList<String>();
	String line = null;
	while ((line = br.readLine()) != null ) {
	    lines.add(line);
	}
	// Close the input stream
	fstream.close();
	return lines;
    }
    
    public static void readFromFile(String filePath,LineHandler lineHandler) throws IOException {
	FileInputStream fstream = new FileInputStream(filePath);
	BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	String line = null;
	while ((line = br.readLine()) != null ) {
	    boolean shouldContinue=lineHandler.handle(line);
	    if(!shouldContinue) {
		break;
	    }
	}
	// Close the input stream
	fstream.close();
    }

    public static void copyFile(String source, String dest) throws IOException {
	copyFile(new File(source), new File(dest));
    }

    public static File initDirectory(String path) {
	File f = new File(path);
	if (f.exists() && (!f.isDirectory() || !f.canWrite())) {
	    throw new IllegalArgumentException("Expected a writable directory!");
	} else {
	    boolean created = f.mkdirs();
	    if (!created) {
		throw new IllegalStateException("Failed to create directory " + path);
	    }
	}
	return f;
    }
    
    public static boolean recursiveDelete(File f) {
	if(!f.isFile()) {
	    return f.delete();
	}
	/* assume is directory */
	for(File childFile : f.listFiles()) {
	    recursiveDelete(childFile);
	}
	return f.delete();
    }
}
