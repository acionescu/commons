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
package net.segoia.util.parser;

import java.util.HashMap;
import java.util.Map;

import net.segoia.util.parser.event.ConfigurableParseEventHandler;

public class ParseManager {
    private Map<ParserConfig, Parser> parsers = new HashMap<ParserConfig, Parser>();

    public ParseResponse parse(String input, ParserConfig parserConfig) throws ParserException {
	Parser p = parsers.get(parserConfig);
	if (p == null) {
	    p = new Parser();
	    p.addSymbols(parserConfig.getSymbols());
	    p.getParseContextConfig().getNestedSymbols().setCaseInsensitive(parserConfig.isCaseInsensitive());
	    ParseEventHandlerConfig parseEventHandlerConfig = parserConfig.getParseEventHandlerConfig();
	    if (parseEventHandlerConfig != null) {
		p.setHandlerFactory(new ParserHandlerFactory(new ConfigurableParseEventHandler(parseEventHandlerConfig)));
	    }
	    parsers.put(parserConfig, p);
	}
	return p.parse(input);
    }
}
