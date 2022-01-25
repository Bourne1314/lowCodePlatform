package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.BdPersonJobService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BdPersonJobServiceTest extends BaseTest {
    @Autowired
    BdPersonJobService bdPersonJobService;

    @Test
    public void savePersonJobTest() {
        BdPersonJobDO bdPersonJobDO = new BdPersonJobDO();
        bdPersonJobDO.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        bdPersonJobDO.setOrganizationId("b96daa08d59c467a9bd6cca17e46f53f");
        bdPersonJobDO.setDepartmentId("8c22d74d433d4828bd59b05d7059a8b7");
        bdPersonJobDO.setJobId("10d31045f5114983a5110f0915197284");
        bdPersonJobDO.setMainJob(1);
        bdPersonJobDO.setPostId("23233ad0839e487293c4557cb40877e6");
        bdPersonJobDO.setSortIndex(12);
        bdPersonJobDO.setPersonCode("34543");
        bdPersonJobDO.setPersonDocId("6fb1542175fc41f3adf1aefad04584ce");
        boolean flg = bdPersonJobService.savePersonJob(bdPersonJobDO);
        assertThat(flg, is(true));
        System.out.println(bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>()
                .eq("person_doc_id", "6fb1542175fc41f3adf1aefad04584ce")));
        assertThat(bdPersonJobService.count(new QueryWrapper<BdPersonJobDO>()
                .eq("person_doc_id", "6fb1542175fc41f3adf1aefad04584ce")), is(2));
    }

//    @Test
//    public void deletePersonJob() {
//    }

//    @Test
//    public void updatePersonJob() {
//    }

    @Test
    public void getJobsTest() {
        List<BdPersonJobDO> personJobDOS = bdPersonJobService.getJobs("f0998046ae844ed4b1c1dab4e490f62e");
        assertThat(personJobDOS.size(), is(1));
    }

//    @Test
//    public void setMainJob() {
//    }
}