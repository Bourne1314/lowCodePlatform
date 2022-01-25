package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.common.config.ZuulRouteConfig;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @Description TODO
 * @Author JR-zhangzhaojun
 * @DATE 2021/10/13
 * @Param
 * @return
 * @Version 1.0
 */
@RestController
@Api("初始化扫描数据处理")
@RequestMapping("/orgauth/getRouteConfig")
public class GetRouteControllerO {
    @Autowired
    ZuulRouteConfig zuulRouteConfig;

    @RequestMapping(method = RequestMethod.GET)
    Set<String> getRouteConfig(){
        return zuulRouteConfig.getRoutes().keySet();
    }
}
