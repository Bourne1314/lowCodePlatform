package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.DevRoleDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色管理 实例对象访问接口
 *
 * @author zuog
 * @date 2019/11/25 11:10
 */
@Transactional
public interface DevRoleService extends IBaseService<DevRoleDO> {
    /**
     * 获取角色信息
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/11/27 17:47
     */
    DevRoleDO getRoleInfo(String id);
    /**
     * 保存角色
     *
     * @param sysRoleDO
     * @return
     * @author zuogang
     * @date 2019/11/27 17:47
     */
    boolean saveRole(DevRoleDO sysRoleDO);
    /**
     * 更新角色
     *
     * @param sysRoleDO
     * @return
     * @author zuogang
     * @date 2019/11/27 17:47
     */
    boolean updateRole(DevRoleDO sysRoleDO);
}
