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
package net.segoia.util.parser.event;

import net.segoia.util.parser.ParseEventHandlerConfig;
import net.segoia.util.strings.StringUtil;

public class ConfigurableParseEventHandler extends DefaultParseEventHandler {
    private ParseEventHandlerConfig config;

    public ConfigurableParseEventHandler() {
	super();
    }

    public ConfigurableParseEventHandler(ParseEventHandlerConfig config) {
	super();
	this.config = config;
    }

    public Object handleEmptyString(String content) {
	return escapeString(content);
    }

    private String escapeString(String input) {
	
	return StringUtil.escapeString(input,  config.getOutputCharsToBeEscaped(),config.getOutputEscapeChar());
    }
    
   

    /**
     * @return the config
     */
    public ParseEventHandlerConfig getConfig() {
	return config;
    }

    /**
     * @param config
     *            the config to set
     */
    public void setConfig(ParseEventHandlerConfig config) {
	this.config = config;
    }

}
