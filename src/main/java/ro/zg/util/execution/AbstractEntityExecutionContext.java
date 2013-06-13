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
package ro.zg.util.execution;

import java.util.List;

import ro.zg.commons.exceptions.ExceptionHandler;

public abstract class AbstractEntityExecutionContext<E extends ExecutionEntity<I, O>, I, O> implements
	ExecutionEntity<I, O> {
    /**
     * Listeners to be called before and after execution of the wrapped entity
     */
    private List<ExecutionLifeCycleListener<E, I, O>> listeners;
    /**
     * the wrapped execution entity
     */
    private E wrappedExecutionEntity;
    /**
     * Exception handler for this entity
     */
    private ExceptionHandler<O> exceptionHandler;

    public O execute(I input) throws Exception {
	O newOutput = null;
	try {
	    I newInput = callBeforeExecutionOnListeners(input);
	    O output = wrappedExecutionEntity.execute(newInput);
	    newOutput = callAfterExecutionOnListeners(input, output);
	} catch (Exception e) {
	    newOutput = handleException(wrappedExecutionEntity, input, e);
	}
	return newOutput;
    }

    protected I callBeforeExecutionOnListeners(I input) throws Exception {
	I currentInput = input;
	if (listeners != null) {
	    for (ExecutionLifeCycleListener<E, I, O> listener : listeners) {
		currentInput = listener.beforeExecution(getBeforeExectionContext(currentInput));
	    }
	}
	return currentInput;
    }

    protected O callAfterExecutionOnListeners(I input, O output) throws Exception {
	O currentOutput = output;
	if (listeners != null) {
	    for (int i = listeners.size() - 1; i >= 0; i--) {
		ExecutionLifeCycleListener<E, I, O> listener = listeners.get(i);
		currentOutput = listener.afterExecution(getAfterExectionContext(input, currentOutput));
	    }
	}
	return currentOutput;
    }

    protected abstract BeforeExecutionContext<E, I, O> getBeforeExectionContext(I input);

    protected abstract AfterExecutionContext<E, I, O> getAfterExectionContext(I input, O output);

    private O handleException(ExecutionEntity<I, O> target, I input, Exception e) throws Exception {
	if (exceptionHandler != null) {
//	    return exceptionHandler.handle(e, target, input);
	}
	/* if we don't have a handler, we cannot do something more intelligent */
	throw e;
    }

    public List<ExecutionLifeCycleListener<E, I, O>> getListeners() {
	return listeners;
    }

    public E getWrappedExecutionEntity() {
	return wrappedExecutionEntity;
    }

    public void setListeners(List<ExecutionLifeCycleListener<E, I, O>> listeners) {
	this.listeners = listeners;
    }

    public void setWrappedExecutionEntity(E wrappedExecutionEntity) {
	this.wrappedExecutionEntity = wrappedExecutionEntity;
    }

    public ExceptionHandler<O> getExceptionHandler() {
	return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler<O> exceptionHandler) {
	this.exceptionHandler = exceptionHandler;
    }

}
