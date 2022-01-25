package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysGroupAppServiceTest extends BaseTest {

    @Test
    public void saveAppFromBdAppLibTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", "0e4b26e45d0348088503efc8768afcdc");// 单体测试一级集团1
        params.put("appName", "单体测试应用11");
        params.put("appId", "ed-dantiapp");
        params.put("checkFlg", "1");
        params.put("adminName", "dantiadmin");
        params.put("secName", "dantisec");
        params.put("auditorName", "dantiauditor");
        boolean flg = sysGroupAppService.saveAppFromBdAppLib(params);
        assertThat(flg, is(true));
        assertThat(sysGroupAppService.count(new QueryWrapper<SysGroupAppDO>()
                .eq("name", "单体测试应用11")), is(1));
        System.out.println("应用结果集：" + sysGroupAppService.getOne(new QueryWrapper<SysGroupAppDO>()
                .eq("name", "单体测试应用11")).toString());
        List<String> names = new ArrayList<>();
        names.add("dantiadmin");
        names.add("dantisec");
        names.add("dantiauditor");
        assertThat(sysUserService.count(new QueryWrapper<SysUserDO>()
                .in("user_name", names)), is(3));
        System.out.println("管理员结果集：" + sysUserService.list(new QueryWrapper<SysUserDO>()
                .in("user_name", names)).toString());

    }

    @Test
    public void listUserOrgAppTest() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        List<SysGroupAppDO> sysGroupAppDOS = sysGroupAppService.listUserOrgApp();
        assertThat(sysGroupAppDOS.size(), is(1));
        assertThat(sysGroupAppDOS.get(0).getId(), is("bt-ceshidemo111"));
        System.out.println("结果集：" + sysGroupAppDOS.toString());
    }

    @Test
    public void setMainAppTest() {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", "9e92cb17c7bf4af9b6072520d42ea103");
        params.put("appId", "bt-ceshidemo222");
        System.out.println("变更前：" + sysGroupAppService.getById("bt-ceshidemo222"));
        boolean flg = sysGroupAppService.setMainApp(params);
        assertThat(flg, is(true));
        assertThat(sysGroupAppService.getById("bt-ceshidemo222").getMainApp(), is(1));
        System.out.println("变更后：" + sysGroupAppService.getById("bt-ceshidemo222"));
    }

    @Test
    public void listAppHaveAuthTest() {
        List<SysGroupAppDO> sysGroupAppDOS = sysGroupAppService.listAppHaveAuth("9e92cb17c7bf4af9b6072520d42ea103");
        assertThat(sysGroupAppDOS.size(), is(2));
        assertThat(sysGroupAppDOS.get(0).getId(), is("bt-ceshidemo111"));
        assertThat(sysGroupAppDOS.get(1).getId(), is("bt-ceshidemo222"));
        System.out.println("结果集：" + sysGroupAppDOS.toString());
    }

    @Test
    public void listAppNoAuthTest() {
        List<SysGroupAppDO> sysGroupAppDOS = sysGroupAppService.listAppNoAuth("9e92cb17c7bf4af9b6072520d42ea103");
        assertThat(sysGroupAppDOS.size(), is(0));
        System.out.println("结果集：" + sysGroupAppDOS.toString());
    }

    @Test
    public void updateAppTest() {
        SysGroupAppDO instance = sysGroupAppService.getOne(new QueryWrapper<SysGroupAppDO>()
                .eq("name", "单体测试应用111"));
        System.out.println("修改前：" + instance.toString());
        instance.setSortIndex(345);
        instance.setSecretLevel(4);
        instance.setName("修改后-单体测试应用111");
        boolean flg = sysGroupAppService.updateApp(instance);
        assertThat(flg, is(true));
        SysGroupAppDO newGroupApp = sysGroupAppService.getById(instance.getId());
        assertThat(newGroupApp.getName(), is("修改后-单体测试应用111"));
        assertThat(newGroupApp.getSecretLevel(), is(4));
        assertThat(newGroupApp.getSortIndex(), is(345));
        System.out.println("修改后：" + newGroupApp);
    }

    @Test
    public void deleteAppTest() {
        List<String> ids = new ArrayList<>(16);
        ids.add("bt-ceshidemo111");
        ids.add("bt-ceshidemo222");
        boolean flg = sysGroupAppService.deleteApp(ids);
        assertThat(flg, is(true));
        assertThat(sysGroupAppService.count(new QueryWrapper<SysGroupAppDO>()
                .in("id", ids)), is(0));
    }
}