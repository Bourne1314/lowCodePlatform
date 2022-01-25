package com.csicit.ace.dev;

import com.csicit.ace.common.annotation.AceInitScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.csicit.ace")
@MapperScan({"com.csicit.ace.dev.mapper","com.csicit.ace.*.*.mapper"})
//@EnableFeignClients({"com.csicit.ace"})
@AceInitScan("com.csicit.ace.dev")
public class DevPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevPlatformApplication.class, args);
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");
        System.out.println("----------DevPlatForm-----------------");
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");

    }

}
