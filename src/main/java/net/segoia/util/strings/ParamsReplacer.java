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
package net.segoia.util.strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamsReplacer {
    private Pattern pattern;
    
    public ParamsReplacer(String regex){
	pattern = Pattern.compile(regex);
    }
    
    public String replace(String target, MatchHandler handler){
	Matcher m = pattern.matcher(target);
	String resp = target;
	while (m.find()) {
	    String group = m.group();
	    String prop = group.replace("(", "");
	    prop = prop.replace(")", "");
	    Object value = handler.onMatch(prop);
	    if(value == null){
		return null;
	    }
	    resp = resp.replace(group, value.toString());
	}
	return resp;
    }
}
