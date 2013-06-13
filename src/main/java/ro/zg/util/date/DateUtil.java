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
package ro.zg.util.date;


public class DateUtil {

    public static String getMiliNano() {
	long mili = System.currentTimeMillis();
	long nano = System.nanoTime();
	return "" + mili + ("" + nano).substring(7);
    }
    
    public static String removeNanos(String date) {
	return date.split("\\.")[0];
    }
    
    public static void main (String[] args) {
	System.out.println(DateUtil.removeNanos("2010-06-22 11:58:06.656855"));
    }

}
