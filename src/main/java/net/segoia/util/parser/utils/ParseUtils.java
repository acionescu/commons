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
package net.segoia.util.parser.utils;

import net.segoia.util.parser.ParserException;

public class ParseUtils {
    private static JavaCollectionMapParser collectionsParser = new JavaCollectionMapParser();
    
    
    
    public static Object parseCollection(String input) throws ParserException {
	return collectionsParser.parse(input);
    }
    
    public static void main(String[] args) throws ParserException {
	System.out.println("out="+parseCollection("[{name=username,is-required=true,is-form-field=true,field-input-regex=\"^[\\w]{5,20}$\"},{name=password,is-required=true,is-form-field=true,is-sensitive=true,field-input-regex=\"^[\\w]{5,20}$\"},{name=password-again,is-required=true,is-sensitive=true,is-form-field=true,field-input-regex=\"^[\\w]{5,20}$\"},{name=email,is-required=true,is-form-field=true,field-input-regex=\"(\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,3})\"}]"));
    }
}
