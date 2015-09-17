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
package net.segoia.util.translation;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.util.data.reflection.ReflectionUtility;
import net.segoia.util.validation.ParameterValidationUtil;

public class SimpleTypeTranslator implements Translator {

    public Object translate(String input, String type) throws ContextAwareException {
	ParameterValidationUtil.validateNotNull(input, "'input' parameter cannot be null");
	ParameterValidationUtil.validateNotNull(type, "'type' parameter cannot be null");
	
	return ReflectionUtility.createObjectByTypeAndValue(type, input);
    }
}
