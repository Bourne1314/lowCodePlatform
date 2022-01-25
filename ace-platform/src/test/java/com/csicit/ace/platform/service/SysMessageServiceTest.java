package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.pojo.domain.SysMsgReadDO;
import com.csicit.ace.common.pojo.domain.SysMsgUnReadDO;
import com.csicit.ace.data.persistent.service.SysMessageService;
import com.csicit.ace.data.persistent.service.SysMsgReadService;
import com.csicit.ace.data.persistent.service.SysMsgUnReadService;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysMessageServiceTest extends BaseTest {

    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    SysMsgReadService sysMsgReadService;

    @Autowired
    SysMsgUnReadService sysMsgUnReadService;

    @Test
    public void listNoReadTest() {
        List<SysMessageDO> sysMessageDOs = sysMessageService.listNoRead("681cead3d0764d24b261fe3d531ad775");
        System.out.println("结果集：" + sysMessageDOs.toString());
        assertThat(sysMessageDOs.get(0).getTitle(), is("title2"));
        assertThat(sysMessageDOs.get(1).getTitle(), is("title1"));
        assertThat(sysMessageDOs.size(), is(2));
    }

    @Test
    public void updateMsgReadTest() {
        boolean flg = sysMessageService.updateMsgRead("681cead3d0764d24b261fe3d531ad775",
                "cdrcadfd4eb0452d9259e85f665fa187");
        assertThat(flg, is(true));
        assertThat(sysMsgReadService.count(new QueryWrapper<SysMsgReadDO>()
                .eq("user_id", "681cead3d0764d24b261fe3d531ad775")
                .eq("msg_id", "cdrcadfd4eb0452d9259e85f665fa187")), is(1));
        assertThat(sysMsgUnReadService.count(new QueryWrapper<SysMsgUnReadDO>()
                .eq("user_id", "681cead3d0764d24b261fe3d531ad775")
                .eq("msg_id", "cdrcadfd4eb0452d9259e85f665fa187")), is(0));
    }
}