package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.DevUserRoleDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户角色管理 实例对象访问接口
 *
 * @author zuog
 * @date 2019/11/25 11:10
 */
@Transactional
public interface DevUserRoleService extends IBaseService<DevUserRoleDO> {
}
