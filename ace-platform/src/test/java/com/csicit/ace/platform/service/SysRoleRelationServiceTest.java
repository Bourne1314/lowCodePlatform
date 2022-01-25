package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysRoleRelationDO;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.SysRoleRelationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysRoleRelationServiceTest extends BaseTest {
    @Autowired
    SysRoleRelationService sysRoleRelationService;

    @Test
    public void saveChildRolesTest() {
        String id = "5aca40d1a6594410bc599b6d3f2821f9";// 角色3
        List<String> cids = new ArrayList<>();
        cids.add("6aca40d1a6594410bc599b6d3f2821f1");// 角色4
        cids.add("7aca40d1a6594410bc599b6d3f2821f2");// 角色5
        boolean flg = sysRoleRelationService.saveChildRoles(id, cids);
        assertThat(flg, is(true));
        List<SysRoleRelationDO> sysRoleRelationDO = sysRoleRelationService.list(new QueryWrapper<SysRoleRelationDO>()
                .eq("pid", id));
        assertThat(sysRoleRelationDO.get(0).getCid(), is("6aca40d1a6594410bc599b6d3f2821f1"));
        assertThat(sysRoleRelationDO.get(1).getCid(), is("7aca40d1a6594410bc599b6d3f2821f2"));
        System.out.println("结果集：" + sysRoleRelationDO.toString());
    }

    @Test
    public void saveParentRolesTest() {
        String id = "5aca40d1a6594410bc599b6d3f2821f9";// 角色3
        List<String> cids = new ArrayList<>();
        cids.add("6aca40d1a6594410bc599b6d3f2821f1");// 角色4
        cids.add("7aca40d1a6594410bc599b6d3f2821f2");// 角色5
        boolean flg = sysRoleRelationService.saveParentRoles(id, cids);
        assertThat(flg, is(true));
        List<SysRoleRelationDO> sysRoleRelationDO = sysRoleRelationService.list(new QueryWrapper<SysRoleRelationDO>()
                .eq("cid", id));
        assertThat(sysRoleRelationDO.get(0).getPid(), is("6aca40d1a6594410bc599b6d3f2821f1"));
        assertThat(sysRoleRelationDO.get(1).getPid(), is("7aca40d1a6594410bc599b6d3f2821f2"));
        System.out.println("结果集：" + sysRoleRelationDO.toString());
    }

    @Test
    public void getAllSuperRoleIds() {
        List<String> ids = sysRoleRelationService.getAllSuperRoleIds("33410fea0e1a491690931c8e6ec89a51");
        assertThat(ids.size(), is(2));
        assertThat(ids.get(0), is("79c10fea0e1a491690931c8e6ec89a51"));
        assertThat(ids.get(1), is("3aca40d1a6594410bc599b6d3f2821f7"));
        System.out.println("结果集：" + ids.toString());
    }
}