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
package net.segoia.util.net.resources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.TransformerException;

public class URIResolver {
    public String resolve(String href, String base) throws TransformerException, MalformedURLException {

	String uri = href;
	int hashPos = href.indexOf("#");
	if (hashPos >= 0) {
	    uri = href.substring(0, hashPos);
	}

	String result = null;

	try {
	    URL url = null;

	    if (base == null) {
		url = new URL(uri);
		result = url.toString();
	    } else {
		URL baseURL = new URL(base);
		url = (href.length() == 0 ? baseURL : new URL(baseURL, uri));
		result = url.toString();
	    }
	} catch (java.net.MalformedURLException mue) {
	    // try to make an absolute URI from the current base
	    String absBase = makeAbsolute(base);
	    if (!absBase.equals(base)) {
		// don't bother if the absBase isn't different!
		return resolve(href, absBase);
	    } else {
		throw mue;
	    }
	}
	return result;
    }

    /** Attempt to construct an absolute URI */
    private String makeAbsolute(String uri) {
	if (uri == null) {
	    uri = "";
	}

	try {
	    URL url = new URL(uri);
	    return url.toString();
	} catch (MalformedURLException mue) {
	    try {
		// URL fileURL = FileURL.makeURL(uri);
		File file = new File(uri);
		return file.toURI().toURL().toString();
		// return fileURL.toString();
	    } catch (MalformedURLException mue2) {
		// bail
		return uri;
	    }
	}
    }
}
