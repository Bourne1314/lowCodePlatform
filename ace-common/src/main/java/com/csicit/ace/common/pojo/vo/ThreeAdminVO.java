package com.csicit.ace.common.pojo.vo;

import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 三员管理数据对象
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/23 18:05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThreeAdminVO {

    /**
     * 角色类型
     */
    private String roleType;
    /**
     * 用户集合
     */
    private List<SysUserDO> sysUserDOList;
    /**
     * 删除3员ID集合
     */
    private String[] userIds;
    /**
     * 用户有效权限-授权组织对象集合
     */
    private List<OrgOrganizationDO> orgOrganizationDOList;
    /**
     * 用户有效权限-授权集团对象集合
     */
    private List<OrgGroupDO> orgGroupDOList;
    /**
     * 用户有效权限-授权用户组对象集合
     */
    private List<SysUserGroupDO> sysUserGroupDOList;
    /**
     * 用户有效权限-授权应用对象集合
     */
//    private List<SysGroupAppDO> sysGroupAppDOList;
    /**
     *  提交应用三员-管控应用权限
     */
    private List<SysAuthDO> sysAuthDOList;
}
