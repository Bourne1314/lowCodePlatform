package com.csicit.ace.zuul.feign;

import com.csicit.ace.common.utils.system.SecurityUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/6/17 10:02
 */
@EnableFeignClients("com.csicit.ace.zuul.feign")
@Configuration
public class FeignConfig implements RequestInterceptor {

    @Autowired
    SecurityUtils securityUtils;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //添加token
        try {
            requestTemplate.header("token", securityUtils.getToken());
            requestTemplate.header("realip", securityUtils.getRealIp());
            requestTemplate.header("Cookie", "JSESSIONID=" + securityUtils.getSession().getId());
            requestTemplate.header("JSESSIONID", securityUtils.getSession().getId());
        } catch (Exception e) {

        }
    }
}
