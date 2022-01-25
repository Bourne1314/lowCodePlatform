package com.csicit.ace.platform.core.service.impl;

import com.csicit.ace.common.pojo.domain.SysAuthScopeUserGroupDO;
import com.csicit.ace.data.persistent.mapper.SysAuthScopeUserGroupMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysAuthScopeUserGroupService;
import org.springframework.stereotype.Service;

/**
 * 系统管理-有效权限-授权用户组关系管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysAuthScopeUserGroupService")
public class SysAuthScopeUserGroupServiceImpl extends BaseServiceImpl<SysAuthScopeUserGroupMapper,
        SysAuthScopeUserGroupDO> implements SysAuthScopeUserGroupService {

}
