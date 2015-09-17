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
 * returns i(e(p(x)))
 * where p - preparator
 * e - executor
 * i - inspector
 * x - the input
 * 
 * 
 * @author adi
 *
 * @param <P> - raw input
 * @param <E> - prepared input
 * @param <I> - raw output
 * @param <O> - final output
 */
public class PreparatorInspectorEntity<P, E, I, O> implements
	ContextExecutionEntity<ExecutionContext<P, PreparatorInspectorEntityConfiguration<P, E, I, O>>, O> {

    public O execute(ExecutionContext<P, PreparatorInspectorEntityConfiguration<P, E, I, O>> executionContext)
	    throws Exception {
	PreparatorInspectorEntityConfiguration<P, E, I, O> config = executionContext.getConfig();
	P rawInput = executionContext.getInput();

	ExecutionEntity<P, E> preparator = config.getPreparator();
	ExecutionEntity<E, I> executor = config.getExecutor();
	ExecutionEntity<I, O> inspector = config.getInspector();
	
	return inspector.execute(executor.execute(preparator.execute(rawInput)));
    }

}
