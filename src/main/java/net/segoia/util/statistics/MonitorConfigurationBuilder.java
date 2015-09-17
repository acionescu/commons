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
/**
 * $Id: MonitorConfigurationBuilder.java,v 1.1 2007/10/30 07:54:02 aionescu Exp $
 */
package net.segoia.util.statistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Creates a {@link MonitorConfiguration} object for the specified monitor name from a properties file. The default name
 * of the file is <monitor.properties> and should be located in the classpath of the application The property keys
 * defined in the configuration files should be like <name of the monitor>.<name of the actual property> If no
 * definition of this type is found it will try to load the configuration like <part of the name of the monitor>.<name
 * of the actual property> It is hightly advised to use as names for the monitors the names of the classes you want to
 * monitor So for example for the class com.cosmote.rtbus.Path use the monitor with name com.cosmote.rtbus.Path Doing
 * like this you get better control over the configuration So if you want to explicitlly define the property <enabled>
 * for this monitor you do like this com.cosmote.rtbus.Path.enabled = true Otherwise if you want package control you
 * only define com.cosmote.rtbus.enabled = true and all the monitors defined for the classes located in this package or
 * deeper in the hierarcy will inherit this property if a higher level one it's not specified.
 * 
 * @author aionescu
 * @version $Revision: 1.1 $
 */
public class MonitorConfigurationBuilder {
    /**
     * default configuration file name
     */
    private static String DEFAULT_CONFIGURATIN_FILE = "monitor.properties";
    /**
     * the properties which will be read from the configuration file
     */
    private static String[] confKeys = new String[] { "enabled" };
    /**
     * for every defined property in the confKeys will hold a list with the property keys defined in the configuration
     * file for it
     */
    private static Hashtable availableKeyPropertyValues = new Hashtable();
    /**
     * loads the configuration file
     */
    private static Properties confProperties = new Properties();
    /**
     * stores the configurations already created
     */
    private static Hashtable configurations = new Hashtable();
    /**
     * logger for this class
     */
    private static Logger logger = Logger.getLogger(MonitorConfigurationBuilder.class);

    /**
     * this static block loads / tries to load the configurations from the default file
     */
    static {
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	URL confUrl = classLoader.getResource(DEFAULT_CONFIGURATIN_FILE);
	if (confUrl != null) {
	    String path = confUrl.getPath().replace("%20", " ");

	    try {
		confProperties.load(new FileInputStream(new File(path)));
		precompile();
	    } catch (FileNotFoundException e) {
		logger.warn(DEFAULT_CONFIGURATIN_FILE + " file could not be found");
	    } catch (IOException e) {
		logger.warn("Could not load " + DEFAULT_CONFIGURATIN_FILE);
	    }
	}
	else{
	    logger.warn("Could not load " + DEFAULT_CONFIGURATIN_FILE);
	}
    }

    /**
     * Parses the properties file and groups the the keys by the properties defined in confKeys
     * 
     */
    private static void precompile() {
	Enumeration propNames = confProperties.propertyNames();
	while (propNames.hasMoreElements()) {
	    String propName = (String) propNames.nextElement();
	    for (int i = 0; i < confKeys.length; i++) {
		String keyProp = confKeys[i];
		if (propName.indexOf(keyProp) != -1) {
		    addKeyPropertyValue(keyProp, propName);
		}
	    }
	}
    }

    public static void configure(MonitorBehaviour monitor) {
	String name = monitor.getName();
	for (int i = 0; i < confKeys.length; i++) {
	    String confKey = confKeys[i];
	    List availablePropKeys = (List) availableKeyPropertyValues.get(confKey);
	    if (availablePropKeys == null) {
		continue;
	    }
	    // iterates on all property keys defined for this property(confKey)
	    for (int j = 0; j < availablePropKeys.size(); j++) {
		String keyProp = (String) availablePropKeys.get(j);
		// tries to get the key property with the biggest length that matches
		// with the specified name.This way will go from specific to general.
		if (name.indexOf(keyProp.substring(0, keyProp.length() - confKey.length() - 1)) != -1) {
		    String value = confProperties.getProperty(keyProp);
		    try {
			// tries to set the property through reflection
			// a setter for this property should exist on MonitorConfiguration class
			// ReflectionUtility.setValueToField(monitor, confKey, value);
		    } catch (Exception e) {
			logger.error("Cannot save monitor configuration property '" + confKey + "' with value '"
				+ value + ",.", e);
		    }

		    break;
		}
	    }
	}
    }

    /**
     * Adds a new property key for the specified confKey
     * 
     * @param confKey
     * @param propertyKey
     */
    private static void addKeyPropertyValue(String confKey, String propertyKey) {
	List values = (List) availableKeyPropertyValues.get(confKey);
	if (values == null) {
	    values = new ArrayList();
	    availableKeyPropertyValues.put(confKey, values);
	}
	values.add(propertyKey);
	Collections.sort(values, new LengthComparator());
    }

}

/**
 * Used to sort the list of property keys from the bigges length to the smallest
 * 
 * @author aionescu
 * @version $Revision: 1.1 $
 */
class LengthComparator implements Comparator {

    public int compare(Object obj1, Object obj2) {
	if (obj1 == obj2 || obj1.equals(obj2)) {
	    return 0;
	}

	if (obj1 instanceof String && obj2 instanceof String) {
	    String str1 = (String) obj1;
	    String str2 = (String) obj2;
	    if (str1.length() > str2.length()) {
		return -1;
	    } else if (str1.length() < str2.length()) {
		return 1;
	    }
	}
	return 0;
    }

}
