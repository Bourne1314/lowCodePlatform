package com.csicit.ace.platform.core.controller;


import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * 基础接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */

public abstract class BaseController {

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    protected String appName;

    /**
     * Redis工具类
     */
    @Autowired
    protected CacheUtil cacheUtil;
    /**
     * 安全工具类
     */
    @Autowired
    protected SecurityUtils securityUtils;
    /**
     * 系统配置接口对象
     */
    @Autowired
    protected SysConfigService sysConfigService;
    /**
     * 组织-公司接口对象
     */
    @Autowired
    protected OrgCorporationService orgCorporationService;
    /**
     * 组织-公司接口对象
     */
    @Autowired
    protected OrgCorporationVService orgCorporationVService;

    /**
     * 组织-集团接口对象
     */
    @Autowired
    protected OrgGroupService orgGroupService;
    /**
     * 组织-组织接口对象
     */
    @Autowired
    protected OrgOrganizationService orgOrganizationService;
    /**
     * 组织-部门接口对象
     */
    @Autowired
    protected OrgDepartmentService orgDepartmentService;
    /**
     * 系统用户接口对象
     */
    @Autowired
    protected SysUserService sysUserService;
    /**
     * 用户组接口对象
     */
    @Autowired
    protected SysUserGroupService sysUserGroupService;

    /**
     * 用户角色关系接口对象
     */
    @Autowired
    protected SysUserRoleService sysUserRoleService;
    /**
     * 用户角色版本关系接口对象
     */
    @Autowired
    protected SysUserRoleLvService sysUserRoleLvService;
    /**
     * 用户角色历史数据关系接口对象
     */
    @Autowired
    protected SysUserRoleVService sysUserRoleVService;
    /**
     * 集团应用接口对象
     */
    @Autowired
    protected SysGroupAppService sysGroupAppService;

    /**
     * 权限接口对象
     */
    @Autowired
    protected SysAuthService sysAuthService;
    /**
     * 角色接口对象
     */
    @Autowired
    protected SysRoleService sysRoleService;
    /**
     * 菜单接口对象
     */
    @Autowired
    protected SysMenuService sysMenuService;

    /**
     * api资源接口对象
     */
    @Autowired
    protected SysApiResourceService sysApiResourceService;
    /**
     * 用户有效权限接口对象
     */
    @Autowired
    protected SysAuthMixService sysAuthMixService;
    /**
     * 角色父子关系接口
     */
    @Autowired
    protected SysRoleRelationService sysRoleRelationService;
    /**
     * 角色互斥关系接口
     */
    @Autowired
    protected SysRoleMutexService sysRoleMutexService;
    /**
     * 权限用户关系接口
     */
    @Autowired
    protected SysAuthUserService sysAuthUserService;
    /**
     * 权限角色关系接口
     */
    @Autowired
    protected SysAuthRoleService sysAuthRoleService;

    /**
     * 人员档案接口
     */
    @Autowired
    protected BdPersonDocService bdPersonDocService;

    /**
     * 系统字典类型接口对象
     */
    @Autowired
    protected SysDictService sysDictService;

    /**
     * 系统字典数据接口对象
     */
    @Autowired
    protected SysDictValueService sysDictValueService;

    /**
     * 用户管控域业务单元据接口对象
     */
    @Autowired
    protected SysAuthScopeOrgService sysAuthScopeOrgService;

    /**
     * 获取当前用户id
     *
     * @param
     * @return java.lang.String
     * @author zuogang
     * @date 2019/4/22 14:24
     */
    protected String getCurrentUserId() {
        return securityUtils.getCurrentUserId();
    }

}
