package com.csicit.ace.zuul.service;

import com.csicit.ace.zuul.feign.ClientService;
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
    ClientService clientService;
}
