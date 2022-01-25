package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.pojo.domain.dev.DevUserRoleDO;
import com.csicit.ace.data.persistent.mapper.DevUserRoleMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.DevUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色管理 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("devUserRoleService")
public class DevUserRoleServiceImpl extends BaseServiceImpl<DevUserRoleMapper,DevUserRoleDO> implements DevUserRoleService {
}
