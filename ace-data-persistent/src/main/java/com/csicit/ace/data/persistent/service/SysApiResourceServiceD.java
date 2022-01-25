package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.AppUpgradeJaxb.ApiResourceDetail;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 11:08
 */
@Transactional
public interface SysApiResourceServiceD extends IService<SysApiResourceDO> {
    /**
     * 初始化平台所有注解"AceAuth"API资源
     *
     * @param pkgName 顶级包名
     * @author shanwj
     * @date 2019/4/16 16:53
     */
    void initApiResource(String pkgName);

    /**
     * 平台运行时保存api资源
     *
     * @param apis
     * @param appId
     * @return
     */
    boolean saveApis(List<SysApiResourceDO> apis, String appId);

    /**
     * 应用升级时，API更新
     *
     * @param apiResourceDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 14:33
     */
    boolean apiResourceUpdate(List<ApiResourceDetail> apiResourceDetails, String appId);
}
