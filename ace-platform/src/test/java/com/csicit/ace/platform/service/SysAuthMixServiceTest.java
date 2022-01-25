package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysAuthMixServiceTest extends BaseTest {

    @Test
    public void getAuthWithAppByUserIdTest() {
        Map<String, List> map = sysAuthMixService.getAuthWithAppByUserId("dca07269fe1c4fecb667c442c3ace476");
        assertThat((map.get("bt-ceshidemo111")).size(), is(2));
        assertThat((map.get("bt-ceshidemo222")).size(), is(1));
    }

    @Test
    public void saveAuthMixForAppTest() {
//        boolean flg = sysAuthMixService.saveAuthMixForApp("f0998046ae844ed4b1c1dab4e490f62e", "bt-ceshidemo111");
//        assertThat(flg, is(true));
//        assertThat(sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
//                .eq("app_id", "bt-ceshidemo111").eq("user_id", "f0998046ae844ed4b1c1dab4e490f62e")).size(), is(5));

        boolean flg2 = sysAuthMixService.saveAuthMixForApp("dca07269fe1c4fecb667c442c3ace476", "bt-ceshidemo222");
        assertThat(flg2, is(true));
        assertThat(sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
                .eq("app_id", "bt-ceshidemo222").eq("user_id", "dca07269fe1c4fecb667c442c3ace476")), is(1));
    }

    @Test
    public void saveAuthMixTest() {
//        int count = sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>()
//                .eq("user_id", "dca07269fe1c4fecb667c442c3ace476"));
        boolean flg = sysAuthMixService.saveAuthMix("dca07269fe1c4fecb667c442c3ace476");
        assertThat(flg, is(true));
        assertThat(sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", "dca07269fe1c4fecb667c442c3ace476")).size(), is(3));
    }
}