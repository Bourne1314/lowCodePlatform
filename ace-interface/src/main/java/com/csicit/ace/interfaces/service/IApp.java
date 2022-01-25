package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.domain.SysGroupAppDO;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/3/5 14:31
 */
public interface IApp {

    /**
     * 获取当前应用信息
     *
     */
    SysGroupAppDO getCurrentApp();

    /**
     * 获取应用信息
     *
     * @param appId     app主键
     */
    SysGroupAppDO getAppById(String appId);
}
