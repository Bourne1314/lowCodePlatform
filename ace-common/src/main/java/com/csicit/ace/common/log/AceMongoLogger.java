package com.csicit.ace.common.log;

import com.csicit.ace.common.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/28 11:07
 */
@Component
@ConditionalOnProperty(prefix = "ace", name = "config.openMongoDB", havingValue = "true", matchIfMissing = false)
public class AceMongoLogger implements AceLogger {

    private static Logger logger = LoggerFactory.getLogger(AceMongoLogger.class);

    @Value("${ace.config.localRecord:true}")
    private boolean localRecord;

    @Autowired
    MongoLogger mongoLogger;

    static final int INFO = 1;

    static final int WARN = 2;

    static final int ERROR = 3;

    @Async
    @Override
    public void debug(String info) {
        logger.debug(info);
    }

    @Async
    @Override
    public void info(String info) {
        if (localRecord) {
            logger.info(info);
        }
        mongoLogger.saveLog(info, INFO);
    }

    @Async
    @Override
    public void warn(String info) {
        if (localRecord) {
            logger.warn(info);
        }
        mongoLogger.saveLog(info, WARN);
    }

    @Async
    @Override
    public void error(String info) {
        if (localRecord) {
            logger.error(info);
        }
        mongoLogger.saveLog(info, ERROR);
    }

    @Async
    @Override
    public void error(String info, Exception e) {
        if (localRecord) {
            logger.error(info, e);
        }
        mongoLogger.saveLog(info + "  " + ExceptionUtils.getStackTrace(e), ERROR);
    }

    @Async
    @Override
    public void debug(String info, Class<?> clazz) {
        debug(clazz.getName() + ": " + info);
    }

    @Async
    @Override
    public void info(String info, Class<?> clazz) {
        info(clazz.getName() + ": " + info);
    }

    @Override
    public void warn(String info, Class<?> clazz) {
        warn(clazz.getName() + ":" + info);
    }

    @Async
    @Override
    public void error(String info, Class<?> clazz) {
        error(clazz.getName() + ": " + info);
    }

    @Async
    @Override
    public void error(String info, Exception e, Class<?> clazz) {
        error(clazz.getName() + ": " + info, e);
    }
}
