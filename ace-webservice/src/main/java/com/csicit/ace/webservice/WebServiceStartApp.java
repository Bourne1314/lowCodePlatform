package com.csicit.ace.webservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/5 8:18
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.csicit.ace")
@MapperScan({"com.csicit.ace.webservice.mapper"})
public class WebServiceStartApp {
    public static void main(String[] args) {
        SpringApplication.run(WebServiceStartApp.class, args);
    }
}
