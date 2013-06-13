package ro.zg.util.parser.workers;

import java.util.Collection;
import java.util.Map;

public abstract class ParseWorker {
    private Map<Object,Object> params;

    public ParseWorker(Map<Object, Object> params) {
	super();
	this.params = params;
    }



    public abstract Object process(Collection<Object> input);


    /**
     * @return the params
     */
    public Map<Object, Object> getParams() {
        return params;
    }
    
    
}
