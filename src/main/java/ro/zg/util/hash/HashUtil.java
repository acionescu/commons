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
package ro.zg.util.hash;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.crypto.dsig.DigestMethod;

public class HashUtil {

    public static String digest(String input, String hashType, String charSet) throws NoSuchAlgorithmException,
	    UnsupportedEncodingException {
	MessageDigest cript = MessageDigest.getInstance(hashType);
	cript.reset();
	cript.update(input.getBytes(charSet));
	return convertToHex(cript.digest());

    }

    public static String digestSHA1(String input, String charSet) throws UnsupportedEncodingException {
	try {
	    return digest(input, DigestMethod.SHA1, charSet);
	} catch (NoSuchAlgorithmException e) {

	}
	return null;
    }

    public static String digestSHA1(String input) {
	try {
	    return digest(input, "SHA-1", "UTF-8");

	} catch (NoSuchAlgorithmException e) {

	} catch (UnsupportedEncodingException e) {

	}
	return null;
    }

    public static String convertToHex(byte[] data) {
	StringBuffer buf = new StringBuffer();
	for (int i = 0; i < data.length; i++) {
	    int halfbyte = (data[i] >>> 4) & 0x0F;
	    int two_halfs = 0;
	    do {
		if ((0 <= halfbyte) && (halfbyte <= 9))
		    buf.append((char) ('0' + halfbyte));
		else
		    buf.append((char) ('a' + (halfbyte - 10)));
		halfbyte = data[i] & 0x0F;
	    } while (two_halfs++ < 1);
	}
	return buf.toString();
    }

    public static void main(String[] args) throws Exception {
	String input = "This is a test ";
	System.out.println(HashUtil.digest(input, "SHA-1", "UTF-8"));
	System.out.println(HashUtil.digestSHA1(input));
    }
}
