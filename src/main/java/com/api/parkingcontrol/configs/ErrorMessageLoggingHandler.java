package com.api.parkingcontrol.configs;

import com.api.parkingcontrol.configs.Logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.HashMap;
import java.util.Map;

public final class ErrorMessageLoggingHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, Level> classAndLogLevelMap = new HashMap<>() {{
        put(NoSuchBeanDefinitionException.class.getName(), Level.ERROR);
    }};

    public void perform(Exception exception) {
        Class<? extends Exception> exceptionClass = exception.getClass();
        Level logLevel = (exceptionClass.isAnnotationPresent(Logging.class))
                ? exceptionClass.getAnnotation(Logging.class).level()
                : classAndLogLevelMap.get(exceptionClass.getName());

        if (logLevel == null){
            if (logger.isWarnEnabled()) {
                logger.warn(exception.getClass().getName() + " is not yet " +
                        "specify logging level");
            }
            return;
        }
        if (logLevel == Level.NONE) {
            return;
        }

        String logMessage = exception.getMessage();
        switch (logLevel) {
            case INFO:
                if (logger.isInfoEnabled()) {
                    logger.info(logMessage, exception);
                }
                break;
            case ERROR:
                if (logger.isErrorEnabled()) {
                    logger.error(logMessage, exception);
                }
                break;
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    logger.debug(logMessage, exception);
                }
                break;
            case WARN:
                if (logger.isWarnEnabled()) {
                    logger.warn(logMessage, exception);
                }
                break;
            case TRACE:
                if (logger.isTraceEnabled()) {
                    logger.trace(logMessage, exception);
                }
                break;
        }
    }
}
