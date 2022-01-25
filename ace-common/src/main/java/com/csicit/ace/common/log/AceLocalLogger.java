package com.csicit.ace.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/28 11:07
 */
@ConditionalOnMissingBean(AceMongoLogger.class)
@ConditionalOnProperty(prefix = "ace", name = "config.openMongoDB", havingValue = "false", matchIfMissing = true)
@Component
public class AceLocalLogger implements AceLogger {

    private static Logger logger = LoggerFactory.getLogger(AceLocalLogger.class);

    @Async
    @Override
    public void debug(String info) {
        logger.debug(info);
    }

    @Async
    @Override
    public void info(String info) {
        logger.info(info);
    }

    @Async
    @Override
    public void warn(String info) {
        logger.warn(info);
    }

    @Async
    @Override
    public void error(String info) {
        logger.error(info);
    }

    @Async
    @Override
    public void error(String info, Exception e) {
        logger.error(info, e);
    }

    @Async
    @Override
    public void debug(String info, Class<?> clazz) {
        Logger loggerT = LoggerFactory.getLogger(clazz);
        loggerT.debug(info);
    }

    @Async
    @Override
    public void info(String info, Class<?> clazz) {
        Logger loggerT = LoggerFactory.getLogger(clazz);
        loggerT.info(info);
    }

    @Async
    @Override
    public void warn(String info, Class<?> clazz) {
        Logger loggerT = LoggerFactory.getLogger(clazz);
        loggerT.warn(info);
    }

    @Async
    @Override
    public void error(String info, Class<?> clazz) {
        Logger loggerT = LoggerFactory.getLogger(clazz);
        loggerT.error(info);
    }

    @Async
    @Override
    public void error(String info, Exception e, Class<?> clazz) {
        Logger loggerT = LoggerFactory.getLogger(clazz);
        loggerT.error(info, e);
    }
}
