package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysWaitGrantAuthDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 待激活的用户或角色表 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-05 17:37:22
 */
@Transactional
public interface SysWaitGrantAuthService extends IService<SysWaitGrantAuthDO> {
    /**
     * 添加新的待激活的表数据
     *
     * @param id
     * @param type
     * @param appId
     * @return
     */
    boolean saveSysWaitGrantAuth(String id, Integer type, String appId);
}
