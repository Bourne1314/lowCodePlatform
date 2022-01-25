package com.csicit.ace.platform.service;

import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysUserRoleVServiceTest extends BaseTest {

    @Test
    public void getRoleHistoryByUserId() {
        Map<Integer, Object> map = sysUserRoleVService.getRoleHistoryByUserId("bt-ceshidemo111",
                "f0998046ae844ed4b1c1dab4e490f62e");
        assertThat(((ArrayList) map.get(0)).size(), is(2));
        assertThat(((ArrayList) map.get(0)).get(0), is("单体测试角色1"));
        assertThat(((ArrayList) map.get(0)).get(1), is("单体测试角色2"));
        System.out.println("结果集为：" + map.toString());
    }
}