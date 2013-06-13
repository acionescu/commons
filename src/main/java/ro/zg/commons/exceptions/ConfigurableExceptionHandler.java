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

public abstract class ConfigurableExceptionHandler<O> extends ExtendedExceptionHandler<O> {
    /**
     * Holds the configuration parameters for this handler
     */
    private ExceptionHandlerConfiguration config = new ExceptionHandlerConfiguration();

    protected O afterHandling(ContextAwareException e, O output) throws ContextAwareException {
	reraise(e);
	return output;
    }

    protected void beforeHandling(ContextAwareException e) {
	// TODO Auto-generated method stub

    }

    protected O onException(ContextAwareException oldException, ContextAwareException newException)
	    throws ContextAwareException {
	reraise(newException);
	return null;
    }

    protected void reraise(ContextAwareException e) throws ContextAwareException {
	if (config != null && config.isReraise()) {
	    throw e;
	}
    }

    public ExceptionHandlerConfiguration getConfig() {
	return config;
    }

    public void setConfig(ExceptionHandlerConfiguration config) {
	this.config = config;
    }
}
