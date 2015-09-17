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

import net.segoia.util.execution.ExecutionEntity;

public class ExecutionEntityWrapperConfiguration<I,O,C> implements ConfigurationEntity {
    private ContextExecutionEntity<ExecutionContext<I, C>, O> wrappedEntity;
    private C wrappedEntityConfiguration;
    private ExecutionEntity<EntityExceptionContext, O> exceptionHandler;

    public ExecutionEntity<EntityExceptionContext, O> getExceptionHandler() {
	return exceptionHandler;
    }

    public void setExceptionHandler(ExecutionEntity<EntityExceptionContext, O> exceptionHandler) {
	this.exceptionHandler = exceptionHandler;
    }


    public C getWrappedEntityConfiguration() {
        return wrappedEntityConfiguration;
    }

    public void setWrappedEntityConfiguration(C wrappedEntityConfiguration) {
        this.wrappedEntityConfiguration = wrappedEntityConfiguration;
    }

    public ContextExecutionEntity<ExecutionContext<I, C>, O> getWrappedEntity() {
        return wrappedEntity;
    }

    public void setWrappedEntity(ContextExecutionEntity<ExecutionContext<I, C>, O> wrappedEntity) {
        this.wrappedEntity = wrappedEntity;
    }

}
