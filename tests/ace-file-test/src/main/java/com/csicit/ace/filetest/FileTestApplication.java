package com.csicit.ace.filetest;

import com.csicit.ace.common.annotation.AceInitScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@AceInitScan("com.csicit.ace.filetest")
//@EnableFeignClients("com.csicit.ace")
@ComponentScan({"com.csicit.ace"})
public class FileTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileTestApplication.class, args);
    }
}