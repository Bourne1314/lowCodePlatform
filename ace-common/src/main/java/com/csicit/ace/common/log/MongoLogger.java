package com.csicit.ace.common.log;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/28 11:10
 */
@ConditionalOnProperty(prefix = "ace", name = "config.openMongoDB", havingValue = "true", matchIfMissing = false)
@Component
public class MongoLogger {

    private static Logger logger = LoggerFactory.getLogger(MongoLogger.class);

    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");

    @Autowired
    MongoTemplate mongoTemplate;

    @Value("${spring.application.name}")
    private String appName;


    @Async
    public void saveLog(String info, int level) {
        try {
            mongoTemplate.insert(new Document("info", info)
                    .append("level", level)
                    .append("time", df.format(LocalDateTime.now())).append("times",
                            System.currentTimeMillis()), appName + "-" + LocalDate.now());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

}
