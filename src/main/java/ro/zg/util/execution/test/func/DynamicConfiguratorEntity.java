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
package ro.zg.util.execution.test.func;

import ro.zg.util.execution.ExecutionEntity;
import ro.zg.util.execution.test.ContextExecutionEntity;
import ro.zg.util.execution.test.ExecutionContext;

public class DynamicConfiguratorEntity<I, O, C> implements
	ContextExecutionEntity<ExecutionContext<I, DynamicConfiguratorEntityConfiguration<I, O, C>>, O> {
    /**
     * returns f(i,c), where f is the executor, c - g(i)
     */
    public O execute(ExecutionContext<I, DynamicConfiguratorEntityConfiguration<I, O, C>> executionContext)
	    throws Exception {
	DynamicConfiguratorEntityConfiguration<I, O, C> config = executionContext.getConfig();
	ExecutionEntity<I, C> configurationCreator = config.getConfigurationCreator();
	ContextExecutionEntity<ExecutionContext<I, C>, O> executor = config.getExecutor();
	I input = executionContext.getInput();
	C dynamicConfig = configurationCreator.execute(input);
	ExecutionContext<I, C> execContext = new ExecutionContext<I, C>(input,dynamicConfig);
	return executor.execute(execContext);
    }

}
