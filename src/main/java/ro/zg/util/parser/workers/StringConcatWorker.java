package ro.zg.util.parser.workers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class StringConcatWorker extends ParseWorker{
    
    

    public StringConcatWorker(Map<Object, Object> params) {
	super(params);
    }

    @Override
    public Object process(Collection<Object> input) {
	if(input.size() <= 1) {
	    return input;
	}
	StringBuffer sb = new StringBuffer();
	for(Object o : input) {
	    if(o instanceof String) {
		sb.append((String)o);
	    }
	    else {
		return input;
	    }
	}
	List<Object> result = new ArrayList<Object>();
	result.add(sb.toString());
	return result;
    }

}
