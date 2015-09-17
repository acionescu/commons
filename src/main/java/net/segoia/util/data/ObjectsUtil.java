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
package net.segoia.util.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.segoia.util.data.reflection.ReflectionUtility;

public class ObjectsUtil {
    /**
     * Returns a copy of the object, or null if the object cannot be serialized.
     */
    public static Object copy(Object orig) {
	Object obj = null;
	try {
	    // Write the object out to a byte array
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ObjectOutputStream out = new ObjectOutputStream(bos);
	    out.writeObject(orig);
	    out.flush();
	    out.close();

	    // Make an input stream from the byte array and read
	    // a copy of the object back in.
	    ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
	    obj = in.readObject();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException cnfe) {
	    cnfe.printStackTrace();
	}
	return obj;
    }

    public static Map createMapFromCollection(Collection<?> source, String keyProp){
	if(source == null || keyProp == null){
	    return null;
	}
	LinkedHashMap<Object, Object> map = new LinkedHashMap<Object,Object>();
	for(Object o : source){
	    try {
		map.put(ReflectionUtility.getValueForField(o, keyProp), o);
	    } catch (Exception e) {
		throw new IllegalArgumentException("Field '"+keyProp+"' for class '"+o.getClass()+"' could not be read");
	    }
	}
	return map;
    }
    
    public static boolean areEqual(Object o1, Object o2) {
	if(o1!=null) {
	    if(o2==null) {
		return false;
	    }
	    return o1.equals(o2);
	}
	return o2==null;
    }
}
