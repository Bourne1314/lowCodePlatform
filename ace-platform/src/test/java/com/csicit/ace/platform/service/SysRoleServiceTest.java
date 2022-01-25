package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.common.pojo.domain.SysAuthRoleLvDO;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Transactional
public class SysRoleServiceTest extends BaseTest {

    @Test
    public void infoRoleTest() {
        SysRoleDO sysRoleDO = sysRoleService.infoRole("3aca40d1a6594410bc599b6d3f2821f7");
        System.out.println("结果为：" + sysRoleDO.toString());
        assertThat(sysRoleDO.getMRoles().size(), is(2));
        assertThat(sysRoleDO.getMRoles().get(0).getName(), is("单体测试互斥角色1"));
        assertThat(sysRoleDO.getMRoles().get(1).getName(), is("单体测试互斥角色2"));
        assertThat(sysRoleDO.getCRoles().size(), is(2));
        assertThat(sysRoleDO.getCRoles().get(0).getName(), is("单体测试下级角色2"));
        assertThat(sysRoleDO.getCRoles().get(1).getName(), is("单体测试下级角色1"));
    }

    @Test
    public void saveRoleTest() {
        SysRoleDO sysRoleDO = new SysRoleDO();
        sysRoleDO.setAppId("bt-ceshidemo111");
        sysRoleDO.setName("测试角色1");
        sysRoleDO.setRoleExplain("测试角色说明");
        sysRoleDO.setRemark("测试说明");
        boolean flg = sysRoleService.saveRole(sysRoleDO);
        assertThat(flg, is(true));
        SysRoleDO sysRoleDO1 = sysRoleService.getOne(new QueryWrapper<SysRoleDO>()
                .eq("name", "测试角色1"));
        assertThat(sysRoleDO1 != null, is(true));
        System.out.println("新建角色信息：" + sysRoleDO1.toString());
    }

    @Test
    public void updateRoleTest() {
        SysRoleDO sysRoleDO = sysRoleService.getById("3aca40d1a6594410bc599b6d3f2821f7");
        System.out.println("修改前角色：" + sysRoleDO.toString());
        sysRoleDO.setName("修改后-测试角色1");
        sysRoleDO.setRoleExplain("修改后-测试角色说明");
        sysRoleDO.setRemark("修改后-测试角色备注");
        boolean flg = sysRoleService.updateRole(sysRoleDO);
        assertThat(flg, is(true));
        SysRoleDO sysRoleDO1 = sysRoleService.getById("3aca40d1a6594410bc599b6d3f2821f7");
        assertThat(sysRoleDO1.getName(), is("修改后-测试角色1"));
        assertThat(sysRoleDO1.getRoleExplain(), is("修改后-测试角色说明"));
        assertThat(sysRoleDO1.getRemark(), is("修改后-测试角色备注"));
        System.out.println("修改后角色：" + sysRoleDO1);
    }

    @Test
    public void deleteByIdsTest() {
        List<String> ids = new ArrayList<>(16);
        ids.add("3aca40d1a6594410bc599b6d3f2821f7");
        ids.add("4aca40d1a6594410bc599b6d3f2821f8");
        boolean flg = sysRoleService.deleteByIds(ids);
        assertThat(flg, is(true));
        assertThat(sysRoleService.count(new QueryWrapper<SysRoleDO>()
                .in("id", ids)), is(0));
        List<SysAuthRoleLvDO> sysAuthRoleLvDOs = sysAuthRoleLvService.list(new QueryWrapper<SysAuthRoleLvDO>()
                .in("role_id", ids));
        sysAuthRoleLvDOs.stream().forEach(sysAuthRoleLvDO -> {
            assertThat(sysAuthRoleLvDO.getLastVersion(), is(0));
        });
        List<SysAuthMixDO> sysAuthMixDOS = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e"));
        assertThat(sysAuthMixDOS.size(), is(0));
    }

    @Test
    public void getChildAndMutualRoleListTest1() {
        SysRoleDO sysRoleDO = sysRoleService.getById("3aca40d1a6594410bc599b6d3f2821f7");
        System.out.println("当前角色信息：" + sysRoleDO.toString());
        List<SysRoleDO> croles = sysRoleService.getChildAndMutualRoleList("bt-ceshidemo111",
                "3aca40d1a6594410bc599b6d3f2821f7", "2");
        assertThat(croles.size(), is(7));
        System.out.println("可作为下级角色列表：" + croles.toString());
    }

    @Test
    public void getChildAndMutualRoleListTest2() {
        SysRoleDO sysRoleDO = sysRoleService.getById("3aca40d1a6594410bc599b6d3f2821f7");
        System.out.println("当前角色信息：" + sysRoleDO.toString());
        List<SysRoleDO> mroles = sysRoleService.getChildAndMutualRoleList("bt-ceshidemo111",
                "3aca40d1a6594410bc599b6d3f2821f7", "3");
        assertThat(mroles.size(), is(6));
        System.out.println("可作为互斥级角色列表：" + mroles.toString());
    }
}