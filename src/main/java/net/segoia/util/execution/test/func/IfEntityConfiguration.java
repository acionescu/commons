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

public class IfEntityConfiguration<I,O> {
    private ExecutionEntity<I, Boolean> condition;
    private ExecutionEntity<I, O> yesAction;
    private ExecutionEntity<I, O> noAction;
    public ExecutionEntity<I, Boolean> getCondition() {
        return condition;
    }
    public ExecutionEntity<I, O> getYesAction() {
        return yesAction;
    }
    public ExecutionEntity<I, O> getNoAction() {
        return noAction;
    }
    public void setCondition(ExecutionEntity<I, Boolean> condition) {
        this.condition = condition;
    }
    public void setYesAction(ExecutionEntity<I, O> yesAction) {
        this.yesAction = yesAction;
    }
    public void setNoAction(ExecutionEntity<I, O> noAction) {
        this.noAction = noAction;
    }
    
    
}
