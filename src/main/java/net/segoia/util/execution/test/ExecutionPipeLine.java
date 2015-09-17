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

import java.util.List;

import net.segoia.util.execution.ExecutionEntity;

public class ExecutionPipeLine<I,O> implements ContextExecutionEntity<ExecutionContext<I,ExecutionPipeLineConfiguration>, O>{

    public O execute(ExecutionContext<I, ExecutionPipeLineConfiguration> executionContext) throws Exception {
	List<ExecutionEntity> executionLine = executionContext.getConfig().getExecutionLine();
	Object currentInput = executionContext.getInput();
	for(ExecutionEntity entity : executionLine){
	    currentInput = entity.execute(currentInput);
	}
	return (O)currentInput;
    }

}
