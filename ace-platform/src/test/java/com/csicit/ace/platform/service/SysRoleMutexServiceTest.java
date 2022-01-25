package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysRoleMutexDO;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.SysRoleMutexService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysRoleMutexServiceTest extends BaseTest {
    @Autowired
    SysRoleMutexService sysRoleMutexService;

    @Test
    public void saveMutexRolesTest() {
        String id = "3aca40d1a6594410bc599b6d3f2821f7";
        List<String> ids = new ArrayList<>();
        ids.add("6aca40d1a6594410bc599b6d3f2821f1");
        ids.add("5aca40d1a6594410bc599b6d3f2821f9");
        boolean flg = sysRoleMutexService.saveMutexRoles(id, ids);
        assertThat(flg, is(true));
        List<SysRoleMutexDO> sysRoleMutexDOS = sysRoleMutexService.list(new QueryWrapper<SysRoleMutexDO>().eq
                ("role_id", id));
        assertThat(sysRoleMutexDOS.size(), is(2));
        assertThat(sysRoleMutexDOS.get(0).getRoleMutexId(), is("6aca40d1a6594410bc599b6d3f2821f1"));
        assertThat(sysRoleMutexDOS.get(1).getRoleMutexId(), is("5aca40d1a6594410bc599b6d3f2821f9"));
        System.out.println("结果集：" + sysRoleMutexDOS.toString());
    }
}