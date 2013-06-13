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
package ro.zg.util.execution.test;

import java.util.Map;

import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.NameValue;
import ro.zg.util.execution.ExecutionEntity;

public class DynamicExecutionEntity<O> implements ExecutionEntity<GenericNameValueContext, O> {

    public O execute(GenericNameValueContext input) throws Exception {
	GenericNameValueContext globalContext = (GenericNameValueContext) input.getValue("global-context");
	GenericNameValueContext staticContext = getStaticContext(input);
	Map<String, String> dynamicInput = (Map<String, String>) input.getValue("dynamic-input");
	
	ExecutionEntity<GenericNameValueContext, O> executor = (ExecutionEntity<GenericNameValueContext, O>) input
		.getValue("executor");

	GenericNameValueContext localContext = getLocalContext(globalContext, staticContext, dynamicInput);
	O result = executor.execute(localContext);
	return result;
    }
    /**
     * Extracts the static context from the input
     * This can either be already created with the name static-input or 
     * it can come as a raw object map in which case a {@link GenericNameValueContext} 
     * is created
     * @param input
     * @return
     */
    private GenericNameValueContext getStaticContext(GenericNameValueContext input){
	GenericNameValueContext staticContext = (GenericNameValueContext) input.getValue("static-input");
	if(staticContext == null){
	    Map<String,Object> rawStaticInput =  (Map<String,Object>)input.getValue("raw-static-input");
	    if(rawStaticInput != null){
		staticContext = new GenericNameValueContext();
		staticContext.putMap(rawStaticInput);
	    }
	}
	return staticContext;
    }
    /**
     * Creates the local component context 
     * Adds the static parameters
     * Extracts the dynamic parameters from the global context and sets them on the local context
     * @param globalContext
     * @param staticContext
     * @param dynamicParameters
     * @return
     */
    private GenericNameValueContext getLocalContext(GenericNameValueContext globalContext,
	    GenericNameValueContext staticContext, Map<String, String> dynamicParameters) {
	GenericNameValueContext localContext = new GenericNameValueContext();
	if (staticContext != null) {
	    localContext.putAll(staticContext.getParameters());
	}
	for (Map.Entry<String, String> entry : dynamicParameters.entrySet()) {
	    String key = entry.getKey();
	    String value = entry.getValue();
	    NameValue<Object> contextParam = globalContext.get(value);
	    if (contextParam != null) {
		localContext.put(key, contextParam.getValue());
	    }
	}

	return localContext;
    }

}
