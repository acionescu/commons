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
package ro.zg.commons.exceptions;

public abstract class ExtendedExceptionHandler<O> implements ExceptionHandler<O> {

    protected abstract O doHandling(ContextAwareException cause) throws ContextAwareException;

    protected abstract void beforeHandling(ContextAwareException cause) throws ContextAwareException;

    protected abstract O afterHandling(ContextAwareException cause, O output) throws ContextAwareException;

    protected abstract O onException(ContextAwareException oldCause, ContextAwareException newCause)
	    throws ContextAwareException;

    public O handle(ContextAwareException cause) throws ContextAwareException {
	beforeHandling(cause);
	O output = null;
	try {
	    output = doHandling(cause);
	} catch (ContextAwareException ex) {
	    return onException(cause, ex);
	}
	return afterHandling(cause, output);

    }

}
