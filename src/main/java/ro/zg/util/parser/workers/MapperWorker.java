package ro.zg.util.parser.workers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MapperWorker extends ParseWorker {

    public MapperWorker(Map<Object, Object> params) {
	super(params);
	// TODO Auto-generated constructor stub
    }

    @Override
    public Object process(Collection<Object> input) {
	Map<Object, Object> params = getParams();

	List<Object> result = new ArrayList<Object>();

	for (Object o : input) {
	    Object value = params.get(o);
	    if (value != null) {
		result.add(value);
	    }
	    else {
		result.add(o);
	    }
	}
	
	return result;
    }

}
