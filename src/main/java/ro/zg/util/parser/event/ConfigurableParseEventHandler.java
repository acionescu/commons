package ro.zg.util.parser.event;

import ro.zg.util.parser.ParseEventHandlerConfig;
import ro.zg.util.strings.StringUtil;

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
