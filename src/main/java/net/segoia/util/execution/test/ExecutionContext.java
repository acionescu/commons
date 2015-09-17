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
package net.segoia.util.execution.test;

public class ExecutionContext<I, C> {
    private I input;
    private C config;
    
    public ExecutionContext(){
	
    }
    
    public ExecutionContext(I input, C config){
	this.input = input;
	this.config = config;
    }

    public I getInput() {
	return input;
    }

    public C getConfig() {
	return config;
    }

    public void setInput(I input) {
	this.input = input;
    }

    public void setConfig(C config) {
	this.config = config;
    }

}
