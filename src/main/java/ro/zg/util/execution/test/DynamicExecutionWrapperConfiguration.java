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
import ro.zg.util.execution.ExecutionEntity;

public class DynamicExecutionWrapperConfiguration<O> {
    private Map<String, Object> staticParameters;
    private ExecutionEntity<GenericNameValueContext, O> executor;
    private GenericNameValueContext staticContext;
    private Map<String, String> dynamicParameters;
    private String originalInputName;

    public void init() {
	if (staticParameters != null) {
	    staticContext = new GenericNameValueContext();
	    staticContext.putMap(staticParameters);
	}
    }

    public Map<String, Object> getStaticParameters() {
	return staticParameters;
    }

    public GenericNameValueContext getStaticContext() {
	return staticContext;
    }

    public Map<String, String> getDynamicParameters() {
	return dynamicParameters;
    }

    public void setStaticParameters(Map<String, Object> staticParameters) {
	this.staticParameters = staticParameters;
    }

    public void setDynamicParameters(Map<String, String> dynamicParameters) {
	this.dynamicParameters = dynamicParameters;
    }

    public ExecutionEntity<GenericNameValueContext, O> getExecutor() {
	return executor;
    }

    public void setExecutor(ExecutionEntity<GenericNameValueContext, O> executor) {
	this.executor = executor;
    }

    public String getOriginalInputName() {
        return originalInputName;
    }

    public void setOriginalInputName(String originalInputName) {
        this.originalInputName = originalInputName;
    }

}
