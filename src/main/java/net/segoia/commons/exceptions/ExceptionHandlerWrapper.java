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
package net.segoia.commons.exceptions;


public class ExceptionHandlerWrapper<O> extends ConfigurableExceptionHandler<O>{
    /**
     * The instance that actually does the handling
     */
    private ExceptionHandler<O> wrappedExceptionHandler;
    
    public ExceptionHandler<O> getWrappedExceptionHandler() {
        return wrappedExceptionHandler;
    }

    public void setWrappedExceptionHandler(ExceptionHandler<O> wrappedExceptionHandler) {
        this.wrappedExceptionHandler = wrappedExceptionHandler;
    }

    protected O doHandling(ContextAwareException e) throws ContextAwareException{
	return wrappedExceptionHandler.handle(e);
    }
    
    
}
