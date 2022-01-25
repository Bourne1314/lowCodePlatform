package com.csicit.ace.platform.service;

import com.csicit.ace.common.pojo.domain.SysAuthUserVDO;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysAuthUserVServiceTest extends BaseTest {

    @Test
    public void getAuthHistoryByUserIdTest() {
        Map<Integer, Object> map = sysAuthUserVService.getAuthHistoryByUserId("bt-ceshidemo111",
                "dca07269fe1c4fecb667c442c3ace476");
        assertThat(((ArrayList) map.get(0)).size(), is(2));
        assertThat(((SysAuthUserVDO) ((ArrayList) map.get(0)).get(0)).getAuthId(), is
                ("7eec28f2ddb64fc7a5542ea5d0e6e607"));
        assertThat(((SysAuthUserVDO) ((ArrayList) map.get(0)).get(1)).getAuthId(), is
                ("dea304f572034b67a2f3f2727d470f47"));
        System.out.println("结果集为：" + map.toString());
    }
}