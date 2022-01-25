package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.BdPersonDocService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BdPersonDocServiceTest extends BaseTest {

    @Autowired
    BdPersonDocService bdPersonDocService;

    @Test
    public void insertTest() {
        BdPersonDocDO personDocDO = new BdPersonDocDO();
        personDocDO.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        personDocDO.setOrganizationId("b96daa08d59c467a9bd6cca17e46f53f");
        personDocDO.setName("测试个人档案1");
        personDocDO.setCode("1231");

        BdPersonJobDO bdPersonJobDO = new BdPersonJobDO();
        bdPersonJobDO.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        bdPersonJobDO.setOrganizationId("b96daa08d59c467a9bd6cca17e46f53f");
        bdPersonJobDO.setDepartmentId("8c22d74d433d4828bd59b05d7059a8b7");
        bdPersonJobDO.setJobId("10d31045f5114983a5110f0915197284");
        bdPersonJobDO.setMainJob(1);
        bdPersonJobDO.setPostId("66553ad0839e487293c4557cb40877e6");
        bdPersonJobDO.setSortIndex(12);
        bdPersonJobDO.setPersonCode("34543");
        personDocDO.setMainJob(bdPersonJobDO);

        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setUserName("ceshidangan1");
        sysUserDO.setStaffNo("3453453");
        sysUserDO.setSortIndex(6235);
        sysUserDO.setSecretLevel(4);
        personDocDO.setUser(sysUserDO);

        R r = bdPersonDocService.insert(personDocDO);
        System.out.println(r.get("msg"));
        assertThat("保存成功!", is(r.get("msg")));
        System.out.println("档案信息：" + bdPersonDocService.getOne(new QueryWrapper<BdPersonDocDO>()
                .eq("name", "测试个人档案1")).toString());
        assertThat(bdPersonDocService.count(new QueryWrapper<BdPersonDocDO>()
                .eq("name", "测试个人档案1")), is(1));
        String id = bdPersonDocService.getOne(new QueryWrapper<BdPersonDocDO>()
                .eq("name", "测试个人档案1")).getId();
        assertThat(bdPersonJobService.count(new QueryWrapper<BdPersonJobDO>()
                .eq("person_doc_id", id)), is(1));
        assertThat(sysUserService.count(new QueryWrapper<SysUserDO>()
                .eq("user_name", "ceshidangan1")), is(1));
        System.out.println("用户信息：" + sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", "ceshidangan1")).toString());
    }

//    @Test
//    public void update() {
//    }

//    @Test
//    public void deleteTest() {
//        List<String> ids = new ArrayList<>();
//        ids.add("c17949cab6c441b189718f44cbd833e5");
//        ids.add("6899c92db4624a829e49165f8732bfb1");
//        R r = bdPersonDocService.delete(ids);
//        System.out.println(r.get("msg"));
//        assertThat("删除成功!", is(r.get("msg")));
//        System.out.println("档案信息：" + bdPersonDocService.list(new QueryWrapper<BdPersonDocDO>()
//                .in("id", ids)).toString());
//        assertThat(sysUserService.list(new QueryWrapper<SysUserDO>()
//                .in("person_doc_id", ids)).size(), is(0));
//    }

    @Test
    public void getPersonByIdTest() {
        BdPersonDocDO personDocDO = bdPersonDocService.getPersonById("c17949cab6c441b189718f44cbd833e5");
        System.out.println("个人档案信息：" + personDocDO.toString());
        assertThat((personDocDO != null), is(true));
        assertThat(personDocDO.getJobList().size(), is(1));
    }
}