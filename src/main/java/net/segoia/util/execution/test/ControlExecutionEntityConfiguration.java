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

public class ControlExecutionEntityConfiguration implements ConfigurationEntity{
    private ExecutionEntity mainExecutionEntity;
    private ExecutionEntity controlExecutionEntity;
    
    private ConfigurationEntity mainEntityConfiguration;
    private ConfigurationEntity controlEntityConfiguration;
    
    public ExecutionEntity getMainExecutionEntity() {
        return mainExecutionEntity;
    }
    public ExecutionEntity getControlExecutionEntity() {
        return controlExecutionEntity;
    }
    public void setMainExecutionEntity(ExecutionEntity mainExecutionEntity) {
        this.mainExecutionEntity = mainExecutionEntity;
    }
    public void setControlExecutionEntity(ExecutionEntity controlExecutionEntity) {
        this.controlExecutionEntity = controlExecutionEntity;
    }
    public ConfigurationEntity getMainEntityConfiguration() {
        return mainEntityConfiguration;
    }
    public ConfigurationEntity getControlEntityConfiguration() {
        return controlEntityConfiguration;
    }
    public void setMainEntityConfiguration(ConfigurationEntity mainEntityConfiguration) {
        this.mainEntityConfiguration = mainEntityConfiguration;
    }
    public void setControlEntityConfiguration(ConfigurationEntity controlEntityConfiguration) {
        this.controlEntityConfiguration = controlEntityConfiguration;
    }
    
    
}
