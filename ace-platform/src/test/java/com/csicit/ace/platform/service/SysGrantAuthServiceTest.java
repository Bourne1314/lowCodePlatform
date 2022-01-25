package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.GrantAuthReciveVO;
import com.csicit.ace.common.pojo.vo.GrantAuthVO;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.SysGrantAuthService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysGrantAuthServiceTest extends BaseTest {

    @Autowired
    SysGrantAuthService sysGrantAuthService;
//    @Test
//    public void saveUserRoleAuth() {
//    }

    @Test
    public void getUserAuthsTest() {
        List<GrantAuthVO> grantAuthVOS = sysGrantAuthService.getUserAuths("dca07269fe1c4fecb667c442c3ace476",
                "bt-ceshidemo111");
        System.out.println("结果集：" + grantAuthVOS.toString());
        assertThat(grantAuthVOS.size(), is(8));
        grantAuthVOS.stream().forEach(grantAuthVO -> {
            if (Objects.equals("7eec28f2ddb64fc7a5542ea5d0e6e607", grantAuthVO.getId()) || Objects.equals
                    ("dea304f572034b67a2f3f2727d470f47", grantAuthVO.getId())) {
                assertThat(grantAuthVO.getRevoker(), is(0));
            } else {
                assertThat(grantAuthVO.getRevoker(), is(-1));
            }
        });
    }

    @Test
    public void getRoleAuthsTest() {
        List<GrantAuthVO> grantAuthVOS = sysGrantAuthService.getRoleAuths("4aca40d1a6594410bc599b6d3f2821f8",
                "bt-ceshidemo111");
        System.out.println("结果集：" + grantAuthVOS.toString());
        assertThat(grantAuthVOS.size(), is(8));
        grantAuthVOS.stream().forEach(grantAuthVO -> {
            if (Objects.equals("92defb36651649f7a78e9e58e5fe850f", grantAuthVO.getId()) || Objects.equals
                    ("1944b2e3e8664c69921a6164f81aac33", grantAuthVO.getId())) {
                assertThat(grantAuthVO.getRevoker(), is(0));
            } else {
                assertThat(grantAuthVO.getRevoker(), is(-1));
            }
        });
    }

    @Test
    public void getUsersAndRolesTest() {
        GrantAuthReciveVO grantAuthReciveVO = sysGrantAuthService.getUsersAndRoles
                ("7eec28f2ddb64fc7a5542ea5d0e6e607", "bt-ceshidemo111");
        List<SysAuthUserDO> sysAuthUserDOList = grantAuthReciveVO.getSysAuthUserDOList();
        List<SysAuthRoleDO> sysAuthRoleDOList = grantAuthReciveVO.getSysAuthRoleDOList();
        System.out.println("用户结果集：" + sysAuthUserDOList);
        System.out.println("角色结果集：" + sysAuthRoleDOList);
        assertThat(sysAuthRoleDOList.size(), is(1));
        assertThat(sysAuthRoleDOList.get(0).getRoleName(), is("单体测试角色1"));
        assertThat(sysAuthUserDOList.size(), is(1));
        assertThat(sysAuthUserDOList.get(0).getRealName(), is("单体测试用户2"));
    }

    @Test
    public void getAppAuthsTest() {
        List<SysAuthDO> sysAuthDOS = sysGrantAuthService.getAppAuths("bt-ceshidemo222");
        System.out.println("结果集：" + sysAuthDOS.toString());
        assertThat(sysAuthDOS.size(), is(1));
        assertThat(sysAuthDOS.get(0).getName(), is("首页(应用2)"));
    }

    //    @Test
    private void waitActivationUserAndRoleList() {
        String token = login("appceshiren3");// 测试应用管理员
        SecurityUtils.TEST_TOKEN = token;

        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();
        grantAuthReciveVO.setAppId("bt-ceshidemo111");
        grantAuthReciveVO.setUserId("dca07269fe1c4fecb667c442c3ace476");// 单体测试用户2
        List<GrantAuthVO> grantAuthVOS = new ArrayList<>(16);
        GrantAuthVO grantAuthVO1 = new GrantAuthVO();
        grantAuthVO1.setId("e66ecacdc5ca437696e7d6c4d8bee13c");// 测试权限3
        grantAuthVO1.setRevoker(0);
        grantAuthVOS.add(grantAuthVO1);
        grantAuthReciveVO.setGrantAuthVOList(grantAuthVOS);
        sysAuthUserService.saveUserAuth(grantAuthReciveVO);

        GrantAuthReciveVO grantAuthReciveVO2 = new GrantAuthReciveVO();
        grantAuthReciveVO2.setRoleId("3aca40d1a6594410bc599b6d3f2821f7");// 单体测试角色1
        grantAuthReciveVO2.setAppId("bt-ceshidemo111");
        List<GrantAuthVO> grantAuthVOS2 = new ArrayList<>(16);
        GrantAuthVO grantAuthVO2 = new GrantAuthVO();
        grantAuthVO2.setId("e66ecacdc5ca437696e7d6c4d8bee13c");// 测试权限3
        grantAuthVO2.setRevoker(0);
        grantAuthVOS2.add(grantAuthVO2);
        grantAuthReciveVO2.setGrantAuthVOList(grantAuthVOS2);
        sysAuthRoleService.saveRoleAuth(grantAuthReciveVO2);

        GrantAuthReciveVO grantAuthReciveVO1 = sysGrantAuthService.waitActivationUserAndRoleList("bt-ceshidemo111");
        System.out.println("待激活用户结果集：" + grantAuthReciveVO1.getSysUserList().toString());
        assertThat(grantAuthReciveVO1.getSysUserList().size(), is(1));
        System.out.println("待激活角色结果集：" + grantAuthReciveVO1.getSysRoleList().toString());
        assertThat(grantAuthReciveVO1.getSysRoleList().size(), is(1));
    }

    @Test
    public void waitActivationUserAndRoleCount() {
        waitActivationUserAndRoleList();
        GrantAuthReciveVO grantAuthReciveVO = sysGrantAuthService.waitActivationUserAndRoleCount();
        assertThat(grantAuthReciveVO.getRoleCount(), is(1));
        assertThat(grantAuthReciveVO.getUserCount(), is(1));
    }

    @Test
    public void saveAllActivation() {
        waitActivationUserAndRoleList();
        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();
        grantAuthReciveVO.setAppId("bt-ceshidemo111");
        boolean flg = sysGrantAuthService.saveAllActivation(grantAuthReciveVO);
        assertThat(flg, is(true));

        System.out.println(sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", "dca07269fe1c4fecb667c442c3ace476")).toString());
        assertThat(flg, is(true));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 0)
                .eq("app_id", "bt-ceshidemo111").eq("activate_id", "dca07269fe1c4fecb667c442c3ace476")), is(0));
        assertThat(sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
                .eq("app_id", "bt-ceshidemo111")  .eq("user_id", "dca07269fe1c4fecb667c442c3ace476")), is(1));
        assertThat(sysAuthUserLvService.count(new QueryWrapper<SysAuthUserLvDO>()
                .eq("app_id", "bt-ceshidemo111")   .eq("user_id", "dca07269fe1c4fecb667c442c3ace476")), is(2));
        String lvId = sysAuthUserLvService.getOne(new QueryWrapper<SysAuthUserLvDO>()
                .eq("app_id", "bt-ceshidemo111")  .eq("is_last_version", 1).eq("user_id", "dca07269fe1c4fecb667c442c3ace476")).getId();
        assertThat(sysAuthUserVService.count(new QueryWrapper<SysAuthUserVDO>()
                .eq("lv_id", lvId)), is(1));

        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 1)
                .eq("app_id", "bt-ceshidemo111").eq("activate_id", "3aca40d1a6594410bc599b6d3f2821f7")), is(0));
        assertThat(sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")), is(3));
        assertThat(sysAuthRoleLvService.count(new QueryWrapper<SysAuthRoleLvDO>()
                .eq("role_id", "3aca40d1a6594410bc599b6d3f2821f7")), is(2));
        String lvId2 = sysAuthRoleLvService.getOne(new QueryWrapper<SysAuthRoleLvDO>()
                .eq("is_last_version", 1).eq("role_id", "3aca40d1a6594410bc599b6d3f2821f7")).getId();
        assertThat(sysAuthRoleVService.count(new QueryWrapper<SysAuthRoleVDO>()
                .eq("lv_id", lvId2)), is(1));
    }
}