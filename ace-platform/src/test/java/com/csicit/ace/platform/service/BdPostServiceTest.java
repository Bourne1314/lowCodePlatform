package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.BdPostDO;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.BdPostService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BdPostServiceTest extends BaseTest {

    @Autowired
    BdPostService bdPostService;

    @Test
    public void savePostTest() {
        BdPostDO postDO = new BdPostDO();
        postDO.setName("单元测试岗位3");
        postDO.setTypeId("3347804290774390a8e38715a200c5ea");
        postDO.setDepartmentId("8c22d74d433d4828bd59b05d7059a8b7");
        postDO.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        postDO.setOrganizationId("b96daa08d59c467a9bd6cca17e46f53f");
        postDO.setSortIndex(12);
        List<String> depIds = new ArrayList<>();
        depIds.add("3373e43a15d74d97a4c9a2ebb4a5bb25");
        depIds.add("fa08fccfe110469f87be52888b6159cd");
        postDO.setOtherDepIds(depIds);
        boolean flg = bdPostService.savePost(postDO);
        assertThat(flg, is(true));
        assertThat(bdPostService.count(new QueryWrapper<BdPostDO>().eq("name", "单元测试岗位3")), is(3));
        System.out.println("岗位信息：" + bdPostService.list(new QueryWrapper<BdPostDO>()
                .eq("name", "单元测试岗位3")).toString());

    }

//    @Test
//    public void updatePost() {
//    }

//    @Test
//    public void deletePost() {
//    }
}