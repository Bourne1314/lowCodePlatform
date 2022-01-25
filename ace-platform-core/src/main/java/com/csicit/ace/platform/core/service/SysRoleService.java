package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * 角色管理 实例对象访问接口
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysRoleService extends IService<SysRoleDO> {

    /**
     * 填充角色关联的部门名称
     * @param roleList
     * @return
     * @author FourLeaves
     * @date 2021/3/19 11:21
     */
    List<SysRoleDO> fillDepName(List<SysRoleDO> roleList);

    /**
     * 根据id查询角色
     *
     * @param id
     * @return SysRoleDO
     * @author shanwj
     * @date 2019/4/18 18:00
     */
    SysRoleDO infoRole(String id);

    /**
     * 更新用户-角色-部门对应关系
     *
     * @param userId 用户主键
     * @param depId 部门主键
     * @param oldDepId 旧的部门主键
     * @return boolean
     * @author FourLeaves
     * @date 2021/3/22 9:39
     */
    boolean updateRoleAndDepForUser(String userId, String depId, String oldDepId);

    /**
     * 更新角色-部门对应关系
     *
     * @param role 角色
     * @param depId 部门主键
     * @author FourLeaves
     * @date 2021/3/22 9:39
     */
     boolean addRoleAndDep(SysRoleDO role, String depId);

    /**
     * 删除角色-部门对应关系
     *
     * @param role 角色
     * @param depId 部门主键
     * @author FourLeaves
     * @date 2021/3/22 9:39
     */
    boolean deleteRoleAndDep(SysRoleDO role, String depId);

    /**
     * 新增角色
     *
     * @param role 角色对象
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 18:00
     */
    boolean saveRole(SysRoleDO role);

    /**
     * 更新角色
     *
     * @param role 角色对象
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 18:01
     */
    boolean updateRole(SysRoleDO role);

    /**
     * 删除权限
     *
     * @param ids
     * @return boolean
     * @author shanwj
     * @date 2019/5/16 11:24
     */
    boolean deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 通过该角色ID获得可作为下级角色的列表数据
     *
     * @param appId
     * @param roleId
     * @param type   1修改组件使用 2编辑下级角色组件使用 3编辑互斥角色组件使用
     * @return SysRoleDO
     * @author shanwj
     * @date 2019/4/18 18:00
     */
    List<SysRoleDO> getChildAndMutualRoleList(String appId, String roleId, String type);


}
