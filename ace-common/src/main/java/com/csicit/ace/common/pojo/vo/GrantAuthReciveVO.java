package com.csicit.ace.common.pojo.vo;

import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.domain.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 前端已授权数据对象
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/17 18:05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrantAuthReciveVO {
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 激活类型 0为用户，1为角色
     */
    private Integer type;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String realName;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 用户、角色授权时勾选的授权对象集合
     */
    private List<GrantAuthVO> grantAuthVOList;
    /**
     * 权限id
     */
    private String authId;
    /**
     * 权限名
     */
    private String authName;
    /**
     * 权限授权时勾选的授权用户集合
     */
    private List<SysAuthUserDO> sysAuthUserDOList;
    /**
     * 权限授权时勾选的授权角色集合
     */
    private List<SysAuthRoleDO> sysAuthRoleDOList;
    /**
     * 用户有效权限-授权组织对象集合
     */
    private List<OrgOrganizationDO> orgOrganizationDOList;
    /**
     * 用户有效权限-授权用户组对象集合
     */
    private List<SysUserGroupDO> sysUserGroupDOList;

    /**
     * 待激活的用户列表
     */
    private List<SysUserDO> sysUserList;

    /**
     * 待激活的用户个数
     */
    private Integer userCount;

    /**
     * 待激活的角色列表
     */
    private List<SysRoleDO> sysRoleList;

    /**
     * 待激活的角色个数
     */
    private Integer roleCount;

    /**
     * 待激活角色关系的用户个数
     */
    private Integer roleUserCount;

}
