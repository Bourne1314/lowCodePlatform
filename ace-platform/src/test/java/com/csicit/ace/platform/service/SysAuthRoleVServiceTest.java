package com.csicit.ace.platform.service;

import com.csicit.ace.common.pojo.domain.SysAuthRoleVDO;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysAuthRoleVServiceTest extends BaseTest {

    @Test
    public void getAuthHistoryByRoleIdTest() {
        Map<Integer, Object> map = sysAuthRoleVService.getAuthHistoryByRoleId("4aca40d1a6594410bc599b6d3f2821f8");
        assertThat(((ArrayList) map.get(0)).size(), is(2));
        assertThat(((SysAuthRoleVDO) ((ArrayList) map.get(0)).get(0)).getAuthId(), is
                ("92defb36651649f7a78e9e58e5fe850f"));
        assertThat(((SysAuthRoleVDO) ((ArrayList) map.get(0)).get(1)).getAuthId(), is
                ("1944b2e3e8664c69921a6164f81aac33"));
        System.out.println("结果集为：" + map.toString());
    }
}