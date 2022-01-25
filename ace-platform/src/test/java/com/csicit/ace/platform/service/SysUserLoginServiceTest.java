package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.pojo.domain.SysUserLoginDO;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.SysUserLoginService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysUserLoginServiceTest extends BaseTest {

    @Autowired
    SysUserLoginService sysUserLoginService;

    @Test
    public void getLatestLogin() {
        SysUserDO sysUserDO = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "admin"));
        SysUserLoginDO sysUserLoginDO = sysUserLoginService.getLatestLogin(sysUserDO.getId());
        assertThat((sysUserLoginDO == null), is(false));
        System.out.println("结果集：" + sysUserLoginDO.toString());
    }
}