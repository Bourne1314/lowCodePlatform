package com.csicit.ace.zuul;

import com.csicit.ace.common.annotation.AceInitScan;
import com.csicit.ace.common.constant.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/5/25 15:18
 */
@ServletComponentScan("com.csicit.ace")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableZuulProxy
@ComponentScan("com.csicit.ace")
@AceInitScan("com.csicit.ace.zuul")
public class ZuulStartApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
        Constants.isZuulApp = true;
        SpringApplication.run(ZuulStartApp.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        Constants.isZuulApp = true;
//        return builder.sources(ZuulStartApp.class);
//    }
}
