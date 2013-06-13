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

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.execution.ExecutionEntity;

public class ExecutionEntityWrapper<I, O, C> implements ExecutionEntity<I, O> {
    private ExecutionEntityWrapperConfiguration<I,O,C> config;

    public O execute(I input) throws Exception {
	ContextExecutionEntity<ExecutionContext<I, C>, O> wrappedEntity = config.getWrappedEntity();
	C wrappedEntityConfiguration = config.getWrappedEntityConfiguration();
	O result = null;
	try {
	    result = wrappedEntity.execute(new ExecutionContext(input, wrappedEntityConfiguration));
	} catch (Exception e) {
	    /* create exception context */
	    EntityExceptionContext ec = new EntityExceptionContext(e, input, wrappedEntity, wrappedEntityConfiguration);
	    ExecutionEntity<EntityExceptionContext, O> exceptionHandler = config.getExceptionHandler();
	    if (exceptionHandler != null) {
		result = exceptionHandler.execute(ec);
	    } else {
		/* if no handler defined throw exception */
		ExceptionContext ecc = new ExceptionContext();
		ecc.put(new GenericNameValue("context", ec));
		throw new ContextAwareException("EXECUTION_ENTITY_EXCEPTION", e, ecc);
	    }
	}
	return result;
    }

    public ExecutionEntityWrapperConfiguration getConfig() {
	return config;
    }

    public void setConfig(ExecutionEntityWrapperConfiguration config) {
	this.config = config;
    }

}
