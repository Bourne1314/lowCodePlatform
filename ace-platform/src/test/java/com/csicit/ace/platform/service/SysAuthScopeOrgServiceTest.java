package com.csicit.ace.platform.service;

import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysAuthScopeOrgServiceTest extends BaseTest {

    @Test
    public void getOrgIdsByUserIdTest() {
        List<String> orgIds1 = sysAuthScopeOrgService.getOrgIdsByUserId("4c66b48a5cfa4886a4c5861cb01eae02");// 测试集团管理员
        assertThat(orgIds1.size(), is(5));
        assertThat(orgIds1, hasItems("b96daa08d59c467a9bd6cca17e46f53f", "e48fd9d687f741e088e4a086fe8dbb77",
                "e18d9ba2bfdb4d67ab99c642ed47655a", "7956981e38f44d0b8b57302307beacb8",
                "54d087404f454139a0b57b9437deb1f8"));
        System.out.println("结果集1：" + orgIds1.toString());
        List<String> orgIds2 = sysAuthScopeOrgService.getOrgIdsByUserId("681cead3d0764d24b261fe3d531ad775");// 测试应用管理员
        assertThat(orgIds2.size(), is(1));
        assertThat(orgIds2.get(0), is("b96daa08d59c467a9bd6cca17e46f53f"));
        System.out.println("结果集2：" + orgIds2.toString());
    }
}