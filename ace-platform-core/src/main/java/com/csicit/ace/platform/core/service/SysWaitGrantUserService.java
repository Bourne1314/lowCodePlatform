package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysWaitGrantUserDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色用户授予—待激活的用户表 实例对象访问接口
 *
 * @author generator
 * @date 2019-12-12 15:06:28
 * @version V1.0
 */
@Transactional
public interface SysWaitGrantUserService extends IBaseService<SysWaitGrantUserDO> {
    /**
     * 添加带激活的用户角色授予的用户表
     *
     * @param appId
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:50
     */
    boolean saveSysWaitGrantUser(String appId, String userId);
}
