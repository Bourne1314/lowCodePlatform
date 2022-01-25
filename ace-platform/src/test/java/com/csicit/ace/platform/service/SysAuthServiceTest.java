package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysAuthServiceTest extends BaseTest {

    @Test
    public void infoAuthTest() {
//        SysAuthDO sysAuthDO = sysAuthService.infoAuth("e66ecacdc5ca437696e7d6c4d8bee13c");
//        System.out.println("权限信息：" + sysAuthDO.toString());
//        assertThat((sysAuthDO != null), is(true));
//        assertThat(sysAuthDO.getApis().size(), is(3));
//        assertThat(sysAuthDO.getApis().get(0).getName(), is("单体测试Api1"));
//        assertThat(sysAuthDO.getApis().get(1).getName(), is("单体测试Api2"));
//        assertThat(sysAuthDO.getApis().get(2).getName(), is("单体测试Api3"));
    }

    @Test
    public void saveAuthTest() {
        SysAuthDO sysAuthDO = new SysAuthDO();
        sysAuthDO.setName("单体测试权限111");
        sysAuthDO.setCode("ceshicode111");
        sysAuthDO.setOrgAdmin(1);
        sysAuthDO.setAppId("bt-ceshidemo111");
        sysAuthDO.setUserGroupAdmin(1);
        sysAuthDO.setRemark("rwrwerwer");
        sysAuthDO.setSortIndex(100);
        sysAuthDO.setParentId("0");
        boolean flg = sysAuthService.saveAuth(sysAuthDO);
        assertThat(flg, is(true));
        SysAuthDO auth = sysAuthService.getOne(new QueryWrapper<SysAuthDO>()
                .eq("name", "单体测试权限111"));
        assertThat((auth != null), is(true));
        System.out.println("权限信息：" + auth);

        SysAuthDO sysAuthDOCid = new SysAuthDO();
        sysAuthDOCid.setName("单体测试权限1下级");
        sysAuthDOCid.setCode("ceshicode1xiaji");
        sysAuthDOCid.setOrgAdmin(0);
        sysAuthDO.setAppId("bt-ceshidemo111");
        sysAuthDOCid.setUserGroupAdmin(1);
        sysAuthDOCid.setRemark("rwrwerwer");
        sysAuthDOCid.setSortIndex(110);
        sysAuthDOCid.setParentId(auth.getId());
        boolean flgCid = sysAuthService.saveAuth(sysAuthDOCid);
        assertThat(flgCid, is(true));
        SysAuthDO authChild = sysAuthService.getOne(new QueryWrapper<SysAuthDO>()
                .eq("name", "单体测试权限1下级"));
        assertThat((authChild != null), is(true));
        System.out.println("权限信息：" + authChild);
    }

    @Test
    public void updateAuthTest1() {
        SysAuthDO sysAuthDO = sysAuthService.getOne(new QueryWrapper<SysAuthDO>()
                .eq("name", "单体测试权限3"));
        System.out.println("修改前权限：" + sysAuthDO.toString());
        System.out.println("修改前子权限：" + sysAuthService.list(new QueryWrapper<SysAuthDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", sysAuthDO.getSortPath())).toString());
        sysAuthDO.setName("修改后-单体测试权限3");
        sysAuthDO.setSortIndex(1191);
        boolean flg = sysAuthService.updateAuth(sysAuthDO);
        assertThat(flg, is(true));
        SysAuthDO newAuth = sysAuthService.getById("e66ecacdc5ca437696e7d6c4d8bee13c");
        assertThat(newAuth.getName(), is("修改后-单体测试权限3"));
        assertThat(newAuth.getSortPath(), is("001191"));
        assertThat(sysAuthService.count(new QueryWrapper<SysAuthDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", newAuth.getSortPath())), is(3));
        System.out.println("修改后权限：" + newAuth.toString());
        System.out.println("修改后子权限：" + sysAuthService.list(new QueryWrapper<SysAuthDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", newAuth.getSortPath())).toString());
    }

    @Test
    public void updateAuthTest2() {
        SysAuthDO sysAuthDO = sysAuthService.getOne(new QueryWrapper<SysAuthDO>()
                .eq("name", "单体测试权限3下级-权限1"));
        System.out.println("修改前权限：" + sysAuthDO.toString());
        System.out.println("修改前子权限：" + sysAuthService.list(new QueryWrapper<SysAuthDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", sysAuthDO.getSortPath())).toString());
        sysAuthDO.setName("修改后-单体测试权限3下级-权限1");
        sysAuthDO.setSortIndex(2808);
        boolean flg = sysAuthService.updateAuth(sysAuthDO);
        assertThat(flg, is(true));
        SysAuthDO newAuth = sysAuthService.getById("111ecacdc5ca437696e7d6c4d8bee222");
        assertThat(newAuth.getName(), is("修改后-单体测试权限3下级-权限1"));
        assertThat(newAuth.getSortPath(), is("000040002808"));
        assertThat(sysAuthService.count(new QueryWrapper<SysAuthDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", newAuth.getSortPath())), is(2));
        System.out.println("修改后权限：" + newAuth.toString());
        System.out.println("修改后子权限：" + sysAuthService.list(new QueryWrapper<SysAuthDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", newAuth.getSortPath())).toString());
    }

    @Test
    public void updateAuthTest3() {
        SysAuthDO sysAuthDO = sysAuthService.getOne(new QueryWrapper<SysAuthDO>()
                .eq("name", "单体测试权限1"));
        System.out.println("修改前权限：" + sysAuthDO.toString());
        sysAuthDO.setName("修改后-单体测试权限1");
        sysAuthDO.setSortIndex(343);
        List<SysApiResourceDO> sysApiResourceDOList = new ArrayList<>(16);
        sysApiResourceDOList.add(sysApiResourceService.getById("TestController.test1"));
        sysApiResourceDOList.add(sysApiResourceService.getById("TestController.test2"));
        sysAuthDO.setApis(sysApiResourceDOList);
        boolean flg = sysAuthService.updateAuth(sysAuthDO);
        assertThat(flg, is(true));
        SysAuthDO newAuth = sysAuthService.getById("7eec28f2ddb64fc7a5542ea5d0e6e607");
        assertThat(newAuth.getName(), is("修改后-单体测试权限1"));
        assertThat(newAuth.getSortPath(), is("000343"));
        assertThat(sysAuthApiService.count(new QueryWrapper<SysAuthApiDO>()
                .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607")), is(2));
        assertThat(sysApiMixService.count(new QueryWrapper<SysApiMixDO>()
                        .eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e").eq("auth_id",
                        "7eec28f2ddb64fc7a5542ea5d0e6e607"))
                , is(2));
        assertThat(sysApiMixService.count(new QueryWrapper<SysApiMixDO>()
                        .eq("user_id", "dca07269fe1c4fecb667c442c3ace476").eq("auth_id",
                        "7eec28f2ddb64fc7a5542ea5d0e6e607"))
                , is(2));
        System.out.println("修改后权限：" + newAuth.toString());
        System.out.println("权限APi信息：" + sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>()
                .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607")).toString());
        System.out.println("Api有效信息：" + sysApiMixService.list(new QueryWrapper<SysApiMixDO>()
                .eq("auth_id", "7eec28f2ddb64fc7a5542ea5d0e6e607")).toString());
    }


    @Test
    public void deleteByIdsTest() {
        List<String> ids = new ArrayList<>();
        ids.add("7eec28f2ddb64fc7a5542ea5d0e6e607");// 权限1
        ids.add("dea304f572034b67a2f3f2727d470f47");// 权限2
        ids.add("b7259562c1a84c5c9bb9754ad69acb37");// 首页
        boolean flg = sysAuthService.deleteByIds(ids);
        assertThat(flg, is(true));
        System.out.println("首页菜单对应权限ID：" + sysMenuService.getById("b7259562c1a84c5c9bb9754ad69acb37").getAuthId());
        assertThat(sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
                .in("auth_id", ids)), is(0));
        assertThat(sysAuthUserService.count(new QueryWrapper<SysAuthUserDO>()
                .in("auth_id", ids)), is(0));
        assertThat(sysAuthRoleService.count(new QueryWrapper<SysAuthRoleDO>()
                .in("auth_id", ids)), is(0));
        assertThat(sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "dca07269fe1c4fecb667c442c3ace476")), is(0));// 用户2有效权限
        assertThat(sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")), is(2));// 用户1有效权限
        assertThat(sysAuthRoleLvService.count(new QueryWrapper<SysAuthRoleLvDO>()
                .eq("role_id", "3aca40d1a6594410bc599b6d3f2821f7")), is(2));
        String roleLvId = sysAuthRoleLvService.getOne(new QueryWrapper<SysAuthRoleLvDO>()
                .eq("role_id", "3aca40d1a6594410bc599b6d3f2821f7").eq("is_last_version", 1)).getId();
        assertThat(sysAuthRoleVService.count(new QueryWrapper<SysAuthRoleVDO>()
                .eq("lv_id", roleLvId)), is(0));
        assertThat(sysAuthUserLvService.count(new QueryWrapper<SysAuthUserLvDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "dca07269fe1c4fecb667c442c3ace476")), is(2));// 测试用户2
        String userLvId = sysAuthUserLvService.getOne(new QueryWrapper<SysAuthUserLvDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "dca07269fe1c4fecb667c442c3ace476").eq
                        ("is_last_version", 1)).getId();
        assertThat(sysAuthUserVService.count(new QueryWrapper<SysAuthUserVDO>()
                .eq("lv_id", userLvId)), is(0));

    }

    @Test
    public void queryUserAuthMixTest() {
        List<SysAuthDO> authDOS = sysAuthService.queryUserAuthMix("f0998046ae844ed4b1c1dab4e490f62e",
                "bt-ceshidemo111");
        assertThat(authDOS.size(), is(5));
        List<String> authIds = authDOS.stream().map(AbstractBaseDomain::getId).collect(Collectors.toList());
        assertThat(authIds, hasItems("7eec28f2ddb64fc7a5542ea5d0e6e607", "dea304f572034b67a2f3f2727d470f47",
                "92defb36651649f7a78e9e58e5fe850f", "1944b2e3e8664c69921a6164f81aac33",
                "b7259562c1a84c5c9bb9754ad69acb37"));
        System.out.println("用户1有效权限：" + authDOS.toString());
    }

    @Test
    public void getParentAuthsTest() {
        List<SysAuthDO> sysAuthDOS = new ArrayList<>(16);
        sysAuthService.getParentAuths(sysAuthDOS, "333ecacdc5ca437696e7d6c4d8bee333");
        System.out.println("权限上级信息：" + sysAuthDOS.toString());
        assertThat(sysAuthDOS.size(), is(2));
        assertThat(sysAuthDOS.get(0).getName(), is("单体测试权限3下级-权限1"));
        assertThat(sysAuthDOS.get(1).getName(), is("单体测试权限3"));
    }

    @Test
    public void getChildAuthsTest() {
        List<SysAuthDO> sysAuthDOS = new ArrayList<>(16);
        sysAuthService.getChildAuths("e66ecacdc5ca437696e7d6c4d8bee13c", sysAuthDOS);
        System.out.println("权限下级信息：" + sysAuthDOS.toString());
        assertThat(sysAuthDOS.size(), is(2));
        assertThat(sysAuthDOS.get(0).getName(), is("单体测试权限3下级-权限1"));
        assertThat(sysAuthDOS.get(1).getName(), is("单体测试权限3下级-权限1下级-权限1"));
    }
}