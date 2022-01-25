package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.GrantAuthReciveVO;
import com.csicit.ace.common.pojo.vo.GrantAuthVO;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysAuthUserServiceTest extends BaseTest {

    /**
     * 测试用户权限关系
     *
     * @param
     * @return void
     * @author zuogang
     * @date 2020/2/18 17:21
     */
    private GrantAuthReciveVO setUserAuth() {
        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();
        grantAuthReciveVO.setAppId("bt-ceshidemo111");
        grantAuthReciveVO.setUserId("dca07269fe1c4fecb667c442c3ace476");// 单体测试用户2
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
    public void saveUserAuthTest() {
        GrantAuthReciveVO grantAuthReciveVO = setUserAuth();

        boolean flg = sysAuthUserService.saveUserAuth(grantAuthReciveVO);
        assertThat(flg, is(true));
        assertThat(sysAuthUserService.count(new QueryWrapper<SysAuthUserDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "dca07269fe1c4fecb667c442c3ace476")), is(3));

        assertThat(sysAuthUserService.count(new QueryWrapper<SysAuthUserDO>()
                        .eq("app_id", "bt-ceshidemo111").eq("is_revoke", 0)
                        .eq("auth_id", "e66ecacdc5ca437696e7d6c4d8bee13c").eq("user_id",
                        "dca07269fe1c4fecb667c442c3ace476"))
                , is(1));
        assertThat(sysAuthUserService.count(new QueryWrapper<SysAuthUserDO>()
                        .eq("app_id", "bt-ceshidemo111").eq("is_revoke", 0)
                        .eq("auth_id", "92defb36651649f7a78e9e58e5fe850f").eq("user_id",
                        "dca07269fe1c4fecb667c442c3ace476"))
                , is(1));
        assertThat(sysAuthUserService.count(new QueryWrapper<SysAuthUserDO>()
                        .eq("app_id", "bt-ceshidemo111").eq("is_revoke", 1)
                        .eq("auth_id", "1944b2e3e8664c69921a6164f81aac33").eq("user_id",
                        "dca07269fe1c4fecb667c442c3ace476"))
                , is(1));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("app_id", "bt-ceshidemo111").eq("type", 0)
                .eq("app_id", "bt-ceshidemo111").eq("activate_id", "dca07269fe1c4fecb667c442c3ace476")), is(1));

    }

    @Test
    public void saveUserAuthActivationTest() {
        sysAuthUserService.saveUserAuth(setUserAuth());
        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();
        grantAuthReciveVO.setAppId("bt-ceshidemo111");
        grantAuthReciveVO.setUserId("dca07269fe1c4fecb667c442c3ace476");// 测试用户2
        boolean flg = sysAuthUserService.saveUserAuthActivation(grantAuthReciveVO);
        assertThat(flg, is(true));
        System.out.println(sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", "dca07269fe1c4fecb667c442c3ace476")).toString());
        assertThat(flg, is(true));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 0)
                .eq("app_id", "bt-ceshidemo111").eq("activate_id", "dca07269fe1c4fecb667c442c3ace476")), is(0));
        assertThat(sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "dca07269fe1c4fecb667c442c3ace476")), is(2));
        assertThat(sysAuthUserLvService.count(new QueryWrapper<SysAuthUserLvDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "dca07269fe1c4fecb667c442c3ace476")), is(2));
        String lvId = sysAuthUserLvService.getOne(new QueryWrapper<SysAuthUserLvDO>()
                .eq("app_id", "bt-ceshidemo111").eq("is_last_version", 1).eq("user_id",
                        "dca07269fe1c4fecb667c442c3ace476")).getId();
        assertThat(sysAuthUserVService.count(new QueryWrapper<SysAuthUserVDO>()
                .eq("lv_id", lvId)), is(3));
    }

    @Test
    public void saveAuthUserGrantTest() {
        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();
        grantAuthReciveVO.setAppId("bt-ceshidemo111");
        grantAuthReciveVO.setAuthId("7eec28f2ddb64fc7a5542ea5d0e6e607");// 单体测试权限1
        List<SysAuthUserDO> sysAuthUserDOList = new ArrayList<>(16);
        SysAuthUserDO sysAuthUserDO1 = new SysAuthUserDO();
        sysAuthUserDO1.setUserId("cd3947f0c7a3478e97acbec9813f66e3");
        sysAuthUserDO1.setRevoker(0);
        sysAuthUserDOList.add(sysAuthUserDO1);
        SysAuthUserDO sysAuthUserDO2 = new SysAuthUserDO();
        sysAuthUserDO2.setUserId("262432b31fd249ce8ade25aa1df39966");
        sysAuthUserDO2.setRevoker(0);
        sysAuthUserDOList.add(sysAuthUserDO2);
        SysAuthUserDO sysAuthUserDO3 = new SysAuthUserDO();
        sysAuthUserDO3.setUserId("884b9b13c26740aaa07849bec202e104");
        sysAuthUserDO3.setRevoker(1);
        sysAuthUserDOList.add(sysAuthUserDO3);
        grantAuthReciveVO.setSysAuthUserDOList(sysAuthUserDOList);
        boolean flg =(boolean)sysAuthUserService.saveAuthUserGrant(grantAuthReciveVO).get("result");
        assertThat(flg, is(true));
        assertThat(sysAuthUserService.count(new QueryWrapper<SysAuthUserDO>()
                .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607")), is(3));
        assertThat(sysAuthUserService.count(new QueryWrapper<SysAuthUserDO>()
                        .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607").eq("user_id",
                        "dca07269fe1c4fecb667c442c3ace476"))
                , is(0));
        assertThat(sysAuthUserService.count(new QueryWrapper<SysAuthUserDO>()
                        .eq("is_revoke", 0)
                        .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607").eq("user_id",
                        "cd3947f0c7a3478e97acbec9813f66e3"))
                , is(1));
        assertThat(sysAuthUserService.count(new QueryWrapper<SysAuthUserDO>()
                        .eq("is_revoke", 0)
                        .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607").eq("user_id",
                        "262432b31fd249ce8ade25aa1df39966"))
                , is(1));
        assertThat(sysAuthUserService.count(new QueryWrapper<SysAuthUserDO>()
                        .eq("is_revoke", 1)
                        .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607").eq("user_id",
                        "884b9b13c26740aaa07849bec202e104"))
                , is(1));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("activate_id", "dca07269fe1c4fecb667c442c3ace476")), is(1));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("activate_id", "262432b31fd249ce8ade25aa1df39966")), is(1));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("activate_id", "884b9b13c26740aaa07849bec202e104")), is(1));
        assertThat(sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("activate_id", "cd3947f0c7a3478e97acbec9813f66e3")), is(1));
    }

//    @Test
//    public void getActiveAuthUser() {
//    }
//
//    @Test
//    public void getActiveAuthUserForApp() {
//    }

//    @Test
//    public void authUserActivation() {
//    }

    @Test
    public void getChangeUserListTest() {
        String token = login("appceshiren3");
        SecurityUtils.TEST_TOKEN = token;
        Map<String, Object> map = new HashMap<>();
        map.put("appId", "bt-ceshidemo111");
        map.put("beginTime", "");
        map.put("endTime", "");
        List<SysUserDO> sysUserDOS = sysAuthUserService.getChangeUserList(map);
        System.out.println("结果集：" + sysUserDOS.toString());
        assertThat(sysUserDOS.size(), is(1));
        assertThat(sysUserDOS.get(0).getUserName(), is("ceshiuser2"));
    }
}