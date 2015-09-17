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
package net.segoia.util.data.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class AbstractSerializationUtility<O> {
    
    protected abstract File getFileForId(String id);
    
    protected File getFileForId(String id, boolean createIfInexistent) throws IOException{
	File f = getFileForId(id);
	if( !f.exists() && createIfInexistent ){
	    /* if the parent directory does not exist, create it */
	    if(!f.getParentFile().exists()){
		f.getParentFile().mkdirs();
	    }
	    f.createNewFile();
	}
	return f;
    }
    
    protected FileInputStream getInputStreamForId(String id, boolean createIfInexistent) throws IOException{
	File f = getFileForId(id,createIfInexistent);
	if( !f.exists() ){
	   return null;
	}
	return new FileInputStream(f);
    }
    
    protected O getObjectForId(String id, boolean createIfInexistent) throws IOException, ClassNotFoundException{
	FileInputStream fis = getInputStreamForId(id, createIfInexistent);
	if(fis == null){
	    return null;
	}
	ObjectInputStream dis = new ObjectInputStream(fis);
	return (O)dis.readObject();
    }
    
    protected FileOutputStream getOutputStreamForId(String id) throws IOException{
	File f = getFileForId(id,true);
	return new FileOutputStream(f);
    }
    
    protected void saveObject(O o, String id) throws IOException{
	FileOutputStream fos = getOutputStreamForId(id);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	oos.writeObject(o);
    }
}
