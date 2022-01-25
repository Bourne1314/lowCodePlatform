package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.domain.SysMenuDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SysMenuServiceTest extends BaseTest {

    @Test
    public void listMenuTreeTest() {
        String appId = "bt-ceshidemo111";
        List<SysMenuDO> sysMenuDOS = sysMenuService.listMenuTree(appId);
        assertThat(sysMenuDOS.size(), is(2));
        assertThat(sysMenuDOS.get(0).getName(), is("首页"));
        System.out.println("结果集1：" + sysMenuDOS.get(0).toString());
        assertThat(sysMenuDOS.get(1).getName(), is("单体测试顶级菜单1"));
        System.out.println("结果集2：" + sysMenuDOS.get(1).toString());
    }

    @Test
    public void listSideTreeTest() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        String appId = "platform";
        List<SysMenuDO> sysMenuDOS = sysMenuService.listSideTree(appId);
        sysMenuDOS.stream().forEach(sysMenuDO -> {
            System.out.println("结果集：" + sysMenuDO.toString());
        });
        assertThat(sysMenuDOS.size(), is(6));
        assertThat(sysMenuDOS.get(0).getName(), is("首页"));
        assertThat(sysMenuDOS.get(1).getName(), is("系统管理"));
        assertThat(sysMenuDOS.get(2).getName(), is("配置管理"));
        assertThat(sysMenuDOS.get(3).getName(), is("工作流程"));
        assertThat(sysMenuDOS.get(4).getName(), is("数据展示"));
        assertThat(sysMenuDOS.get(5).getName(), is("应用配置"));
//        System.out.println("结果集:" + sysMenuDOS.toString());
    }

    @Test
    public void saveMenuTest() {
        SysMenuDO sysMenuDO = new SysMenuDO();
        sysMenuDO.setAppId("bt-ceshidemo111");
        sysMenuDO.setName("单体测试菜单112");
        sysMenuDO.setSortIndex(8786);
        sysMenuDO.setParentId("0");
        sysMenuDO.setType(0);
        sysMenuDO.setAddRelationFlag(1);
        boolean flg = sysMenuService.saveMenu(sysMenuDO);
        assertThat(flg, is(true));
        SysMenuDO result = sysMenuService.getOne(new QueryWrapper<SysMenuDO>()
                .eq("name", "单体测试菜单112"));
        assertThat((result == null), is(false));
        assertThat((sysAuthService.getById(result.getId()) == null), is(false));
        System.out.println("菜单结果集1:" + result);
        System.out.println("权限结果集1：" + sysAuthService.getById(result.getId()).toString());

        SysMenuDO sysMenuDO1 = new SysMenuDO();
        sysMenuDO1.setAppId("bt-ceshidemo111");
        sysMenuDO1.setName("单体测试子菜单434");
        sysMenuDO1.setSortIndex(5621);
        sysMenuDO1.setParentId("b7259562c1a84c5c9bb9754ad69acb37");
        sysMenuDO1.setType(1);
        sysMenuDO1.setAddRelationFlag(1);
        sysMenuDO1.setUrl("/ceshi/menu433");
        boolean flg1 = sysMenuService.saveMenu(sysMenuDO1);
        assertThat(flg1, is(true));
        SysMenuDO result2 = sysMenuService.getOne(new QueryWrapper<SysMenuDO>()
                .eq("name", "单体测试子菜单434"));
        assertThat((result2 == null), is(false));
        assertThat((sysAuthService.getById(result2.getId()) == null), is(false));
        System.out.println("菜单结果集2:" + result2);
        System.out.println("权限结果集2：" + sysAuthService.getById(result2.getId()).toString());
    }

    @Test
    public void updateMenuTest1() {
        SysMenuDO sysMenuDO = sysMenuService.getOne(new QueryWrapper<SysMenuDO>()
                .eq("name", "单体测试顶级菜单1"));
        System.out.println("修改前菜单：" + sysMenuDO.toString());
        sysMenuDO.setSortIndex(232);
        sysMenuDO.setAddRelationFlag(0);
        boolean flg = sysMenuService.updateMenu(sysMenuDO);
        assertThat(flg, is(true));
        SysAuthDO sysAuthDO = sysAuthService.getById(sysMenuDO.getId());
        assertThat((sysAuthDO == null), is(true));
        SysMenuDO newSysMenu = sysMenuService.getById(sysMenuDO.getId());
        assertThat(newSysMenu.getSortPath(), is("000232"));
        System.out.println("修改后菜单：" + newSysMenu);
    }

    @Test
    public void updateMenuTest2() {
        SysMenuDO sysMenuDO = sysMenuService.getOne(new QueryWrapper<SysMenuDO>()
                .eq("name", "单体测试三级菜单1"));
        System.out.println("修改前菜单：" + sysMenuDO.toString());
        sysMenuDO.setSortIndex(456);
        sysMenuDO.setUrl("/ceshi/xiugai/menusanji");
        sysMenuDO.setAddRelationFlag(1);
        boolean flg = sysMenuService.updateMenu(sysMenuDO);
        assertThat(flg, is(true));
        assertThat((sysAuthService.getById(sysMenuDO.getId()) == null), is(false));
        SysMenuDO newSysMenu = sysMenuService.getById(sysMenuDO.getId());
        assertThat(newSysMenu.getSortPath(), is("000696000010000010000456"));
        System.out.println("修改后菜单：" + newSysMenu);
        System.out.println("权限：" + sysAuthService.getById(sysMenuDO.getId()));
    }

    @Test
    public void updateMenuTest3() {
        SysMenuDO sysMenuDO = sysMenuService.getOne(new QueryWrapper<SysMenuDO>()
                .eq("name", "首页").eq("app_id", "bt-ceshidemo111"));
        System.out.println("修改前菜单：" + sysMenuDO.toString());
        System.out.println("修改前权限：" + sysAuthService.getById(sysMenuDO.getId()));
        sysMenuDO.setSortIndex(234);
        sysMenuDO.setAddRelationFlag(1);
        boolean flg = sysMenuService.updateMenu(sysMenuDO);
        assertThat(flg, is(true));
        SysMenuDO newSysMenu = sysMenuService.getById(sysMenuDO.getId());
        assertThat(newSysMenu.getSortPath(), is("000234"));
        SysAuthDO newSysAuthDO = sysAuthService.getById(sysMenuDO.getId());
        System.out.println("修改后菜单：" + newSysMenu);
        System.out.println("修改后权限：" + newSysAuthDO);
    }

    @Test
    public void deleteTest() {
        Map<String, Object> params1 = new HashMap<>();
        params1.put("deleteSons", false);
        List<String> ids = new ArrayList<>(16);
        ids.add("f1b6e0c95778480f9c8d1df50eaffc14");
        params1.put("ids", ids);
        R r = sysMenuService.delete(params1);
        System.out.println(r.get("msg"));
        assertThat("删除成功!", is(r.get("msg")));
        assertThat((sysMenuService.getById("f1b6e0c95778480f9c8d1df50eaffc14") == null), is(true));

        Map<String, Object> params2 = new HashMap<>();
        params2.put("deleteSons", true);
        List<String> ids2 = new ArrayList<>(16);
        ids2.add("b1d57ca7f29c49268b4a097cd4714288");
        params2.put("ids", ids2);
        R r2 = sysMenuService.delete(params2);
        System.out.println(r2.get("msg"));
        assertThat("删除成功!", is(r2.get("msg")));
        assertThat((sysMenuService.getById("b1d57ca7f29c49268b4a097cd4714288") == null), is(true));
        System.out.println("该应用下菜单：" + sysMenuService.list(new QueryWrapper<SysMenuDO>()
                .eq("app_id", "bt-ceshidemo111")).toString());
    }

    @Test
    public void moveMenuTest1() {
        SysMenuDO sysMenuDO = sysMenuService.getById("6b4ba2a597b54926bd0fa39c464dca30");
        System.out.println("变更前：" + sysMenuService.list(new QueryWrapper<SysMenuDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", sysMenuDO.getSortPath())).toString());
        Map<String, String> params = new HashMap<>();
        params.put("id", "6b4ba2a597b54926bd0fa39c464dca30");// 单体测试一级菜单1
        params.put("aftParentId", "0");
        params.put("aftParentName", "一级菜单");
        boolean flg = sysMenuService.moveMenu(params);
        assertThat(flg, is(true));
        SysMenuDO newSysMenu = sysMenuService.getById("6b4ba2a597b54926bd0fa39c464dca30");
        assertThat(newSysMenu.getSortPath(), is("000706"));
        assertThat(sysMenuService.count(new QueryWrapper<SysMenuDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", newSysMenu.getSortPath())), is(3));
        System.out.println("结果集：" + sysMenuService.list(new QueryWrapper<SysMenuDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", sysMenuDO.getSortPath())).toString());
    }

    @Test
    public void moveMenuTest2() {
        SysMenuDO sysMenuDO = sysMenuService.getById("7696ce52ff82454ca307267d4291233f");
        System.out.println("变更前：" + sysMenuService.list(new QueryWrapper<SysMenuDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", sysMenuDO.getSortPath())).toString());
        Map<String, String> params = new HashMap<>();
        params.put("id", "7696ce52ff82454ca307267d4291233f");// 单体测试二级菜单1
        params.put("aftParentId", "d5e18244f3a6463c81c6d8079ce9e031");
        params.put("aftParentName", "单体测试一级菜单2");
        boolean flg = sysMenuService.moveMenu(params);
        assertThat(flg, is(true));
        SysMenuDO newSysMenuDO = sysMenuService.getById("7696ce52ff82454ca307267d4291233f");
        assertThat(newSysMenuDO.getSortPath(), is("000696000020000010"));
        assertThat(sysMenuService.count(new QueryWrapper<SysMenuDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", sysMenuDO.getSortPath())), is(2));
        System.out.println("结果集：" + sysMenuService.list(new QueryWrapper<SysMenuDO>()
                .eq("app_id", "bt-ceshidemo111").likeRight("sort_path", sysMenuDO.getSortPath())).toString());
    }

    @Test
    public void getMenuInfoTest() {
        R r = sysMenuService.getMenuInfo("b1d57ca7f29c49268b4a097cd4714288");
        assertThat(((SysMenuDO) r.get("menu")).getName(), is("单体测试顶级菜单1"));
        assertThat(((ArrayList<String>) r.get("cids")), hasItems("b1d57ca7f29c49268b4a097cd4714288",
                "6b4ba2a597b54926bd0fa39c464dca30", "d5e18244f3a6463c81c6d8079ce9e031",
                "7696ce52ff82454ca307267d4291233f", "f1b6e0c95778480f9c8d1df50eaffc14"));
        assertThat(((ArrayList) r.get("cids")).size(), is(5));
        System.out.println("menu:" + r.get("menu"));
        System.out.println("cids:" + r.get("cids"));
        assertThat((r.get("menu") != null), is(true));
        assertThat((r.get("cids") != null), is(true));
    }
}