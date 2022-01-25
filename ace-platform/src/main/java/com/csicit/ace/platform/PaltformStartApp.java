package com.csicit.ace.platform;

import com.csicit.ace.common.annotation.AceInitScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 平台启动类
 * 启用服务发现
 * 启用feign接口
 *
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.csicit.ace")
@AceInitScan("com.csicit.ace.platform")
public class PaltformStartApp {
    public static void main(String[] args) {
        SpringApplication.run(PaltformStartApp.class, args);
        System.out.println("//                                                    __----~~~~~~~~~~~------___" );
        System.out.println("//                                   .  .   ~~//====......          __--~ ~~" );
        System.out.println("//                   -.            \\_|//     |||\\\\  ~~~~~~::::... /~" );
        System.out.println("//        __---~~~.==~||\\=_    -_--~/_-~|-   |\\\\   \\\\        _/~" );
        System.out.println("//        __---~~~.==~||\\=_    -_--~/_-~|-   |\\\\   \\\\        _/~" );
        System.out.println("//    _-~~     .=~    |  \\\\-_    '-~7  /-   /  ||    \\      /" );
        System.out.println("//  .~       .~       |   \\\\ -_    /  /-   /   ||      \\   /" );
        System.out.println("// /  ____  /         |     \\\\ ~-_/  /|- _/   .||       \\ /" );
        System.out.println("// |~~    ~~|--~~~~--_ \\     ~==-/   | \\~--===~~        .\\" );
        System.out.println("//          '         ~-|      /|    |-~\\~~       __--~~" );
        System.out.println("//                      |-~~-_/ |    |   ~\\_   _-~            /\\" );
        System.out.println("//                           /  \\     \\__   \\/~                \\__" );
        System.out.println("//                       _--~ _/ | .-~~____--~-/                  ~~==." );
        System.out.println("//                      ((->/~   '.|||' -_|    ~~-/ ,              . _||" );
        System.out.println("//                                 -_     ~\\      ~~---l__i__i__i--~~_/" );
        System.out.println("//                                 _-~-__   ~)  \\--______________--~~" );
        System.out.println("//                               //.-~~~-~_--~- |-------~~~~~~~~" );
        System.out.println("//                                      //.-~~~--" );
    }

}
