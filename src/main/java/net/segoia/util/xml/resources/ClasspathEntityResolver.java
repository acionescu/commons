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
package net.segoia.util.xml.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Resolves a request for a resource using the {@link ClassLoader} resource
 * loading mechanism
 * 
 * @author aionescu
 * @version $Revision: 1.1 $
 */
public class ClasspathEntityResolver implements EntityResolver {
    private ClassLoader classLoader;

    public ClasspathEntityResolver() {
	classLoader = getClass().getClassLoader();
    }

    public ClasspathEntityResolver(ClassLoader cl) {
	this.classLoader = cl;
    }

    public InputSource resolveEntity(String publicId, String systemId)
	    throws SAXException, IOException {
	// System.out.println("resolve entity : publicId=" + publicId +
	// " , systemId=" + systemId);
	// System.out.println("using classloader "+classLoader);
	if (systemId == null) {
	    return null;
	}
	String resourceName = systemId;
	InputStream in = null;
	int index = -1;
	String separator = File.separator;
	try {
	    new URL(resourceName);
	    separator="/";
	} catch (Exception e) {
	    e.printStackTrace();
	}
	do {
	    resourceName = resourceName.substring(index + 1);
	    in = classLoader.getResourceAsStream(resourceName);
	    index = resourceName.indexOf(separator);

	} while (index >= 0 && in == null);

	if (in == null) {
	    throw new IOException("Failed to resolve " + resourceName);
	}

	return new InputSource(in);
    }

};
