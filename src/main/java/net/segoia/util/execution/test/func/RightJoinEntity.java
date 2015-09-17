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
/**
 * Returns JoinGroup(x,f(x))
 * where f - {@link ExecutionEntity}
 * x - input
 * @author adi
 *
 * @param <I>
 * @param <O>
 */
public class RightJoinEntity<I,O> implements ContextExecutionEntity<ExecutionContext<I,ExecutionEntity<I, O>>, JoinGroup<I, O>>{

    public JoinGroup<I, O> execute(ExecutionContext<I, ExecutionEntity<I, O>> executionContext) throws Exception {
	I input = executionContext.getInput();
	return new JoinGroup<I, O>(input,executionContext.getConfig().execute(input));
    }

}
