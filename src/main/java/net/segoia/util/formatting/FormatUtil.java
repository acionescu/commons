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
package net.segoia.util.formatting;

import java.util.Arrays;
import java.util.Collection;

public class FormatUtil {
    
    public static <T> String formatCollection(Collection<T> c, String separator, String itemEncloser, String start, String end) {
	StringBuffer out = new StringBuffer();
	
	boolean first = true;
	
	for(T item : c) {
	    if(!first) {
		out.append(separator);
	    }
	    
	    out.append(encloseObject(item, itemEncloser,itemEncloser));
	    
	    first = false;
	}
	
	return encloseObject(out, start, end);
    }
    
    public static String encloseObject(Object o,String start, String end) {
	return start+o.toString()+end;
    }

    
    public static void main(String[] args) {
	String[] arr = new String[] {"item1","item2","item3"};
	
	String out = formatCollection(Arrays.asList(arr),",","'","[","]");
	
	System.out.println(out);
    }
    
}
