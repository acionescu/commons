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

public class ParameterValidationUtil {
    private static SimpleTypeValidator simpleTypeValidator = new SimpleTypeValidator();
    /* validate not null */
    public static boolean validateNotNull(Object input) {
	return (input != null);
    }

    public static void validateNotNull(Object input, String message) {
	if (!ParameterValidationUtil.validateNotNull(input)) {
	    throw new IllegalArgumentException(message);
	}
    }
    
    
    /* validate type */
    
    public static boolean validateSimpleType(Object input, String type){
	if("Any".equals(type)) {
	    return true;
	}
	return simpleTypeValidator.validate(input, type);
    }
    
    public static void validateSimpleType(Object input, String type,String message){
	if (!simpleTypeValidator.validate(input, type)){
	    throw new IllegalArgumentException(message);
	}
    }
}
