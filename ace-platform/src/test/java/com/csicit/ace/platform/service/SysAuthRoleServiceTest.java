package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.GrantAuthReciveVO;
import com.csicit.ace.common.pojo.vo.GrantAuthVO;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysAuthRoleServiceTest extends BaseTest {


    /**
     * 测试角色权限关系
     *
     * @param
     * @return void
     * @author zuogang
     * @date 2020/2/18 17:21
     */
    private GrantAuthReciveVO setRoleAuth() {
        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();
        grantAuthReciveVO.setRoleId("3aca40d1a6594410bc599b6d3f2821f7");// 单体测试角色1
        grantAuthReciveVO.setAppId("bt-ceshidemo111");
        List<GrantAuthVO> grantAuthVOS = new ArrayList<>(16);
        GrantAuthVO grantAuthVO1 = new GrantAuthVO();
        grantAuthVO1.setId("e66ecacdc5ca437696e7d6c4d8bee13c");// 测试权限3
        grantAuthVO1.setRevoker(0);
        GrantAuthVO grantAuthVO2 = new GrantAuthVO();
        grantAuthVO2.setId("92defb36651649f7a78e9e58e5fe850f");// 测试权限4
        grantAuthVO2.setRevoker(0);
        GrantAuthVO grantAuthVO3 = new GrantAuthVO();
        grantAuthVO3.setId("1944b2e3e8664c69921a6164f81aac33");// 测试权限5
        grantAuthVO3.setRevoker(1);
        grantAuthVOS.add(grantAuthVO1);
        grantAuthVOS.add(grantAuthVO2);
        grantAuthVOS.add(grantAuthVO3);
        grantAuthReciveVO.setGrantAuthVOList(grantAuthVOS);
        return grantAuthReciveVO;
    }

    @Test
    public void saveRoleAuthTest() {

        GrantAuthReciveVO grantAuthReciveVO = setRoleAuth();

        boolean flg = sysAuthRoleService.saveRoleAuth(grantAuthReciveVO);
        assertThat(flg, is(true));
        assertThat(sysAuthRoleService.count(new QueryWrapper<SysAuthRoleDO>()
                .eq("role_id", "3aca40d1a6594410bc599b6d3f2821f7")), is(3));
        assertThat(sysAuthRoleService.count(new QueryWrapper<SysAuthRoleDO>()
                        .eq("is_revoke", 0)
                        .eq("auth_id", "e66ecacdc5ca437696e7d6c4d8bee13c").eq("role_id",
                        "3aca40d1a6594410bc599b6d3f2821f7"))
                , is(1));
        assertThat(sysAuthRoleService.count(new QueryWrapper<SysAuthRoleDO>()
                        .eq("is_revoke", 0)
                        .eq("auth_id", "92defb36651649f7a78e9e58e5fe850f").eq("role_id",
                        "3aca40d1a6594410bc599b6d3f2821f7"))
                , is(1));
        assertThat(sysAuthRoleService.count(new QueryWrapper<SysAuthRoleDO>()
                        .eq("is_revoke", 1)
                        .eq("auth_id", "1944b2e3e8664c69921a6164f81aac33").eq("role_id",
                        "3aca40d1a6594410bc599b6d3f2821f7"))
                , is(1));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 1)
                .eq("app_id", "bt-ceshidemo111").eq("activate_id", "3aca40d1a6594410bc599b6d3f2821f7")), is(1));
    }

    @Test
    public void saveRoleAuthActivationTest() {
        sysAuthRoleService.saveRoleAuth(setRoleAuth());
        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();
        grantAuthReciveVO.setAppId("bt-ceshidemo111");
        grantAuthReciveVO.setRoleId("3aca40d1a6594410bc599b6d3f2821f7");// 测试角色1
        boolean flg = sysAuthRoleService.saveRoleAuthActivation(grantAuthReciveVO);
        System.out.println(sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")).toString());
        assertThat(flg, is(true));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 1)
                .eq("app_id", "bt-ceshidemo111").eq("activate_id", "3aca40d1a6594410bc599b6d3f2821f7")), is(0));
        assertThat(sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")), is(2));
        assertThat(sysAuthRoleLvService.count(new QueryWrapper<SysAuthRoleLvDO>()
                .eq("role_id", "3aca40d1a6594410bc599b6d3f2821f7")), is(2));
        String lvId = sysAuthRoleLvService.getOne(new QueryWrapper<SysAuthRoleLvDO>()
                .eq("is_last_version", 1).eq("role_id", "3aca40d1a6594410bc599b6d3f2821f7")).getId();
        assertThat(sysAuthRoleVService.count(new QueryWrapper<SysAuthRoleVDO>()
                .eq("lv_id", lvId)), is(3));
    }

    @Test
    public void saveAuthRoleGrantTest() {
        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();
        grantAuthReciveVO.setAppId("bt-ceshidemo111");
        grantAuthReciveVO.setAuthId("7eec28f2ddb64fc7a5542ea5d0e6e607");// 单体测试权限1
        List<SysAuthRoleDO> sysAuthRoleDOList = new ArrayList<>(16);
        SysAuthRoleDO sysAuthRoleDO1 = new SysAuthRoleDO();
        sysAuthRoleDO1.setRoleId("4aca40d1a6594410bc599b6d3f2821f8");// 角色2
        sysAuthRoleDO1.setRevoker(0);
        sysAuthRoleDOList.add(sysAuthRoleDO1);
        SysAuthRoleDO sysAuthRoleDO2 = new SysAuthRoleDO();
        sysAuthRoleDO2.setRoleId("5aca40d1a6594410bc599b6d3f2821f9");// 角色3
        sysAuthRoleDO2.setRevoker(0);
        sysAuthRoleDOList.add(sysAuthRoleDO2);
        SysAuthRoleDO sysAuthRoleDO3 = new SysAuthRoleDO();
        sysAuthRoleDO3.setRoleId("6aca40d1a6594410bc599b6d3f2821f1");// 角色4
        sysAuthRoleDO3.setRevoker(1);
        sysAuthRoleDOList.add(sysAuthRoleDO3);
        grantAuthReciveVO.setSysAuthRoleDOList(sysAuthRoleDOList);
        boolean flg = (boolean)sysAuthRoleService.saveAuthRoleGrant(grantAuthReciveVO).get("result");
        assertThat(flg, is(true));
        assertThat(sysAuthRoleService.count(new QueryWrapper<SysAuthRoleDO>()
                .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607")), is(3));
        assertThat(sysAuthRoleService.count(new QueryWrapper<SysAuthRoleDO>()
                        .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607").eq("role_id",
                        "3aca40d1a6594410bc599b6d3f2821f7"))
                , is(0));
        assertThat(sysAuthRoleService.count(new QueryWrapper<SysAuthRoleDO>()
                        .eq("is_revoke", 0)
                        .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607").eq("role_id",
                        "4aca40d1a6594410bc599b6d3f2821f8"))
                , is(1));
        assertThat(sysAuthRoleService.count(new QueryWrapper<SysAuthRoleDO>()
                        .eq("is_revoke", 0)
                        .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607").eq("role_id",
                        "5aca40d1a6594410bc599b6d3f2821f9"))
                , is(1));
        assertThat(sysAuthRoleService.count(new QueryWrapper<SysAuthRoleDO>()
                        .eq("is_revoke", 1)
                        .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607").eq("role_id",
                        "6aca40d1a6594410bc599b6d3f2821f1"))
                , is(1));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("activate_id", "3aca40d1a6594410bc599b6d3f2821f7")), is(1));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("activate_id", "4aca40d1a6594410bc599b6d3f2821f8")), is(1));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("activate_id", "5aca40d1a6594410bc599b6d3f2821f9")), is(1));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("activate_id", "6aca40d1a6594410bc599b6d3f2821f1")), is(1));
    }

//    @Test
//    public void getActiveAuthRolesForAppId() {
//    }

//    @Test
//    public void getActiveAuthRoles() {
//    }

    @Test
    public void getActiveAuthRoleTest() {
        List<SysAuthRoleVDO> sysAuthRoleVDOS = sysAuthRoleService.getActiveAuthRole("4aca40d1a6594410bc599b6d3f2821f8");
        assertThat(sysAuthRoleVDOS.size(), is(2));
        assertThat(sysAuthRoleVDOS.get(0).getAuthId(), is("92defb36651649f7a78e9e58e5fe850f"));
        assertThat(sysAuthRoleVDOS.get(1).getAuthId(), is("1944b2e3e8664c69921a6164f81aac33"));
        System.out.println("结果集：" + sysAuthRoleVDOS.toString());
    }

//    @Test
//    public void authRoleActivation() {
//    }

    @Test
    public void getChangeRoleListTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("appId", "bt-ceshidemo111");
        map.put("beginTime", "");
        map.put("endTime", "");
        List<SysRoleDO> sysRoleDOS = sysAuthRoleService.getChangeRoleList(map);
        System.out.println("结果集：" + sysRoleDOS.toString());
        assertThat(sysRoleDOS.size(), is(2));
        assertThat(sysRoleDOS.get(0).getName(), is("单体测试角色1"));
        assertThat(sysRoleDOS.get(1).getName(), is("单体测试角色2"));
    }
}