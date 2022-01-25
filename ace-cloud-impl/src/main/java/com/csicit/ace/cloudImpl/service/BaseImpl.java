package com.csicit.ace.cloudImpl.service;

import com.csicit.ace.cloudImpl.feign.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/3/10 17:38
 */
@Component
public class BaseImpl extends com.csicit.ace.baseimpl.BaseImpl {
    @Autowired
    GatewayService gatewayService;
}
