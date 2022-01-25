package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysPasswordPolicyDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.SysPasswordPolicyService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysPasswordPolicyServiceTest extends BaseTest {

    @Autowired
    SysPasswordPolicyService sysPasswordPolicyService;

    @Test
    public void getPasswordPolicyTest() {
        SysPasswordPolicyDO sysPasswordPolicyDO = sysPasswordPolicyService.getPasswordPolicy("");
        assertThat((sysPasswordPolicyDO != null), is(true));
        assertThat(sysPasswordPolicyDO.getLen(), is(8));
        System.out.println("结果集1：" + sysPasswordPolicyDO.toString());

        SysUserDO sysUserDO = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "admin"));
        SysPasswordPolicyDO sysPasswordPolicyDO2 = sysPasswordPolicyService.getPasswordPolicy(sysUserDO.getId());
        assertThat((sysPasswordPolicyDO2 != null), is(true));
        assertThat(sysPasswordPolicyDO2.getLen(), is(8));
        System.out.println("结果集2：" + sysPasswordPolicyDO2.toString());
    }

//    @Test
//    public void insert() {
//    }

//    @Test
//    public void update() {
//    }

//    @Test
//    public void delete() {
//    }
}