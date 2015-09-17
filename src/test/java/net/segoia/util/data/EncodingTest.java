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

import junit.framework.Assert;
import net.segoia.util.data.encoding.EncodingUtil;

import org.junit.Test;

public class EncodingTest {

   
    public void encodeTest() {
	String origin = "\\abcdebcfghdfhdfhdfpdddeftyx";
	String current = "abcdeygdfhdfhvdeftyx";

	String encoded = EncodingUtil.encode(origin, current);
	System.out.print(encoded);
	Assert.assertEquals("\\\\:4+bcfgh:7-12+dfpdd:14", encoded);
	String decoded=EncodingUtil.decode(encoded, current);
	System.out.println(" -> "+decoded);
	Assert.assertEquals(origin, decoded);

	origin = current.substring(3, 10);
	encoded = EncodingUtil.encode(origin, current);
	System.out.println(encoded);
	Assert.assertEquals(":3-9", encoded);

	origin = "gabcdebc:f\\ghdfhdfhdfpdddeftyx";
	current = "abcdeygdfhdfhvdeftyx";

	encoded = EncodingUtil.encode(origin, current);
	System.out.println(encoded);
	Assert.assertEquals("g:4+bc\\:f\\\\gh:7-12+dfpdd:14", encoded);
	
	origin=current;
	encoded = EncodingUtil.encode(origin, current);
	System.out.println(encoded);
	Assert.assertEquals(":", encoded);
	
	origin="y;*UaVuH);BÁmÂ{I>Z5I";
	current=";*UaVuH)dtjg;BÁmÂ{I>Z5I";
	encoded = EncodingUtil.encode(origin, current);
	System.out.print(encoded);
	decoded=EncodingUtil.decode(encoded, current);
	System.out.println(" -> "+decoded);
	Assert.assertEquals(origin, decoded);
    }

   
    public void encodeStressTest() {
	int constLength = 300;
	int lengthVariance = 300;
	int maxIterations=1000;
	int iterations = 0;
	double compressRatio=0;
	
	while (iterations++ < maxIterations) {

	    String origin = generateRandomString(constLength + (int) (Math.random() * lengthVariance));
	   
	    String current = randomChange(origin, (int) Math.round(Math.random() * 3));
	    
	    String encoded=EncodingUtil.encode(origin, current);
	    
	    String decoded=EncodingUtil.decode(encoded, current);
	    
	    Assert.assertEquals("encoded="+encoded+"\ncurrent="+current,origin, decoded);

	    compressRatio+=(double)encoded.length()/origin.length();
	}
	
	System.out.println("compress ratio= "+(compressRatio/maxIterations));
    }

    private String generateRandomString(int l) {
	StringBuffer sb = new StringBuffer();
	int i = 0;
	while (i++ < l) {
	    sb.append((char) (int) (1 + Math.random() * 200));
	}
	return sb.toString();
    }

    private String randomChange(String input, int changesCount) {
	int change = 0;
	String result = input;
	int maxChangeLength = (int) (0.05 * input.length());

	while (change++ < changesCount) {

	    int changeLength = (int) (Math.random() * maxChangeLength);
	    int pos = (int) (Math.random() * (result.length() - 1));
//	    pos=Math.min(pos, (input.length()-1));
	    double changeType = Math.random();

	    try {
	    /* add something */
	    if (changeType < 0.33) {
		result = result.substring(0, pos) + generateRandomString(changeLength) + result.substring(pos);
	    }
	    /* remove something */
	    else if (changeType < 0.66) {
		result = result.substring(0, pos) + result.substring(Math.min(pos + changeLength, result.length()-1));
	    }
	    /* change something */
	    else {
		changeLength = Math.min(changeLength, result.length() - pos-1);
		result = result.substring(0, pos) + generateRandomString(changeLength)
			+ result.substring(pos + changeLength);
	    }
	    }
	    catch(RuntimeException e) {
		System.out.println("input= "+input.length());
		System.out.println("pos= "+pos);
		System.out.println("change= "+change);
		throw e;
	    }
	}
	return result;
    }

}
