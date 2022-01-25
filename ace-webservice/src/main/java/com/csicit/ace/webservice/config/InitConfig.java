package com.csicit.ace.webservice.config;

import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/7 17:01
 */
@Component
@Order(-1)
public class InitConfig implements ApplicationRunner {

    /**
     * redis访问工具类对象
     */
    @Autowired
    RedisUtils redisUtils;

    @Override
    public void run(ApplicationArguments args) {
        try {
            GMBaseUtil.initSmKeyValue(redisUtils);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
