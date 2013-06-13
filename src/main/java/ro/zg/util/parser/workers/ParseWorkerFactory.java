package ro.zg.util.parser.workers;

import java.util.Map;

public interface ParseWorkerFactory {

    ParseWorker createWorker(Map<Object,Object> params);
}
