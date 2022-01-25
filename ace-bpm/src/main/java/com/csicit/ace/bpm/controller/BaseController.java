package com.csicit.ace.bpm.controller;

import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/16 10:23
 */
public class BaseController {

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    WfdFlowService wfdFlowService;
}