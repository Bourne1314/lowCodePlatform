package com.csicit.ace.orgauth.core.service.impl;

import com.csicit.ace.common.pojo.domain.SysUserRoleDO;
import com.csicit.ace.data.persistent.mapper.SysUserRoleMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色关系管理 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Service("sysUserRoleServiceO")
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRoleDO> implements
        SysUserRoleService {


}
