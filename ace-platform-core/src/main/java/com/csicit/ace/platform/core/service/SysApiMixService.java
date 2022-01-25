package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysApiMixDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 有效API权限 实例对象访问接口
 *
 * @author generator
 * @date 2019-04-15 20:05:05
 * @version V1.0
 */
@Transactional
public interface SysApiMixService extends IBaseService<SysApiMixDO> {

    /**
     * 同步权限API  若权限绑定的api对应改变  apiMix也对应修改
     * @param authId
     * @return
     * @author yansiyang
     * @date 2019/11/25 9:17
     */
    boolean synchronizeAuthApi(String authId);
}
