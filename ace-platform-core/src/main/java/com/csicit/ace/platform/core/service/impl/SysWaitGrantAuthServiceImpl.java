package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysWaitGrantAuthDO;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.data.persistent.mapper.SysWaitGrantAuthMapper;
import com.csicit.ace.platform.core.service.SysWaitGrantAuthService;
import org.springframework.stereotype.Service;


/**
 * 待激活的用户或角色表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-05 17:37:22
 */
@Service("sysWaitGrantAuthService")
public class SysWaitGrantAuthServiceImpl extends ServiceImpl<SysWaitGrantAuthMapper, SysWaitGrantAuthDO> implements
        SysWaitGrantAuthService {
    /**
     * 添加新的待激活的表数据
     *
     * @param id
     * @param type  0为用户1为角色
     * @param appId
     * @author zuogang
     * @date 2019/7/4 14:54
     */
    @Override
    public boolean saveSysWaitGrantAuth(String id, Integer type, String appId) {
        // 添加新的待激活的表数据
        SysWaitGrantAuthDO sysWaitGrantAuthDO = new SysWaitGrantAuthDO();
        sysWaitGrantAuthDO.setId(UuidUtils.createUUID());
        sysWaitGrantAuthDO.setType(type);
        sysWaitGrantAuthDO.setActivateId(id);
        sysWaitGrantAuthDO.setAppId(appId);
        if (!this.save(sysWaitGrantAuthDO))
            return false;
        return true;
    }
}
