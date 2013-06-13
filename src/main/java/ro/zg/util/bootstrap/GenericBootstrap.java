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
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;

public class GenericBootstrap {
    
   private void initFromConfig(GenericBootstrapConfig config) throws Exception {
       ClassLoader classLoader = new URLClassLoader(config.getUrls(),ClassLoader.getSystemClassLoader().getParent());
       Class mainClass = classLoader.loadClass(config.getMainClass());
       Method mainMethod = mainClass.getMethod("main", new Class[] {new String[0].getClass()});
       System.out.println("Bootstrapping "+mainClass+":"+mainMethod+" with classloader "+classLoader);
       if(config.getResourcesToCheckFor() != null) {
	   for(String res : config.getResourcesToCheckFor()) {
	       System.out.println(res +" found at "+classLoader.getResource(res));
	   }
       }
       Thread.currentThread().setContextClassLoader(classLoader);
       mainMethod.invoke(null, new Object[] {config.getArgs()});
   }
    
    public static void main(String[] args) throws Exception {
	start(args);
    }
    
    public static void start(String[] args) throws Exception {
	String configFileName = "bootstrap.properties";
	if(args != null && args.length > 0) {
	    configFileName = args[0];
	}
	File f = new File(configFileName);
	GenericBootstrapConfig config = GenericBootstrapConfig.loadFromFile(f);
	GenericBootstrap bootstrap = new GenericBootstrap();
	bootstrap.initFromConfig(config);
    }
    
    public static void start(URI uri) throws Exception {
	File f = new File(uri);
	GenericBootstrapConfig config = GenericBootstrapConfig.loadFromFile(f);
	GenericBootstrap bootstrap = new GenericBootstrap();
	bootstrap.initFromConfig(config);
    }

}
