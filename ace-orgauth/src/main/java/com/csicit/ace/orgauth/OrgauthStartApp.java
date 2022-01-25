package com.csicit.ace.orgauth;

import com.csicit.ace.common.annotation.AceInitScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/1 9:06
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.csicit.ace")
@EnableFeignClients({"com.csicit.ace"})
@MapperScan({"com.csicit.ace.data.persistent.mapper","com.csicit.ace.dbplus.mybatis.mapper"})
@AceInitScan("com.csicit.ace.orgauth")
public class OrgauthStartApp {
    public static void main(String[] args){
        SpringApplication.run(OrgauthStartApp.class);
    }
}
