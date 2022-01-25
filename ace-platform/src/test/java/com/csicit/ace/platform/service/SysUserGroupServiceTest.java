package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.pojo.domain.SysUserGroupDO;
import com.csicit.ace.common.pojo.domain.SysUserGroupUserDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysUserGroupServiceTest extends BaseTest {

    @Test
    public void updateTest1() {
        SysUserGroupDO sysUserGroupDO = sysUserGroupService.getOne(new QueryWrapper<SysUserGroupDO>()
                .eq("name", "测试顶级用户组1"));
        System.out.println("修改前用户组：" + sysUserGroupService.list(new QueryWrapper<SysUserGroupDO>()
                .likeRight("sort_path", sysUserGroupDO.getSortPath())).toString());
        sysUserGroupDO.setName("修改后-测试顶级用户组1");
        sysUserGroupDO.setSortIndex(123);
        boolean flg = sysUserGroupService.update(sysUserGroupDO);
        assertThat(flg, is(true));
        SysUserGroupDO sysUserGroupDO1 = sysUserGroupService.getById(sysUserGroupDO.getId());
        assertThat(sysUserGroupDO1.getName(), is("修改后-测试顶级用户组1"));
        assertThat(sysUserGroupDO1.getSortPath(), is("000123"));
        assertThat(sysUserGroupService.count(new QueryWrapper<SysUserGroupDO>()
                .likeRight("sort_path", sysUserGroupDO1.getSortPath())), is(5));
        System.out.println("修改后用户组：" + sysUserGroupService.list(new QueryWrapper<SysUserGroupDO>()
                .likeRight("sort_path", sysUserGroupDO1.getSortPath())).toString());
    }

    @Test
    public void updateTest2() {
        SysUserGroupDO sysUserGroupDO = sysUserGroupService.getOne(new QueryWrapper<SysUserGroupDO>()
                .eq("name", "测试二级用户组1"));
        System.out.println("修改前用户组：" + sysUserGroupService.list(new QueryWrapper<SysUserGroupDO>()
                .likeRight("sort_path", sysUserGroupDO.getSortPath())).toString());
        sysUserGroupDO.setName("修改后-测试二级用户组1");
        sysUserGroupDO.setSortIndex(123);
        boolean flg = sysUserGroupService.update(sysUserGroupDO);
        assertThat(flg, is(true));
        SysUserGroupDO sysUserGroupDO1 = sysUserGroupService.getById(sysUserGroupDO.getId());
        assertThat(sysUserGroupDO1.getSortPath(), is("000010000020000123"));
        assertThat(sysUserGroupDO1.getName(), is("修改后-测试二级用户组1"));
        assertThat(sysUserGroupService.count(new QueryWrapper<SysUserGroupDO>()
                .likeRight("sort_path", sysUserGroupDO1.getSortPath())), is(2));
        System.out.println("修改后用户组：" + sysUserGroupService.list(new QueryWrapper<SysUserGroupDO>()
                .likeRight("sort_path", sysUserGroupDO1.getSortPath())).toString());
    }

    @Test
    public void saveUserGroupTest() {
        SysUserGroupDO sysUserGroupDO = new SysUserGroupDO();
        sysUserGroupDO.setParentId("0");
        sysUserGroupDO.setName("测试用户组1");
        sysUserGroupDO.setSortIndex(232);
        sysUserGroupDO.setCode("1231");
        sysUserGroupDO.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        sysUserGroupDO.setOrganizationId("b96daa08d59c467a9bd6cca17e46f53f");
        sysUserGroupDO.setAppId("bt-ceshidemo111");
        boolean flg = sysUserGroupService.saveUserGroup(sysUserGroupDO);
        assertThat(flg, is(true));
        SysUserGroupDO sysUserGroupDO1 = sysUserGroupService.getOne(new QueryWrapper<SysUserGroupDO>()
                .eq("name", "测试用户组1"));
        assertThat(sysUserGroupDO1 != null, is(true));
        System.out.println("结果集：" + sysUserGroupDO1.toString());


        SysUserGroupDO sysUserGroupDO2 = new SysUserGroupDO();
        sysUserGroupDO2.setParentId("323423");
        sysUserGroupDO2.setName("测试用户组1111");
        sysUserGroupDO2.setSortIndex(112);
        sysUserGroupDO2.setCode("243");
        sysUserGroupDO2.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        sysUserGroupDO2.setOrganizationId("b96daa08d59c467a9bd6cca17e46f53f");
        sysUserGroupDO2.setAppId("bt-ceshidemo111");
        boolean flg2 = sysUserGroupService.saveUserGroup(sysUserGroupDO2);
        assertThat(flg2, is(true));
        SysUserGroupDO sysUserGroupDO3 = sysUserGroupService.getOne(new QueryWrapper<SysUserGroupDO>()
                .eq("name", "测试用户组1111"));
        assertThat(sysUserGroupDO3 != null, is(true));
        System.out.println("结果集2：" + sysUserGroupDO3.toString());

    }

    @Test
    public void deleteTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("deleteSons", false);
        List<String> ids = new ArrayList<>();
        ids.add("456456");
        params.put("ids", ids);
        R r = sysUserGroupService.delete(params);
        System.out.println(r.get("msg"));
        assertThat("删除成功!", is(r.get("msg")));
        assertThat((sysUserGroupService.getById("456456") == null), is(true));

        Map<String, Object> params2 = new HashMap<>();
        params2.put("deleteSons", true);
        List<String> ids2 = new ArrayList<>();
        ids2.add("323423");
        params2.put("ids", ids2);
        R r2 = sysUserGroupService.delete(params2);
        System.out.println(r2.get("msg"));
        assertThat("删除成功!", is(r2.get("msg")));
        assertThat((sysUserGroupService.getById("323423") == null), is(true));
    }

    @Test
    public void deleteUserTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", "323423");
        List<String> userIds = new ArrayList<>(16);
        userIds.add("262432b31fd249ce8ade25aa1df39966");
        userIds.add("884b9b13c26740aaa07849bec202e104");
        map.put("userIds", userIds);
        sysUserGroupService.addUser(map);
        R r = sysUserGroupService.deleteUser(map);
        System.out.println(r.get("msg"));
        assertThat("删除成功!", is(r.get("msg")));
        assertThat(sysUserGroupUserService.count(new QueryWrapper<SysUserGroupUserDO>()
                .eq("user_group_id", "323423")), is(0));
    }

    @Test
    public void addUserTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", "323423");
        List<String> userIds = new ArrayList<>(16);
        userIds.add("262432b31fd249ce8ade25aa1df39966");
        userIds.add("884b9b13c26740aaa07849bec202e104");
        map.put("userIds", userIds);
        R r = sysUserGroupService.addUser(map);
        System.out.println(r.get("msg"));
        assertThat("添加成功!", is(r.get("msg")));
        List<SysUserGroupUserDO> sysUserGroupUserDOS = sysUserGroupUserService.list(new
                QueryWrapper<SysUserGroupUserDO>()
                .eq("user_group_id", "323423"));
        assertThat(sysUserGroupUserDOS.get(0).getUserId(), is("262432b31fd249ce8ade25aa1df39966"));
        assertThat(sysUserGroupUserDOS.get(1).getUserId(), is("884b9b13c26740aaa07849bec202e104"));
        System.out.println("结果集：" + sysUserGroupUserDOS.toString());
    }

    @Test
    public void getUserGroupsByAppsTest() {
        SysUserDO sysUserDO = new SysUserDO();
        List<SysGroupAppDO> apps = new ArrayList<>(16);
        apps.add(sysGroupAppService.getById("bt-ceshidemo111"));
        sysUserDO.setApps(apps);
        List<SysUserGroupDO> sysUserGroupDOS = sysUserGroupService.getUserGroupsByApps(sysUserDO);
        assertThat(sysUserGroupDOS.size(), is(5));
        System.out.println("结果集：" + sysUserGroupDOS.toString());
    }
}