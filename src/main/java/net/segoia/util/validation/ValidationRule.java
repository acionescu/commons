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
package net.segoia.util.validation;

import java.util.Map;
/**
 * Used to validate a certain input based on the provided 
 * validation parameters and the internal validator
 * This is used as a wrapper over a certain validator holding also the 
 * validation parameters that will be passed to it when the {@link Validator#validate(Object, Map)}
 * method is called
 * @author adi
 *
 */
public abstract class ValidationRule {
    private Map<String,Object> ruleParameters;
    
    public abstract boolean validate(Object target);
    
    public Map<String, Object> getRuleParameters() {
        return ruleParameters;
    }
   
    public void setRuleParameters(Map<String, Object> ruleParameters) {
        this.ruleParameters = ruleParameters;
    }
}
