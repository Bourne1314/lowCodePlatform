package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysDictDO;
import com.csicit.ace.common.pojo.domain.SysDictValueDO;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysDictServiceTest extends BaseTest {


//    @Test
//    public void saveDict() {
//    }

    @Test
    public void updateDictTest() {
        SysDictDO sysDictDO = sysDictService.getOne(new QueryWrapper<SysDictDO>()
                .eq("id", "1111b30dbda145ffa9ff847404425e2c"));
        System.out.println("修改前字典：" + sysDictDO.toString());
        System.out.println("修改前字典数据：" + sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                .eq("type_id", sysDictDO.getId())).toString());
        sysDictDO.setType("XIUGAI_POST_TYPE");
        boolean flg = sysDictService.updateDict(sysDictDO);
        assertThat(flg, is(true));
        SysDictDO newsysDictDO = sysDictService.getById("1111b30dbda145ffa9ff847404425e2c");
        assertThat(newsysDictDO.getType(), is("XIUGAI_POST_TYPE"));
        System.out.println("修改后字典：" + newsysDictDO);
        assertThat(sysDictValueService.count(new QueryWrapper<SysDictValueDO>()
                .eq("type", "XIUGAI_POST_TYPE")), is(2));
        System.out.println("修改后字典数据：" + sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                .eq("type_id", sysDictDO.getId())).toString());
    }

//    @Test
//    public void deleteByIds() {
//    }
}