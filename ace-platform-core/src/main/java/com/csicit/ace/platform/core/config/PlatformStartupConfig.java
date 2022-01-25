package com.csicit.ace.platform.core.config;

import com.csicit.ace.platform.core.service.SysAuthApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/6/23 9:53
 */
@Configuration
@Order(100)
public class PlatformStartupConfig implements ApplicationRunner {

    @Autowired
    SysAuthApiService sysAuthApiService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        sysAuthApiService.initApis();
    }
}
