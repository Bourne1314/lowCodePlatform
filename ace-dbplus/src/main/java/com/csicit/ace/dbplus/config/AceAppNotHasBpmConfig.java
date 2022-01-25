package com.csicit.ace.dbplus.config;

import com.csicit.ace.common.utils.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/7/31 11:21
 */

//@Configuration("aceAppNotHasBpmConfig")
public class AceAppNotHasBpmConfig {//implements ApplicationRunner {
//
//    /**
//     * 应用名称
//     */
//    @Value("${spring.application.name}")
//    private String appName;
//
//    @Autowired
//    CacheUtil cacheUtil;
//
//    @Override
//    public void run(ApplicationArguments args) {
//        cacheUtil.hset("ace-app-use-bpm", appName, 0);
//    }
}
