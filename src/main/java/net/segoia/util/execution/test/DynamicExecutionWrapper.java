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

import net.segoia.util.data.GenericNameValueContext;

public class DynamicExecutionWrapper<O> extends DynamicExecutionEntity<O>{
    private DynamicExecutionWrapperConfiguration<O> config;

    public O execute(GenericNameValueContext input) throws Exception {
	GenericNameValueContext selfContext = new GenericNameValueContext();
	selfContext.put("global-context",input);
	selfContext.put("static-input",config.getStaticContext());
	selfContext.put("dynamic-input",config.getDynamicParameters());
	selfContext.put("executor",config.getExecutor());
	
	O result = super.execute(selfContext);
	return result;
    }

    public DynamicExecutionWrapperConfiguration<O> getConfig() {
        return config;
    }

    public void setConfig(DynamicExecutionWrapperConfiguration<O> config) {
        this.config = config;
    }
}
