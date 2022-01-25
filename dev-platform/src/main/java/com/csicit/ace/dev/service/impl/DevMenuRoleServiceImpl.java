package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.pojo.domain.dev.DevMenuRoleDO;
import com.csicit.ace.data.persistent.mapper.DevMenuRoleMapper;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.DevMenuRoleService;
import org.springframework.stereotype.Service;

/**
 * 菜单角色管理 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("devMenuRoleService")
public class DevMenuRoleServiceImpl extends BaseServiceImpl<DevMenuRoleMapper,DevMenuRoleDO> implements DevMenuRoleService {
}
