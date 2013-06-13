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
package ro.zg.util.execution.test;

import ro.zg.util.execution.ExecutionEntity;

public class ControlExecutionEntity<I,O,C> implements ContextExecutionEntity<ExecutionContext<I,ControlExecutionEntityConfiguration>, C>{

    public C execute(ExecutionContext<I, ControlExecutionEntityConfiguration> executionContext) throws Exception {
	I input = executionContext.getInput();
	ExecutionEntity mainExecutionEntity = executionContext.getConfig().getMainExecutionEntity();
	ExecutionEntity controlExecutionEntity = executionContext.getConfig().getControlExecutionEntity();
	ConfigurationEntity mainEntityConf = executionContext.getConfig().getMainEntityConfiguration();
	ConfigurationEntity controlEntityConf = executionContext.getConfig().getControlEntityConfiguration();
	
	O output = (O)mainExecutionEntity.execute(new ExecutionContext(input,mainEntityConf));
	C control = (C)controlExecutionEntity.execute(new ExecutionContext(new IOPair<I, O>(input, output),controlEntityConf));
	
	return control;
    }

}
