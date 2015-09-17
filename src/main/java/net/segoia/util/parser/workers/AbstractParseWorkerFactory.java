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
package net.segoia.util.parser.workers;

import java.util.HashMap;
import java.util.Map;

import net.segoia.util.parser.ParserException;

public class AbstractParseWorkerFactory {
    private static Map<String,ParseWorkerFactory> factories;
    public static final String MAPPER="MAPPER";
    public static final String STRING_CONCAT="STRING_CONCAT";
    
    static {
	factories = new HashMap<String, ParseWorkerFactory>();
	factories.put(MAPPER, new ParseWorkerFactory() {
	    
	    @Override
	    public ParseWorker createWorker(Map<Object, Object> params) {
		return new MapperWorker(params);
	    }
	});
	
	factories.put(STRING_CONCAT, new ParseWorkerFactory() {
	    
	    @Override
	    public ParseWorker createWorker(Map<Object, Object> params) {
		return new StringConcatWorker(params);
	    }
	});
    }

    public static ParseWorker createWorker(String type, Map<Object,Object> params) throws ParserException {
	ParseWorkerFactory factory = factories.get(type);
	if(factory != null) {
	    return factory.createWorker(params);
	}
	
	throw new ParserException("No factory found for worker of type "+type);
    }
    
}
