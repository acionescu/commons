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
package net.segoia.util.data;

import com.google.gson.Gson;

import net.segoia.util.formatting.FormatUtil;

public class GenericNameValueList extends GenericNameValueContext{

    /**
     * 
     */
    private static final long serialVersionUID = -6837055493644571920L;
    
    
    public void addValue(Object value) {
	int nextPos = getParameters().size();
	put(""+nextPos,value);
    }
    
    public Object getValueForIndex(int index) {
	return getValue(""+index);
    }
    
    public Object removeValueForIndex(int index) {
	return remove(""+index).getValue();
    }

    public String toJsonString() {
	return new Gson().toJson(getNameValuesAsMap().values());
    }
    
    public String format(String separator,String itemEncloser,String start, String end) {
	return FormatUtil.formatCollection(getNameValuesAsMap().values(), separator, itemEncloser, start, end);
    }
    
}
