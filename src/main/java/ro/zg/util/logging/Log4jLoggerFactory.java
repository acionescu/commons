package ro.zg.util.logging;

public class Log4jLoggerFactory implements LoggerFactory{

    @Override
    public Logger getLogger(String name) {
	return new Log4jLogger(org.apache.log4j.Logger.getLogger(name));
    }

}
