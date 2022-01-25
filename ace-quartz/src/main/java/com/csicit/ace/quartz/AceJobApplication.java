package com.csicit.ace.quartz;

import com.csicit.ace.common.annotation.AceInitScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.csicit.ace")
@MapperScan({"com.csicit.ace.data.persistent.mapper", "com.csicit.ace.dbplus.mybatis.mapper"})
@AceInitScan({"com.csicit.ace.quartz"})
public class AceJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(AceJobApplication.class, args);
    }

}
