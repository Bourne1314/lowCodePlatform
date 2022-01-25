package com.csicit.ace.zuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/6/29 15:59
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    GlobalInterceptor globalInterceptor;

    @Bean
    public CorsFilter corsFilter() {

        //创建CorsConfiguration对象后添加配置
        CorsConfiguration config = new CorsConfiguration();
        //设置放行哪些原始域
        config.addAllowedOrigin("*");
        //放行哪些原始请求头部信息
        config.addAllowedHeader("*");
        //暴露哪些头部信息
        //放行哪些请求方式
        config.addAllowedMethod("GET");     //get
        config.addAllowedMethod("PUT");     //put
        config.addAllowedMethod("POST");    //post
        config.addAllowedMethod("DELETE");  //delete
        config.addAllowedMethod("*");     //放行全部请求

        //是否发送Cookie
        config.setAllowCredentials(true);

        //2. 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource =
                new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        //返回CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }

    /*
     * 注册静态文件的自定义映射路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    static String[] apps = {"platform", "fileserver", "quartz", "orgauth/orgauth", "push"};


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        String[] patterns = new String[apps.length + 1];
        int count = 1;
        patterns[0] = "/" + appName + "/**";
        for (String app : apps) {
            patterns[count] = "/" + app + "/**";
            count++;
        }
        registry.addInterceptor(globalInterceptor).addPathPatterns(patterns)
                .addPathPatterns("", "/")
                .excludePathPatterns(
                        "/ehcache/**",
                        "/index.html",
                        "/jsp/**",
                        "/js/**",
                        "/css/**",
                        "/img/**",
                        "/static/**",
                        "/fonts/**",
                        "/pages/**"
                );
    }
}
