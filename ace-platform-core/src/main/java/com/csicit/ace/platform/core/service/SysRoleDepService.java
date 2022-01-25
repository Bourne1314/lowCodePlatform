package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.SysRoleDepDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/4/21 8:53
 */
@Transactional
public interface SysRoleDepService extends IService<SysRoleDepDO> {

    /**
     * 删除角色关联部门
     * @param id
     * @return
     * @author FourLeaves
     * @date 2021/8/10 9:11
     */
    boolean deleteRoleDep(String id);

    /**
     * 保存角色关联部门
     * @param sysRoleDepDO
     * @return 
     * @author FourLeaves
     * @date 2021/8/10 9:11
     */
    boolean addRoleDep(SysRoleDepDO sysRoleDepDO);

    /**
     * 获取角色关联部门列表
     * @param roleId
     * @return
     * @author FourLeaves
     * @date 2021/8/10 8:58
     */
    List<SysRoleDepDO> getRoleDeps(String roleId);

    /**
     * 获取角色关联部门列表
     * @param roleId
     * @return 
     * @author FourLeaves
     * @date 2021/8/10 8:58
     */
    List<OrgDepartmentDO> getDeps(String roleId);

    /**
     * 获取角色关联部门名称
     * @param roleId
     * @return
     * @author FourLeaves
     * @date 2021/8/10 8:58
     */
    String getDepsName(String roleId);
}
