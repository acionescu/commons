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
package ro.zg.util.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GenericBootstrapConfig {
    private URL[] urls;
    private String mainClass;
    private String[] args;
    private String[] resourcesToCheckFor;

    public static GenericBootstrapConfig loadFromFile(File f) throws IOException {
	Properties p = new Properties();
	p.load(new FileInputStream(f));
	String locations = p.getProperty("locations");
	String[] locationsArray = locations.split(",");

	GenericBootstrapConfig config = new GenericBootstrapConfig();
	config.setUrls(config.locationsToURLs(locationsArray).toArray(new URL[0]));
	config.setMainClass(p.getProperty("mainClass"));

	config.setArgs(p.getProperty("args").split(","));

	String resourcesToCheckFor = p.getProperty("resourcesToCheckFor");
	if (resourcesToCheckFor != null) {
	    config.setResourcesToCheckFor(resourcesToCheckFor.split(","));
	}

	return config;

    }

    private List<URL> locationsToURLs(String[] locations) throws IOException {
	ArrayList<URL> l = new ArrayList<URL>();
	for (String location : locations) {
	    l.add(new URL(location));
	}
	return l;
    }

    /**
     * @return the urls
     */
    public URL[] getUrls() {
	return urls;
    }

    /**
     * @return the mainClass
     */
    public String getMainClass() {
	return mainClass;
    }

    /**
     * @param urls
     *            the urls to set
     */
    public void setUrls(URL[] urls) {
	this.urls = urls;
    }

    /**
     * @param mainClass
     *            the mainClass to set
     */
    public void setMainClass(String mainClass) {
	this.mainClass = mainClass;
    }

    /**
     * @return the args
     */
    public String[] getArgs() {
	return args;
    }

    /**
     * @param args
     *            the args to set
     */
    public void setArgs(String[] args) {
	this.args = args;
    }

    /**
     * @return the resourcesToCheckFor
     */
    public String[] getResourcesToCheckFor() {
	return resourcesToCheckFor;
    }

    /**
     * @param resourcesToCheckFor
     *            the resourcesToCheckFor to set
     */
    public void setResourcesToCheckFor(String[] resourcesToCheckFor) {
	this.resourcesToCheckFor = resourcesToCheckFor;
    }

}
