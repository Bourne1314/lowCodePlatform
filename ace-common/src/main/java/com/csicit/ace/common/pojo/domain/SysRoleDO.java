package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;


/**
 * 角色表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:13:14
 */
@Data
@TableName("SYS_ROLE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleDO extends AbstractBaseRecordDomain {

    /**
     * 角色名
     */
    private String name;
    /**
     * 角色说明
     */
    private String roleExplain;

    /**
     * 角色标识 同一应用下不允许重复
     * 允许为空
     */
    private String roleCode;
    /**
     * 角色类型(1租户系统管理员,2租户安全保密员,3租户安全审计员)
     * (11集团系统管理员,22集团安全保密员,33集团安全审计员)
     * (111应用系统管理员,222应用安全保密员,333应用安全审计员)
     * (1111开发平台超级管理员,2222项目管理人员,3333项目开发人员)
     */
    private Integer roleType;

    /**
     * 角色作用域,0或者null表示本应用，1表示全局
     */
    private Integer roleScope;
    /**
     * 集团应用标识
     */
    private String appId;
    /**
     * 关联部门
     */
    @TableField(exist = false)
    private List<OrgDepartmentDO> deps;

    /**
     * 关联部门名称
     */
    @TableField(exist = false)
    private String depsName;
    /**
     * 下级级角色集合
     */
    @TableField(exist = false)
    private List<SysRoleDO> cRoles;
    /**
     * 互斥角色集合
     */
    @TableField(exist = false)
    private List<SysRoleDO> mRoles;
    /**
     * 下级级角色id集合
     */
    @TableField(exist = false)
    private List<String> cids;
    /**
     * 互斥角色id集合
     */
    @TableField(exist = false)
    private List<String> mids;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 全局角色情况下：是否存在角色用户未激活的情况
     */
    @TableField(exist = false)
    private Integer beActivedScopeRole;
}
