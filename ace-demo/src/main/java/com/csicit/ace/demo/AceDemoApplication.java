package com.csicit.ace.demo;

import com.csicit.ace.common.annotation.AceInitScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.csicit.ace")
@EnableFeignClients({"com.csicit"})
@ServletComponentScan("com.csicit")
@MapperScan({"com.csicit.ace.demo.mapper","com.csicit.ace.dbplus.mybatis.mapper"})
@AceInitScan("com.csicit.ace.demo")
public class AceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AceDemoApplication.class, args);
    }

}
