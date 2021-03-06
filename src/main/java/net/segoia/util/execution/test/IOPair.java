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

public class IOPair<I,O> {
    private I input;
    private O output;
    
    public IOPair(I input, O output){
	this.input = input;
	this.output = output;
    }
    
    public IOPair(){
	
    }

    public I getInput() {
        return input;
    }

    public O getOutput() {
        return output;
    }

    public void setInput(I input) {
        this.input = input;
    }

    public void setOutput(O output) {
        this.output = output;
    }
}
