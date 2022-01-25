package com.csicit.ace.common.log;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/13 9:02
 */
@Order(-2)
@ConditionalOnProperty(prefix = "ace", name = "config.openMongoDB", havingValue = "false", matchIfMissing = true)
@Configuration
@EnableAutoConfiguration(exclude = {MongoDataAutoConfiguration.class, MongoAutoConfiguration.class})
public class MongoConfig {
}
