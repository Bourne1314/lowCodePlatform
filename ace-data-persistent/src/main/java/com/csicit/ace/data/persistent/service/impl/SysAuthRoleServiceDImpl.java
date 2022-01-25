package com.csicit.ace.data.persistent.service.impl;

import com.csicit.ace.common.pojo.domain.SysAuthRoleDO;
import com.csicit.ace.data.persistent.mapper.SysAuthRoleMapper;
import com.csicit.ace.data.persistent.service.SysAuthRoleServiceD;
import org.springframework.stereotype.Service;

/**
 * 权限角色关系管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysAuthRoleServiceD")
public class SysAuthRoleServiceDImpl extends BaseServiceImpl<SysAuthRoleMapper, SysAuthRoleDO> implements
        SysAuthRoleServiceD {

}
