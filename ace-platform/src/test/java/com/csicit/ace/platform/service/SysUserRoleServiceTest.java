package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SysUserRoleServiceTest extends BaseTest {

//    @Test
////    public void deleteGroupThreeRole() {
////    }

//    @Test
//    public void deleteAppThreeRole() {
//    }

    @Test
    public void getAppThreeDoMainTest() {
        SysUserDO sysUserDO = sysUserRoleService.getAppThreeDoMain("681cead3d0764d24b261fe3d531ad775", "activated");
        System.out.println("结果集为：" + sysUserDO.toString());
        assertThat(sysUserDO.getApps().size(), is(1));
        assertThat(sysUserDO.getApps().get(0).getName(), is("单体测试应用111"));
        assertThat(sysUserDO.getOrganizes().size(), is(1));
        assertThat(sysUserDO.getOrganizes().get(0).getName(), is("单体测试顶级业务单元1"));
        assertThat(sysUserDO.getUserGroups().size(), is(0));
    }

    private void setGroupThreeDoMain() {
        saveThreeRoleTest();
        SysUserDO sysUserDO = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "ceshiguanliyuan"));
        sysUserDO.setUserType(11);
        List<OrgGroupDO> groups = new ArrayList<>(16);
        groups.add(orgGroupService.getById("b190c25cb0234d51ab6de0bda198891b"));// 单体测试一级集团2
        groups.add(orgGroupService.getById("af562c26e35d44dbb82c1cf3b56ec112"));// 单体测试二级集团1
        groups.add(orgGroupService.getById("89a06f1243a74a9fbe20fdf08c9f27cd"));// 单体测试三级集团1
        sysUserDO.setGroups(groups);
        sysUserService.saveUserGroupControlDomain(sysUserDO);
        List<String> ids = new ArrayList<>(16);
        ids.add("b190c25cb0234d51ab6de0bda198891b");// 单体测试一级集团2
        ids.add("af562c26e35d44dbb82c1cf3b56ec112");// 单体测试二级集团1
        ids.add("89a06f1243a74a9fbe20fdf08c9f27cd");// 单体测试三级集团1
        Integer count = sysUserAdminOrgService.count(new QueryWrapper<SysUserAdminOrgDO>()
                .in("organization_id", ids).eq("user_id", sysUserDO.getId()).eq("is_activated", 0));
        assertThat(count, is(3));
    }

    @Test
    public void getGroupToBeActivatedDataTest() {
        setGroupThreeDoMain();
        List<SysUserDO> sysUserDOS = sysUserRoleService.getGroupToBeActivatedData();
        System.out.println("结果集：" + sysUserDOS.toString());
        List<String> names = sysUserDOS.stream().map(SysUserDO::getUserName).collect(Collectors.toList());
        assertThat(names, hasItems("ceshiguanliyuan"));
    }

    @Test
    public void setGroupActivatedTest() {
        setGroupThreeDoMain();
        String id = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "ceshiguanliyuan")).getId();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("roleType", "11");
        boolean flg = sysUserRoleService.setGroupActivated(map);
        assertThat(flg, is(true));
        assertThat(sysUserRoleService.count(new QueryWrapper<SysUserRoleDO>()
                .eq("role_id", "groupadmin").eq("user_id", id)), is(1));
        assertThat(sysUserService.getById(id).getUserType(), is(1));
        assertThat(sysUserAdminOrgService.count(new QueryWrapper<SysUserAdminOrgDO>()
                .eq("is_activated", 1).eq("user_id", id)), is(3));
        assertThat(sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", id)), greaterThan(0));
        System.out.println("有效权限结果集：" + sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", id)).toString());
        System.out.println("集团授控域：" + sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                .eq("user_id", id)).toString());
    }

    @Test
    public void getUnallocatedDataTest() {
        saveThreeRoleTest();
        Map<String, Object> map = new HashMap<>();
        map.put("type", "1");
        List<SysUserDO> sysUserDOS = sysUserRoleService.getUnallocatedData(map);
        System.out.println("未分配集团管理员结果集：" + sysUserDOS.toString());
        assertThat(sysUserDOS.size(), greaterThan(1));


        Map<String, Object> map2 = new HashMap<>();
        map2.put("type", "2");
        map2.put("groupId", "9e92cb17c7bf4af9b6072520d42ea103");
        List<SysUserDO> sysUserDOS2 = sysUserRoleService.getUnallocatedData(map2);
        System.out.println("未分配应用管理员结果集：" + sysUserDOS2.toString());
        assertThat(sysUserDOS2.size(), is(1));

    }

    @Test
    public void saveThreeRoleTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("realName", "测试管理员11");//集团级
        map.put("userName", "ceshiguanliyuan");
        map.put("userType", 111);
        map.put("idAddress", "192.168.19.22");
        map.put("secretLevel", 5);
        boolean flg = sysUserRoleService.saveThreeRole(map);
        assertThat(flg, is(true));
        SysUserDO groupadmin = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "ceshiguanliyuan"));
        assertThat(groupadmin != null, is(true));
        System.out.println("新增集团管理员结果集:" + groupadmin.toString());

        Map<String, Object> map2 = new HashMap<>();
        map2.put("realName", "应用测试管理员11");//集团级
        map2.put("userName", "appceshiguanliyuan");
        map2.put("userType", 222);
        map2.put("idAddress", "192.168.19.11");
        map2.put("secretLevel", 5);
        map2.put("groupId", "9e92cb17c7bf4af9b6072520d42ea103");
        boolean flg2 = sysUserRoleService.saveThreeRole(map2);
        assertThat(flg2, is(true));
        SysUserDO appadmin = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "appceshiguanliyuan"));
        assertThat(appadmin != null, is(true));
        System.out.println("新增应用管理员结果集2:" + appadmin.toString());
    }

    @Test
    public void updateThreeRoleTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "78a510f9289c4ca7b299b433078e64e6");// 集团保密员
        map.put("realName", "修改后-集团保密员");
        map.put("ipAddress", "192.168.19.12;192.168.16.165");
        R r = sysUserRoleService.updateThreeRole(map);
        System.out.println(r.get("msg"));
        assertThat("保存成功!", is(r.get("msg")));
        assertThat(sysUserService.getById("78a510f9289c4ca7b299b433078e64e6").getIpAddress(), is("192.168.19.12;" +
                "192.168.16.165"));
        assertThat(sysUserService.getById("78a510f9289c4ca7b299b433078e64e6").getRealName(), is("修改后-集团保密员"));
        System.out.println("结果集为：" + sysUserService.getById("78a510f9289c4ca7b299b433078e64e6"));

        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "681cead3d0764d24b261fe3d531ad775");// 应用管理员
        map1.put("realName", "修改后-应用管理员");
        map1.put("ipAddress", "192.168.19.32;192.168.16.168");
        R r1 = sysUserRoleService.updateThreeRole(map1);
        System.out.println(r1.get("msg"));
        assertThat("保存成功!", is(r1.get("msg")));
        assertThat(sysUserService.getById("681cead3d0764d24b261fe3d531ad775").getRealName(), is("修改后-应用管理员"));
        assertThat(sysUserService.getById("681cead3d0764d24b261fe3d531ad775").getIpAddress(), is("192.168.19.32;" +
                "192.168.16.168"));
        System.out.println("结果集为：" + sysUserService.getById("681cead3d0764d24b261fe3d531ad775"));
    }

    @Test
    public void getGroupAllocatedDataTest() {
        Map<String, Object> map = sysUserRoleService.getGroupAllocatedData();
        List<SysUserDO> allocatedDataList = (ArrayList) map.get("allocatedDataList");
        List<String> userIds = allocatedDataList.stream().map(AbstractBaseDomain::getId)
                .collect(Collectors.toList());
        assertThat(userIds.size(), greaterThan(0));
        assertThat(userIds, hasItems("4c66b48a5cfa4886a4c5861cb01eae02", "a8026654f1054c6bb65b00c5128a770e",
                "78a510f9289c4ca7b299b433078e64e6"));
    }

    @Test
    public void getGroupAllThreeData() {
        setGroupThreeDoMain();
        Map<String, Object> map = sysUserRoleService.getGroupAllThreeData();
        System.out.println("结果集：" + map.get("allThreeDataList").toString());
        assertThat(((ArrayList) map.get("allThreeDataList")).size(), greaterThan(1));
    }

    private void setAppThreeDoMain() {
        saveThreeRoleTest();
        SysUserDO sysUserDO = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "appceshiguanliyuan"));
        sysUserDO.setRoleType(111);
        List<SysGroupAppDO> appDOS = new ArrayList<>(16);
        appDOS.add(sysGroupAppService.getById("bt-ceshidemo222"));
        sysUserDO.setApps(appDOS);
        List<OrgOrganizationDO> orgs = new ArrayList<>(16);
        orgs.add(orgOrganizationService.getById("89a06f1243a74a9fbe20fdf08c9f27cd"));
        orgs.add(orgOrganizationService.getById("af562c26e35d44dbb82c1cf3b56ec112"));
        sysUserDO.setOrganizes(orgs);
        List<SysUserGroupDO> userGroupDOS = new ArrayList<>(16);
        userGroupDOS.add(sysUserGroupService.getById("323423"));
        userGroupDOS.add(sysUserGroupService.getById("3454"));
        sysUserDO.setUserGroups(userGroupDOS);
        sysUserService.saveUserAppControlDomain(sysUserDO);
        SysAuthScopeAppDO sysAuthScopeAppDO = sysAuthScopeAppService.getOne(new QueryWrapper<SysAuthScopeAppDO>()
                .eq("user_id", sysUserDO.getId()).eq("is_activated", 0).eq("app_id", "bt-ceshidemo222"));
        assertThat(sysAuthScopeAppDO != null, is(true));
        List<String> ids = new ArrayList<>(16);
        ids.add("89a06f1243a74a9fbe20fdf08c9f27cd");
        ids.add("af562c26e35d44dbb82c1cf3b56ec112");
        Integer count = sysAuthScopeOrgService.count(new QueryWrapper<SysAuthScopeOrgDO>()
                .in("organization_id", ids).eq("user_id", sysUserDO.getId()).eq("is_activated", 0));
        assertThat(count, is(2));

        List<String> ids2 = new ArrayList<>(16);
        ids2.add("323423");
        ids2.add("3454");
        Integer count2 = sysAuthScopeUserGroupService.count(new QueryWrapper<SysAuthScopeUserGroupDO>()
                .in("user_group_id", ids2).eq("user_id", sysUserDO.getId()).eq("is_activated", 0));
        assertThat(count2, is(2));
    }

    @Test
    public void getAppToBeActivatedDataTest() {
        setAppThreeDoMain();
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", "9e92cb17c7bf4af9b6072520d42ea103");
        List<SysUserDO> sysUserDOS = sysUserRoleService.getAppToBeActivatedData(params);
        System.out.println("结果集：" + sysUserDOS.toString());
        assertThat(sysUserDOS.size(), is(1));
        assertThat(sysUserDOS.get(0).getOrganizes().get(0).getName(), is("单体测试二级集团1"));
        assertThat(sysUserDOS.get(0).getOrganizes().get(1).getName(), is("单体测试三级集团1"));
        assertThat(sysUserDOS.get(0).getApps().get(0).getName(), is("单体测试应用222"));
        assertThat(sysUserDOS.get(0).getUserGroups().get(0).getName(), is("测试一级用户组1"));
        assertThat(sysUserDOS.get(0).getUserGroups().get(1).getName(), is("测试顶级用户组1"));
    }

    @Test
    public void setAppActivatedTest() {
        setAppThreeDoMain();
        Map<String, String> map = new HashMap<>();
        String id = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "appceshiguanliyuan")).getId();
        map.put("id", id);
        map.put("roleType", "111");
        boolean flg = sysUserRoleService.setAppActivated(map);
        assertThat(flg, is(true));
        assertThat(sysUserRoleService.count(new QueryWrapper<SysUserRoleDO>()
                .eq("role_id", "appadmin").eq("user_id", id)), is(1));
        assertThat(sysUserService.getById(id).getUserType(), is(2));
        assertThat(sysAuthScopeAppService.count(new QueryWrapper<SysAuthScopeAppDO>()
                .eq("is_activated", 1).eq("user_id", id)), is(1));
        assertThat(sysAuthScopeOrgService.count(new QueryWrapper<SysAuthScopeOrgDO>()
                .eq("is_activated", 1).eq("user_id", id)), is(2));
        assertThat(sysAuthScopeUserGroupService.count(new QueryWrapper<SysAuthScopeUserGroupDO>()
                .eq("is_activated", 1).eq("user_id", id)), is(2));
        assertThat(sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", id)), greaterThan(0));
        System.out.println("有效权限结果集：" + sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", id)).toString());
        System.out.println("应用授控域：" + sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                .eq("user_id", id)).toString());
        System.out.println("组织授控域：" + sysAuthScopeOrgService.list(new QueryWrapper<SysAuthScopeOrgDO>()
                .eq("user_id", id)).toString());
        System.out.println("用户组授控域：" + sysAuthScopeUserGroupService.list(new QueryWrapper<SysAuthScopeUserGroupDO>()
                .eq("user_id", id)).toString());

    }

    @Test
    public void getAppAllocatedDataTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", "9e92cb17c7bf4af9b6072520d42ea103");
        Map<String, Object> map = sysUserRoleService.getAppAllocatedData(params);
        assertThat(((ArrayList) map.get("allocatedDataList")).size(), is(3));
        System.out.println("结果集：" + map.get("allocatedDataList").toString());
    }

    @Test
    public void getAppAllThreeDataTest() {
        setAppThreeDoMain();
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", "9e92cb17c7bf4af9b6072520d42ea103");
        Map<String, Object> map = sysUserRoleService.getAppAllThreeData(params);
        System.out.println("结果集：" + map.get("allThreeDataList").toString());
        assertThat(((ArrayList) map.get("allThreeDataList")).size(), is(4));
    }

    @Test
    public void getRolesByUserIdTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", "bt-ceshidemo111");
        params.put("userId", "f0998046ae844ed4b1c1dab4e490f62e");
        List<SysRoleDO> sysRoleDOS = sysUserRoleService.getRolesByUserId(params);
        assertThat(sysRoleDOS.size(), is(2));
        assertThat(sysRoleDOS.get(0).getName(), is("单体测试角色1"));
        assertThat(sysRoleDOS.get(1).getName(), is("单体测试角色2"));
        System.out.println("结果集：" + sysRoleDOS.toString());
    }

    @Test
    public void getUsersByRoleIdTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", "3aca40d1a6594410bc599b6d3f2821f7");
        params.put("appId", "bt-ceshidemo111");
        List<SysUserDO> sysUserDOS = sysUserRoleService.getUsersByRoleId(params);
        assertThat(sysUserDOS.size(), is(1));
        assertThat(sysUserDOS.get(0).getRealName(), is("单体测试用户1"));
        System.out.println("结果集:" + sysUserDOS.toString());
    }


    @Test
    public void saveRoleIdAndUserIdsTest() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("roleId", "3aca40d1a6594410bc599b6d3f2821f7");// 测试角色1
//        map.put("appId", "bt-ceshidemo111");
//        List<String> userIds = new ArrayList<>(16);
//        userIds.add("262432b31fd249ce8ade25aa1df39966");// 测试用户4
//        userIds.add("884b9b13c26740aaa07849bec202e104");// 测试用户5
//        map.put("userIds", userIds);
//        boolean flg = sysUserRoleService.saveRoleIdAndUserIds(map);
//        assertThat(flg, is(true));
//        assertThat(sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
//                .eq("app_id", "bt-ceshidemo111").eq("user_id", "262432b31fd249ce8ade25aa1df39966")), is(1));
//        assertThat(sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
//                .eq("app_id", "bt-ceshidemo111").eq("user_id", "884b9b13c26740aaa07849bec202e104")), is(1));
//        assertThat(sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
//                .eq("app_id", "bt-ceshidemo111").eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")), is(1));
//        assertThat(sysUserRoleService.count(new QueryWrapper<SysUserRoleDO>()
//                        .eq("user_id", "262432b31fd249ce8ade25aa1df39966").eq("role_id",
//                        "3aca40d1a6594410bc599b6d3f2821f7"))
//                , is(1));
//        assertThat(sysUserRoleService.count(new QueryWrapper<SysUserRoleDO>()
//                        .eq("user_id", "884b9b13c26740aaa07849bec202e104").eq("role_id",
//                        "3aca40d1a6594410bc599b6d3f2821f7"))
//                , is(1));
    }

    @Test
    public void getWaitActiveUserDataTest() {
        saveRoleIdAndUserIdsTest();
        Map<String, Object> params = new HashMap<>();
        params.put("appId", "bt-ceshidemo111");
        Map<String, Object> map = sysUserRoleService.getWaitActiveUserData(params);
        System.out.println("结果集：" + map.toString());
        assertThat(((ArrayList) map.get("sysUserDOS")).size(), is(3));
        assertThat(((SysUserDO) ((ArrayList) map.get("sysUserDOS")).get(0)).getRealName(), is("单体测试用户1"));
        assertThat(((SysUserDO) ((ArrayList) map.get("sysUserDOS")).get(1)).getRealName(), is("单体测试用户4"));
        assertThat(((SysUserDO) ((ArrayList) map.get("sysUserDOS")).get(2)).getRealName(), is("单体测试用户5"));
    }

    @Test
    public void saveUserIdAndRoleIdsTest() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("userId", "f0998046ae844ed4b1c1dab4e490f62e");// 测试用户1
//        map.put("appId", "bt-ceshidemo111");
//        List<String> roleIds = new ArrayList<>(16);
//        roleIds.add("6aca40d1a6594410bc599b6d3f2821f1");// 测试角色4
//        roleIds.add("7aca40d1a6594410bc599b6d3f2821f2");// 测试角色5
//        map.put("roleIds", roleIds);
//        boolean flg = sysUserRoleService.saveUserIdAndRoleIds(map);
//        assertThat(flg, is(true));
//        assertThat(sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
//                .eq("app_id", "bt-ceshidemo111").eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")), is(1));
//        assertThat(sysUserRoleService.count(new QueryWrapper<SysUserRoleDO>()
//                .eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")), is(2));
//        assertThat(sysUserRoleService.count(new QueryWrapper<SysUserRoleDO>()
//                        .eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e").eq("role_id",
//                        "6aca40d1a6594410bc599b6d3f2821f1"))
//                , is(1));
//        assertThat(sysUserRoleService.count(new QueryWrapper<SysUserRoleDO>()
//                        .eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e").eq("role_id",
//                        "7aca40d1a6594410bc599b6d3f2821f2"))
//                , is(1));
    }

    @Test
    public void activeByUserIdTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "f0998046ae844ed4b1c1dab4e490f62e");// 测试用户1
        map.put("appId", "bt-ceshidemo111");
        List<String> roleIds = new ArrayList<>(16);
        roleIds.add("6aca40d1a6594410bc599b6d3f2821f1");// 测试角色4
        roleIds.add("7aca40d1a6594410bc599b6d3f2821f2");// 测试角色5
        map.put("roleIds", roleIds);
        sysUserRoleService.saveUserIdAndRoleIds(map);
        boolean flg = sysUserRoleService.activeByUserId(map);
        assertThat(flg, is(true));
        assertThat(sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")), is(0));
        assertThat(sysUserRoleLvService.count(new QueryWrapper<SysUserRoleLvDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")), is(2));
        String lvId = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
                .eq("is_latest", 1).eq("app_id", "bt-ceshidemo111").eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")
        ).getId();
        assertThat(sysUserRoleVService.count(new QueryWrapper<SysUserRoleVDO>()
                .eq("lv_id", lvId)), is(2));
    }

//    @Test
//    public void getEffectiveRoleData() {
//    }

    @Test
    public void getEffectiveUserDatasTest() {
        List<String> roleIds2 = new ArrayList<>(16);
        roleIds2.add("3aca40d1a6594410bc599b6d3f2821f7");// 测试角色1
        roleIds2.add("4aca40d1a6594410bc599b6d3f2821f8");// 测试角色2
        List<String> userIds = sysUserRoleService.getEffectiveUserDatas(roleIds2);
        System.out.println("结果集：" + userIds.toString());
        assertThat(userIds.size(), is(1));
        assertThat(userIds.get(0), is("f0998046ae844ed4b1c1dab4e490f62e"));
    }

    @Test
    public void getEffectiveUserDataTest() {
        List<String> userIds = sysUserRoleService.getEffectiveUserData("4aca40d1a6594410bc599b6d3f2821f8");
        System.out.println("结果集：" + userIds.toString());
        assertThat(userIds.size(), is(1));
        assertThat(userIds.get(0), is("f0998046ae844ed4b1c1dab4e490f62e"));
    }

    @Test
    public void allActiveTest() {
//        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", "3aca40d1a6594410bc599b6d3f2821f7");// 测试角色1
        map.put("appId", "bt-ceshidemo111");
        List<String> userIds = new ArrayList<>(16);
        userIds.add("262432b31fd249ce8ade25aa1df39966");// 测试用户4
        userIds.add("884b9b13c26740aaa07849bec202e104");// 测试用户5
        map.put("userIds", userIds);
        sysUserRoleService.saveRoleIdAndUserIds(map);
        boolean flg = sysUserRoleService.allActive(map);
        assertThat(flg, is(true));
        assertThat(sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "262432b31fd249ce8ade25aa1df39966")), is(0));
        assertThat(sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "884b9b13c26740aaa07849bec202e104")), is(0));
        assertThat(sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")), is(0));

        assertThat(sysUserRoleLvService.count(new QueryWrapper<SysUserRoleLvDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")), is(2));
        String lvId = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
                .eq("is_latest", 1).eq("app_id", "bt-ceshidemo111").eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")
        ).getId();
        System.out.println("--------" + sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>()
                .eq("lv_id", lvId)).toString());
        assertThat(sysUserRoleVService.count(new QueryWrapper<SysUserRoleVDO>()
                .eq("lv_id", lvId)), is(1));


        assertThat(sysUserRoleLvService.count(new QueryWrapper<SysUserRoleLvDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "262432b31fd249ce8ade25aa1df39966")), is(1));
        String lvId2 = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
                .eq("is_latest", 1).eq("app_id", "bt-ceshidemo111").eq("user_id", "262432b31fd249ce8ade25aa1df39966")
        ).getId();
        assertThat(sysUserRoleVService.count(new QueryWrapper<SysUserRoleVDO>()
                .eq("lv_id", lvId2)), is(1));

        assertThat(sysUserRoleLvService.count(new QueryWrapper<SysUserRoleLvDO>()
                .eq("app_id", "bt-ceshidemo111").eq("user_id", "884b9b13c26740aaa07849bec202e104")), is(1));
        String lvId3 = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
                .eq("is_latest", 1).eq("app_id", "bt-ceshidemo111").eq("user_id", "884b9b13c26740aaa07849bec202e104")
        ).getId();
        assertThat(sysUserRoleVService.count(new QueryWrapper<SysUserRoleVDO>()
                .eq("lv_id", lvId3)), is(1));
    }

    @Test
    public void getChangeRoleUserList() {
        String token = login("appceshiren3");
        SecurityUtils.TEST_TOKEN = token;
        Map<String, Object> map = new HashMap<>();
        map.put("appId", "bt-ceshidemo111");
        map.put("beginTime", "");
        map.put("endTime", "");
        List<SysUserDO> sysUserDOS = sysUserRoleService.getChangeRoleUserList(map);
        System.out.println("结果集：" + sysUserDOS.toString());
        assertThat(sysUserDOS.size(), is(1));
        assertThat(sysUserDOS.get(0).getRealName(), is("单体测试用户1"));
    }

    @Test
    public void refreshAppThreeAuthTest() {
//        boolean flg = sysUserRoleService.refreshAppThreeAuth("9e92cb17c7bf4af9b6072520d42ea103");
//        assertThat(flg, is(true));
    }
}