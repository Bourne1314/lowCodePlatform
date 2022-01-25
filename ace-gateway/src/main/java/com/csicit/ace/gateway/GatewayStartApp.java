package com.csicit.ace.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: yansiyang
 * @Descruption:
 * @Date: Created in 8:12 2019/4/1
 * @Modified By:
 */
@SpringBootApplication
@ComponentScan("com.csicit.ace")
@EnableDiscoveryClient
@EnableFeignClients
public class GatewayStartApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayStartApp.class);
        System.out.println("//                                                    __----~~~~~~~~~~~------___");
        System.out.println("//                                   .  .   ~~//====......          __--~ ~~");
        System.out.println("//                   -.            \\_|//     |||\\\\  ~~~~~~::::... /~");
        System.out.println("//        __---~~~.==~||\\=_    -_--~/_-~|-   |\\\\   \\\\        _/~");
        System.out.println("//        __---~~~.==~||\\=_    -_--~/_-~|-   |\\\\   \\\\        _/~");
        System.out.println("//    _-~~     .=~    |  \\\\-_    '-~7  /-   /  ||    \\      /");
        System.out.println("//  .~       .~       |   \\\\ -_    /  /-   /   ||      \\   /");
        System.out.println("// /  ____  /         |     \\\\ ~-_/  /|- _/   .||       \\ /");
        System.out.println("// |~~    ~~|--~~~~--_ \\     ~==-/   | \\~--===~~        .\\");
        System.out.println("//          '         ~-|      /|    |-~\\~~       __--~~");
        System.out.println("//                      |-~~-_/ |    |   ~\\_   _-~            /\\");
        System.out.println("//                           /  \\     \\__   \\/~                \\__");
        System.out.println("//                       _--~ _/ | .-~~____--~-/                  ~~==.");
        System.out.println("//                      ((->/~   '.|||' -_|    ~~-/ ,              . _||");
        System.out.println("//                                 -_     ~\\      ~~---l__i__i__i--~~_/");
        System.out.println("//                                 _-~-__   ~)  \\--______________--~~");
        System.out.println("//                               //.-~~~-~_--~- |-------~~~~~~~~");
        System.out.println("//                                      //.-~~~--");
    }
}
