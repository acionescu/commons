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

public class PreparatorInspectorEntityConfiguration<P,E,I,O> {
    private ExecutionEntity<P, E> preparator;
    private ExecutionEntity<E, I> executor;
    private ExecutionEntity<I, O> inspector;
    
    public ExecutionEntity<P, E> getPreparator() {
        return preparator;
    }
    public ExecutionEntity<E, I> getExecutor() {
        return executor;
    }
    public ExecutionEntity<I, O> getInspector() {
        return inspector;
    }
    public void setPreparator(ExecutionEntity<P, E> preparator) {
        this.preparator = preparator;
    }
    public void setExecutor(ExecutionEntity<E, I> executor) {
        this.executor = executor;
    }
    public void setInspector(ExecutionEntity<I, O> inspector) {
        this.inspector = inspector;
    }
    
    
}
