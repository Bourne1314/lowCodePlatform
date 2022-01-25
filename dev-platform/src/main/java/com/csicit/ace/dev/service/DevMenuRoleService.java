package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.DevMenuRoleDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 菜单角色管理 实例对象访问接口
 *
 * @author zuog
 * @date 2019/11/25 11:10
 */
@Transactional
public interface DevMenuRoleService extends IBaseService<DevMenuRoleDO> {
}
