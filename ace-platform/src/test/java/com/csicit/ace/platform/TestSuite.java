package com.csicit.ace.platform;

import com.csicit.ace.platform.controller.BdPersonDocControllerTest;
import com.csicit.ace.platform.controller.OrgOrganizationControllerTest;
import com.csicit.ace.platform.controller.SysUserControllerTest;
import com.csicit.ace.platform.service.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 启动所有的测试类
 *
 * @Author zly
 * @Date 2018/11/2 09:54
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BdPersonDocServiceTest.class,
        BdPersonJobServiceTest.class,
        BdPostServiceTest.class,
        OrgDepartmentServiceTest.class,
        OrgGroupServiceTest.class,
        OrgOrganizationServiceTest.class,
        OrgOrganizationTypeServiceTest.class,
        ReportInfoServiceTest.class,
        SysAuthMixServiceTest.class,
        SysAuthRoleServiceTest.class,
        SysAuthRoleVServiceTest.class,
        SysAuthScopeOrgServiceTest.class,
        SysAuthServiceTest.class,
        SysAuthUserServiceTest.class,
        SysAuthUserVServiceTest.class,
        SysConfigServiceTest.class,
        SysDictServiceTest.class,
        SysDictValueServiceTest.class,
        SysGrantAuthServiceTest.class,
        SysGroupAppServiceTest.class,
        SysJobCalendarServiceTest.class,
        SysMenuServiceTest.class,
        SysMessageServiceTest.class,
        SysPasswordPolicyServiceTest.class,
        SysRoleMutexServiceTest.class,
        SysRoleRelationServiceTest.class,
        SysRoleServiceTest.class,
        SysUserGroupServiceTest.class,
        SysUserLoginServiceTest.class,
        SysUserRoleServiceTest.class,
        SysUserRoleVServiceTest.class,
        SysUserServiceTest.class,
        SysUserControllerTest.class,
        OrgOrganizationControllerTest.class,
        BdPersonDocControllerTest.class})
public class TestSuite {
}
