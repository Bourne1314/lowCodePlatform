package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.domain.SysRoleDO;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/12/1 15:25
 */
public interface IRole {
    /**
     * 通过角色主键获取角色
     * @param id
     * @return 
     * @author FourLeaves
     * @date 2020/12/1 15:26
     */
    SysRoleDO getRoleById(String id);

    /**
     * 通过角色标识获取角色
     * @param code
     * @return 
     * @author FourLeaves
     * @date 2020/12/1 15:26
     */
    SysRoleDO getRoleByCode(String code);
}
