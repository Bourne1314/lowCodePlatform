package com.csicit.ace.dev.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

/**
 * @author zuogang
 * @return
 * @date 2020/3/3 15:50
 */
@Configuration
@Order(10000)
public class LiquibaseConfig {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        liquibase.setContexts("development,test,production");
        liquibase.setDropFirst(false);
        //如果设置为true：第一次执行不会报错，第二次将会报错，导致程序无法启动，所以第一次执行完后一定要改为：false
        liquibase.setShouldRun(false);
        return liquibase;
    }

}
