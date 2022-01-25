package com.csicit.ace.fileserver;

import com.csicit.ace.common.annotation.AceInitScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@EnableTransactionManagement
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.csicit.ace")
@MapperScan({"com.csicit.ace.fileserver.core.mapper"})
@AceInitScan("com.csicit.ace.fileserver")
public class FileServer {
    public static void main(String[] args) {
        SpringApplication.run(FileServer.class, args);
    }
}
