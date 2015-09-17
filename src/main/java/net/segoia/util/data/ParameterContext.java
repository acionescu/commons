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

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ParameterContext<P> implements Serializable{

   
    /**
     * 
     */
    private static final long serialVersionUID = 1152418403033310828L;

//    /**
//     * key - the name of the parameter value - the actual {@link Parameter}
//     */
//    protected Map<String, Integer> paramsMap = new HashMap<String, Integer>();
//
//    protected List<P> parameters = new ArrayList<P>();
    
    protected Map<String, P> parameters = new LinkedHashMap<String, P>();
    
    private Deque<String> executionStack = new ArrayDeque<String>();
    /**
         * 
         */
    public ParameterContext() {
	
    }

    public P get(String name) {
	return parameters.get(name);
    }

   
    public P remove(String name) {
	return parameters.remove(name);
    }
    
    public abstract P put(P parameter);

    public void putAll(Map<String, P> source) {
	for (P p : source.values()) {
	    put(p);
	}
    }

    public void putAll(List<P> source) {
	if(source == null) return;
	for (P p : source) {
	    put(p);
	}
    }

    /**
     * Returns a list of {@link Parameter}(s)
     * 
     * @param names
     *            - list with the name of the parameters
     * @return
     */
    public List<P> get(List<String> names) {
	List<P> result = new ArrayList<P>();
	for (String name : names) {
	    result.add(parameters.get(name));
	}
	return result;
    }
    
    public void clear(){
	parameters.clear();
    }
    
    public int size(){
	return parameters.size();
    }
    
    public String toString(){
	return parameters.toString();
    }

    public List<P> getParametersAsList() {
        return new ArrayList<P>(parameters.values());
	
    }

    public Map<String, P> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, P> parameters) {
        this.parameters = parameters;
    }
    
    public void pushToExecutionStack(String entityId) {
	executionStack.push(entityId);
    }
    
    public String popExecutionStack() {
	return executionStack.pop();
    }
    

    /**
     * @return the executionStack
     */
    public Deque<String> getExecutionStack() {
        return executionStack;
    }

    /**
     * @param executionStack the executionStack to set
     */
    public void setExecutionStack(Deque<String> executionStack) {
        this.executionStack = executionStack;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ParameterContext other = (ParameterContext) obj;
	if (parameters == null) {
	    if (other.parameters != null)
		return false;
	} else if (!parameters.equals(other.parameters))
	    return false;
	return true;
    }

}
