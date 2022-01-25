package com.csicit.ace.cloudImpl.config;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.system.SecurityUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/6/17 10:02
 */

@EnableFeignClients({"com.csicit.ace"})
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
        } catch (Exception e) {
            // 加密应用名 + 时间， 网关解密，解密失败算无效
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appName", securityUtils.getAppName());
            jsonObject.put("time",LocalDateTime.now());
            try {
                String key = GMBaseUtil.encryptString(jsonObject.toJSONString());
                requestTemplate.header("aceFeignKey", key);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RException(ex.getMessage());
            }

        }
        //}
    }
}
