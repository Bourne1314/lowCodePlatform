package com.csicit.ace.platform.core.service.impl;

import com.csicit.ace.common.pojo.domain.SysWaitGrantUserDO;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.data.persistent.mapper.SysWaitGrantUserMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysWaitGrantUserService;
import org.springframework.stereotype.Service;


/**
 * 角色用户授予—待激活的用户表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-12-12 15:06:28
 */
@Service("sysWaitGrantUserService")
public class SysWaitGrantUserServiceImpl extends BaseServiceImpl<SysWaitGrantUserMapper, SysWaitGrantUserDO>
        implements SysWaitGrantUserService {
    /**
     * 添加带激活的用户角色授予的用户表
     *
     * @param appId
     * @param userId
     * @return
     * @author zuogang
     * @date 2019/12/12 15:03
     */
    @Override
    public boolean saveSysWaitGrantUser(String appId, String userId) {
        SysWaitGrantUserDO sysWaitGrantUserDO = new SysWaitGrantUserDO();
        sysWaitGrantUserDO.setId(UuidUtils.createUUID());
        sysWaitGrantUserDO.setAppId(appId);
        sysWaitGrantUserDO.setUserId(userId);
        save(sysWaitGrantUserDO);
        return true;
    }
}
