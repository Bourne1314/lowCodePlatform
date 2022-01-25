package com.csicit.ace.zuul.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/8/9 10:46
 */
@ConditionalOnExpression("!'${ace.config.cache.type:redis}'.equals('redis')")
@Configuration
@EnableAutoConfiguration(exclude = RedisAutoConfiguration.class)
public class RedisStartConfig {
}
