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
package net.segoia.util.execution.test.func;

import net.segoia.util.execution.ExecutionEntity;
import net.segoia.util.execution.test.ContextExecutionEntity;
import net.segoia.util.execution.test.ExecutionContext;

public class DynamicConfiguratorEntityConfiguration<I,O,C> {
    private ExecutionEntity<I, C> configurationCreator;
    private ContextExecutionEntity<ExecutionContext<I,C>, O> executor;
    
    public ExecutionEntity<I, C> getConfigurationCreator() {
        return configurationCreator;
    }
    public ContextExecutionEntity<ExecutionContext<I, C>, O> getExecutor() {
        return executor;
    }
    public void setConfigurationCreator(ExecutionEntity<I, C> configurationCreator) {
        this.configurationCreator = configurationCreator;
    }
    public void setExecutor(ContextExecutionEntity<ExecutionContext<I, C>, O> executor) {
        this.executor = executor;
    }
    
    
}
