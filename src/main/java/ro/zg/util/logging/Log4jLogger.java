package ro.zg.util.logging;

public class Log4jLogger implements Logger{
    private org.apache.log4j.Logger log4jLogger;

    public Log4jLogger(org.apache.log4j.Logger log4jLogger) {
	super();
	this.log4jLogger = log4jLogger;
    }

    @Override
    public void debug(Object message, Throwable t) {
	log4jLogger.debug(message, t);
    }

    @Override
    public void info(Object message, Throwable t) {
	log4jLogger.info(message, t);
	
    }

    @Override
    public void warn(Object message, Throwable t) {
	log4jLogger.warn(message, t);
	
    }

    @Override
    public void error(Object message, Throwable t) {
	log4jLogger.error(message, t);
	
    }

    @Override
    public void fatal(Object message, Throwable t) {
	log4jLogger.fatal(message, t);
    }

    @Override
    public void debug(Object message) {
	log4jLogger.debug(message);
    }

    @Override
    public void info(Object message) {
	log4jLogger.debug(message);
    }

    @Override
    public void warn(Object message) {
	log4jLogger.warn(message);
    }

    @Override
    public void error(Object message) {
	log4jLogger.error(message);
    }

    @Override
    public void fatal(Object message) {
	log4jLogger.fatal(message);
    }

    @Override
    public void append(Object message) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled() {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDebugEnabled() {
	return log4jLogger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
	return log4jLogger.isInfoEnabled();
    }

}
