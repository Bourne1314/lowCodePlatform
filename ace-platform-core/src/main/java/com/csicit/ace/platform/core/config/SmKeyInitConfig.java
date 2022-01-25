package com.csicit.ace.platform.core.config;

import com.csicit.ace.data.persistent.config.MonoAppStartupConfigImpl;
import com.csicit.ace.data.persistent.config.PlatformInitKeysUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/7/10 16:57
 */
// 单机版不执行此方法
@ConditionalOnMissingBean(MonoAppStartupConfigImpl.class)
@Configuration
@Order(-10)
public class SmKeyInitConfig implements ApplicationRunner {

    @Autowired
    PlatformInitKeysUtil platformInitKeysUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 加载 smkey
        platformInitKeysUtil.initSmKeyValue();
    }
}
