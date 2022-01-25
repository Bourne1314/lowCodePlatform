package com.csicit.ace.baseimpl;

import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author JonnyJiang
 * @date 2020/7/9 11:32
 */
public class BaseImpl {
    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected CacheUtil cacheUtil;

    @Value("${spring.application.name}")
    protected String appName;
}
