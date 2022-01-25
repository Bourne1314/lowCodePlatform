package com.csicit.ace.bpmtest;

import com.csicit.ace.common.annotation.AceInitScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 程序入口
 *
 * @author JonnyJiang
 * @date 2019/10/12 11:54
 */
@EnableTransactionManagement
@EnableDiscoveryClient
@SpringBootApplication
//@EnableFeignClients("com.csicit.ace")
@ComponentScan({"com.csicit.ace"})
@MapperScan({"com.csicit.ace.dbplus.mybatis.mapper", "com.csicit.ace.bpm.mapper"})
@AceInitScan("com.csicit.ace.bpmtest")
public class BpmTestApplication {



    public static void main(String[] args) {
        SpringApplication.run(BpmTestApplication.class, args);
//        RedisUtils redisUtils = SpringContextUtils.getBean(RedisUtils.class);
//        Test test = new Test(redisUtils);
//        test.start();
    }


}

//
//class Test extends Thread {
//
//    RedisUtils redisUtils;
//
//    public Test() {
//    }
//
//    public Test(RedisUtils redisUtils) {
//        this.redisUtils = redisUtils;
//    }
//
//    @Override
//    public void run() {
//        try {
//            sleep(40000);
//            System.out.println(redisUtils.get("platformName"));
//            System.out.println(1111);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}