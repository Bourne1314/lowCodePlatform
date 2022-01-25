package com.csicit.ace.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/6/1 14:17
 */
@Data
@Configuration
@ConfigurationProperties("zuul")
public class ZuulRouteConfig {
    private Map<String, Object> routes;
}
