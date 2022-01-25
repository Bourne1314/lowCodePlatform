package com.csicit.ace.platform.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysUserServiceTest extends BaseTest {

    @Test
    public void fillUserOrgAndGroupTest() {
        List<String> userIds = new ArrayList<>(16);
        userIds.add("f0998046ae844ed4b1c1dab4e490f62e");
        userIds.add("dca07269fe1c4fecb667c442c3ace476");
        List<SysUserDO> results = sysUserService.fillUserOrgAndGroup(userIds, null);
        if (results != null && results.size() > 0) {
            results.stream().forEach(result -> {
                assertThat(result.getGroupName(), is("单体测试顶级集团1"));
                assertThat(result.getOrganizationName(), is("单体测试顶级业务单元1"));
                System.out.println("用户名：" + sysUserService.getById(result.getId()).getUserName());
                System.out.println("用户所属集团名：" + result.getGroupName());
                System.out.println("用于所属组织名：" + result.getOrganizationName());
            });
        } else {
            assertThat(1, is(2));
        }

        List<SysUserDO> users = new ArrayList<>(16);
        users.add(sysUserService.getById("262432b31fd249ce8ade25aa1df39966"));
        users.add(sysUserService.getById("884b9b13c26740aaa07849bec202e104"));
        List<SysUserDO> results2 = sysUserService.fillUserOrgAndGroup(null, users);
        if (results2 != null && results2.size() > 0) {
            results2.stream().forEach(result -> {
                assertThat(result.getGroupName(), is("单体测试顶级集团1"));
                assertThat(result.getOrganizationName(), is("单体测试顶级业务单元1"));
                System.out.println("用户名：" + result.getUserName());
                System.out.println("用户所属集团名：" + result.getGroupName());
                System.out.println("用于所属组织名：" + result.getOrganizationName());
            });
        } else {
            assertThat(1, is(2));
        }
    }

    @Test
    public void saveUserTest() {
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setUserName("ceshiuser6");
        sysUserDO.setRealName("单体测试用户6");
        sysUserDO.setPersonDocId("6fb1542175fc41f3adf1aefad04584ce");
        sysUserDO.setSecretLevel(5);
        sysUserDO.setSortIndex(9978);
        sysUserDO.setIpBind(1);
        sysUserDO.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        sysUserDO.setOrganizationId("b96daa08d59c467a9bd6cca17e46f53f");
        sysUserDO.setIpAddress("192.168.19.99,192.168.19.87");
        R r = sysUserService.saveUser(sysUserDO);
        System.out.println(r.get("msg"));
        assertThat("保存成功!", is(r.get("msg")));
        SysUserDO sysUserDO1 = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "ceshiuser6"));
        assertThat(sysUserDO1 != null, is(true));
        System.out.println("用户信息：" + sysUserDO1.toString());
        sysUserService.remove(new QueryWrapper<SysUserDO>()
                .eq("user_name", "ceshiuser6"));

        SysUserDO admin = new SysUserDO();
        admin.setUserName("ceshiadmin1");
        admin.setRealName("测试管理员1");
        admin.setPassword("Jari@111");
        admin.setSecretLevel(2);
        admin.setSortIndex(9778);
        admin.setIpBind(1);
        admin.setIpAddress("192.168.19.99,192.168.19.87");
        R r1 = sysUserService.saveUser(admin);
        System.out.println(r1.get("msg"));
        assertThat("保存成功!", is(r.get("msg")));
        SysUserDO sysUserDO3 = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "ceshiadmin1"));
        assertThat(sysUserDO3 != null, is(true));
        System.out.println("用户信息：" + sysUserDO3.toString());
        sysUserService.remove(new QueryWrapper<SysUserDO>()
                .eq("user_name", "ceshiadmin1"));
    }

    @Test
    public void initUserTest() {
        SysUserDO auditor = new SysUserDO();
        String auditorId = UuidUtils.createUUID();
        auditor.setId(auditorId);
        auditor.setUserType(0);
        auditor.setUserName("auditor11");
        auditor.setRealName("租户安全审计员11");
        auditor.setPassword("Jari@716");
        auditor.setSecretLevel(5);
        auditor.setIpBind(1);
        auditor.setIpAddress("192.168.19.99");
        boolean flg = sysUserService.initUser(auditor);
        assertThat(flg, is(true));
        assertThat(sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "auditor11")) != null, is(true));
    }

    @Test
    public void updateUserTest() {
        SysUserDO sysUserDO = sysUserService.getById("f0998046ae844ed4b1c1dab4e490f62e");
        System.out.println("修改前用户：" + sysUserDO.toString());
        sysUserDO.setRealName("修改-测试用户");
        sysUserDO.setEmail("123@qq.com");
        sysUserDO.setPhoneNumber("12345678891");
        R r = sysUserService.updateUser(sysUserDO);
        System.out.println(r.get("msg"));
        assertThat("更新成功!", is(r.get("msg")));
        SysUserDO sysUserDO2 = sysUserService.getById("f0998046ae844ed4b1c1dab4e490f62e");
        assertThat(sysUserDO2.getRealName(), is("修改-测试用户"));
        assertThat(sysUserDO2.getEmail(), is("123@qq.com"));
        assertThat(sysUserDO2.getPhoneNumber(), is("12345678891"));
        System.out.println("修改后用户：" + sysUserDO2);
    }

    @Test
    public void deleteUserTest() {
        List<String> ids = new ArrayList<>(16);
        ids.add("f0998046ae844ed4b1c1dab4e490f62e");
        ids.add("dca07269fe1c4fecb667c442c3ace476");
        System.out.println("删除前用户：" + sysUserService.list(new QueryWrapper<SysUserDO>()
                .in("id", ids)).toString());
        R r = sysUserService.deleteUser(ids.toArray(new String[2]));
        System.out.println(r.get("msg"));
        assertThat("删除成功!", is(r.get("msg")));
        assertThat(sysUserService.getById("f0998046ae844ed4b1c1dab4e490f62e").getUserName().substring(0, 3), is("del"));
        assertThat(sysUserService.getById("dca07269fe1c4fecb667c442c3ace476").getUserName().substring(0, 3), is("del"));
        System.out.println("删除后用户：" + sysUserService.list(new QueryWrapper<SysUserDO>()
                .in("id", ids)).toString());
    }

//    @Test
//    public void removeUsers() {
//    }

    @Test
    public void persondocTest() {
        SysUserDO sysUserDO = sysUserService.getById("f0998046ae844ed4b1c1dab4e490f62e");
        System.out.println("修改前用户：" + sysUserDO.toString());
        System.out.println("修改前档案：" + bdPersonDocService.getById(sysUserDO.getPersonDocId()).getName());
        Map<String, String> map = new HashMap<>();
        map.put("personDocId", "6fb1542175fc41f3adf1aefad04584ce");
        map.put("userId", "f0998046ae844ed4b1c1dab4e490f62e");
        R r = sysUserService.persondoc(map);
        System.out.println(r.get("msg"));
        assertThat("操作成功！", is(r.get("msg")));
        assertThat(sysUserService.getById("f0998046ae844ed4b1c1dab4e490f62e").getPersonDocId(), is
                ("6fb1542175fc41f3adf1aefad04584ce"));
        System.out.println("修改后用户：" + sysUserService.getById("f0998046ae844ed4b1c1dab4e490f62e"));
        System.out.println("修改后档案：" + bdPersonDocService.getById(sysUserService.getById
                ("f0998046ae844ed4b1c1dab4e490f62e").getPersonDocId()).getName());

    }

    @Test
    public void authenticateTest() {
        boolean flg = sysUserService.authenticate("ceshiuser1", "Jari@716");
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
    }

//    @Test
//    public void validateCaptcha() {
//    }

//    @Test
//    public void resetDefaultPassword() {
//    }

    @Test
    public void updatePasswordTest() {
        R r = sysUserService.updatePassword("ceshiuser1", "Jari@711", false);
        System.out.println(r.get("msg"));
        assertThat("保存成功!", is(r.get("msg")));
    }

    @Test
    public void unlockUserTest() {
        SysUserDO sysUserDO = sysUserService.getById("f0998046ae844ed4b1c1dab4e490f62e");
        sysUserDO.setFailLoginTimes(5);
        sysUserDO.setUnlockTime(LocalDateTime.now());
        Boolean flg = sysUserService.unlockUser("f0998046ae844ed4b1c1dab4e490f62e");
        assertThat(flg, is(true));
        assertThat(null, is(sysUserService.getById("f0998046ae844ed4b1c1dab4e490f62e").getUnlockTime()));
        assertThat(0, is(sysUserService.getById("f0998046ae844ed4b1c1dab4e490f62e").getFailLoginTimes()));
    }

    @Test
    public void resetUserPasswordTest() {
        R r = sysUserService.resetUserPassword("ceshiuser1");
        System.out.println(r.get("msg"));
        assertThat("操作成功！", is(r.get("msg")));
    }

    @Test
    public void saveUserGroupControlDomainTest() {
        SysUserDO sysUserDO = sysUserService.getById("4c66b48a5cfa4886a4c5861cb01eae02");//集团管理员
        System.out.println("修改前用户信息：" + sysUserDO);
        System.out.println("修改前用户集团管控域表信息：" + sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                .eq("user_id", "4c66b48a5cfa4886a4c5861cb01eae02")).toString());
        List<OrgGroupDO> groups = new ArrayList<>(16);
        groups.add(orgGroupService.getById("b190c25cb0234d51ab6de0bda198891b"));// 单体测试一级集团2
        groups.add(orgGroupService.getById("af562c26e35d44dbb82c1cf3b56ec112"));// 单体测试二级集团1
        groups.add(orgGroupService.getById("89a06f1243a74a9fbe20fdf08c9f27cd"));// 单体测试三级集团1
        sysUserDO.setGroups(groups);
        boolean flg = sysUserService.saveUserGroupControlDomain(sysUserDO);
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        assertThat(sysUserAdminOrgService.count(new QueryWrapper<SysUserAdminOrgDO>()
                .eq("is_activated", 0).eq("user_id", "4c66b48a5cfa4886a4c5861cb01eae02")), is(3));
        System.out.println("修改后用户信息：" + sysUserService.getById("4c66b48a5cfa4886a4c5861cb01eae02"));
        System.out.println("修改后用户集团管控域表信息：" + sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                .eq("user_id", "4c66b48a5cfa4886a4c5861cb01eae02")).toString());

    }

    @Test
    public void saveUserAppControlDomainTest() {
        SysUserDO sysUserDO = sysUserService.getById("681cead3d0764d24b261fe3d531ad775");
        System.out.println("修改前用户信息：" + sysUserDO.toString());
        System.out.println("修改前应用授控域表信息：" + sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                .eq("user_id", "681cead3d0764d24b261fe3d531ad775")).toString());
        System.out.println("修改前组织授控域表信息：" + sysAuthScopeOrgService.list(new QueryWrapper<SysAuthScopeOrgDO>()
                .eq("user_id", "681cead3d0764d24b261fe3d531ad775")).toString());
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
        boolean flg = sysUserService.saveUserAppControlDomain(sysUserDO);
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
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
        System.out.println("修改后用户信息：" + sysUserService.getById("681cead3d0764d24b261fe3d531ad775"));
        System.out.println("修改后应用授控域表信息：" + sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                .eq("user_id", "681cead3d0764d24b261fe3d531ad775")).toString());
        System.out.println("修改后组织授控域表信息：" + sysAuthScopeOrgService.list(new QueryWrapper<SysAuthScopeOrgDO>()
                .eq("user_id", "681cead3d0764d24b261fe3d531ad775")).toString());
    }

    @Test
    public void updThreeTenantsIpAddressTest() {
        System.out.println("修改前admin的IP：" + sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "admin")).getIpAddress());
        System.out.println("修改前sec的IP：" + sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "sec")).getIpAddress());
        System.out.println("修改前auditor的IP：" + sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "auditor")).getIpAddress());
        Map<String, String> map = new HashMap<>();
        map.put("adminIpAddress", "192.168.20.20,192.168.20.22,192.168.20.21");
        map.put("secIpAddress", "192.168.20.23");
        map.put("auditorIpAddress", "192.168.20.25");
        boolean flg = sysUserService.updThreeTenantsIpAddress(map);
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        assertThat(sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "admin")).getIpAddress(), is("192.168.20.20,192.168.20.22,192.168.20.21"));
        assertThat(sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "sec")).getIpAddress(), is("192.168.20.23"));
        assertThat(sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "auditor")).getIpAddress(), is("192.168.20.25"));
        System.out.println("修改后admin的IP：" + sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "admin")).getIpAddress());
        System.out.println("修改后sec的IP：" + sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "sec")).getIpAddress());
        System.out.println("修改后auditor的IP：" + sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "auditor")).getIpAddress());
    }
}