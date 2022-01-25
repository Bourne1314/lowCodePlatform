package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 系统管理-集团应用权限 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:07
 */
@Data
@TableName("SYS_AUTH")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuthDO extends AbstractBaseRecordDomain {

    /**
     * 权限名称
     */
    private String name;
    /**
     * 集团应用标识
     */
    private String appId;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 父节点id
     */
    private String parentId;
    /**
     * 父节点名称
     */
    @TableField(exist = false)
    private String parentName;
    /**
     * 排序路径
     */
    private String sortPath;
    /**
     * 是否按组织管控
     */
    @TableField("IS_ORG_ADMIN")
    private Integer orgAdmin;
    /**
     * 是否按用户组管控
     */
    @TableField("IS_USER_GROUP_ADMIN")
    private Integer userGroupAdmin;
    /**
     * 权限标识
     */
    private String code;

    /**
     * 子集团列表
     */
    @TableField(exist = false)
    private List<SysAuthDO> children;

    /**
     * apiResource 集合
     */
    @TableField(exist = false)
    private List<SysApiResourceDO> apis;

//    /**
//     * 授控域-用户组
//     */
//    @TableField(exist = false)
//    private List<SysUserGroupDO> userGroups;
//
//    /**
//     * 授控域-组织
//     */
//    @TableField(exist = false)
//    private List<OrgOrganizationDO> organizes;

//    /**
//     * 该应用三员是否管控该应用权限 false不管控 true管控
//     */
//    @TableField(exist = false)
//    private boolean controlFlag;

    /**
     * 集团应用名称
     */
    @TableField(exist = false)
    private String appName;
    /**
     * 跟踪ID
     */
    private String traceId;
}
