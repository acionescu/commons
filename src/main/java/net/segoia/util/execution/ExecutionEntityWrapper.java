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
package net.segoia.util.execution;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionHandler;

public class ExecutionEntityWrapper<E extends ExecutionEntity<I, O>, I, O> implements ExecutionEntity<I, O> {
    /**
     * the wrapped execution entity
     */
    private E wrappedExecutionEntity;
    /**
     * Exception handler for this entity
     */
    private ExceptionHandler<O> exceptionHandler;

    public O execute(I input) throws Exception {
	try {
	    return wrappedExecutionEntity.execute(input);
	} catch (Exception e) {
	    if (exceptionHandler != null) {
		if (e instanceof ContextAwareException) {
		    return exceptionHandler.handle((ContextAwareException) e);
		} else {
		    return exceptionHandler.handle(new ContextAwareException("GENERIC_EXECUTION_ERROR", e));
		}
	    }
	    throw new ContextAwareException("GENERIC_EXECUTION_ERROR", e);
	}
    }

    public E getWrappedExecutionEntity() {
        return wrappedExecutionEntity;
    }

    public ExceptionHandler<O> getExceptionHandler() {
        return exceptionHandler;
    }

    public void setWrappedExecutionEntity(E wrappedExecutionEntity) {
        this.wrappedExecutionEntity = wrappedExecutionEntity;
    }

    public void setExceptionHandler(ExceptionHandler<O> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
    
}
