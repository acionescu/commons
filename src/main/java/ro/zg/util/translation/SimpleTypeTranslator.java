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
package ro.zg.util.translation;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.util.data.reflection.ReflectionUtility;
import ro.zg.util.validation.ParameterValidationUtil;

public class SimpleTypeTranslator implements Translator {

    public Object translate(String input, String type) throws ContextAwareException {
	ParameterValidationUtil.validateNotNull(input, "'input' parameter cannot be null");
	ParameterValidationUtil.validateNotNull(type, "'type' parameter cannot be null");
	
	return ReflectionUtility.createObjectByTypeAndValue(type, input);
    }
}
