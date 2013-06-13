package ro.zg.util.parser.workers;

import java.util.HashMap;
import java.util.Map;

import ro.zg.util.parser.ParserException;

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
