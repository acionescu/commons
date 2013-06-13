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
package ro.zg.util.processing;

public class RunnableProcessor<I, O> implements Runnable {
    private Processor<I, O> processor;
    private Source<I> source;
    private Destination<I, O> destination;

    public RunnableProcessor(Processor<I, O> processor, Source<I> source, Destination<I, O> destination) {
	this.processor = processor;
	this.source = source;
	this.destination = destination;
    }

    public void run() {
	I input = source.getInput();
	O output = null;

	try {
	    output = processor.process(input);
	    destination.addOutput(input, output);
	} catch (Exception e) {
	    destination.addError(input, e);
	}

    }
}
