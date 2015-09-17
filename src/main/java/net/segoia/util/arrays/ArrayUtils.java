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
package net.segoia.util.arrays;

import java.util.Arrays;

public class ArrayUtils {
    public static void replace(byte[] array, byte oldVal, byte newVal) {
	for(int i=0;i<array.length;i++) {
	    if(array[i] == oldVal) {
		array[i] = newVal;
	    }
	}
    }
    
    public static int indexOf(byte[] array, byte val) {
	for(int i=0;i<array.length;i++) {
	    if(array[i] == val) {
		return i;
	    }
	}
	return -1;
    }
    
    public static int[] toIntArray(char[] charArray) {
	int[] result=new int[charArray.length];
	for(int i=0;i<charArray.length;i++) {
	    result[i]=Character.getNumericValue(charArray[i]);
	}
	return result;
    }
    
    public static int toInt(int[] array) {
	int last=array.length-1;
	int value=array[last];
	for(int i=last-1;i>=0;i--) {
	    value+=array[i]*Math.pow(10, last-i);
	}
	return value;
    }
    
    public static void main(String[] args) {
	int[] ia = new int[] {2,3,5,1,3};
	System.out.println(toInt(ia));
    }
}
