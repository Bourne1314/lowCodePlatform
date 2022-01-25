package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysDictValueDO;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysDictValueServiceTest extends BaseTest {

    @Test
    public void getDictValueByGroupIdTest() {
        List<SysDictValueDO> sysDictValueDOS = sysDictValueService.getDictValueByGroupId("DANTI_POST_TYPE", "");
        assertThat(sysDictValueDOS.size(), is(2));
        assertThat(sysDictValueDOS.get(0).getDictName(), is("研发岗"));
        assertThat(sysDictValueDOS.get(1).getDictName(), is("管理岗"));
        System.out.println("结果集1：" + sysDictValueDOS.toString());

        List<SysDictValueDO> sysDictValueDOS1 = sysDictValueService.getDictValueByGroupId("DANTI_COUNTRY",
                "9e92cb17c7bf4af9b6072520d42ea103");
        assertThat(sysDictValueDOS1.size(), is(2));
        assertThat(sysDictValueDOS1.get(0).getDictName(), is("中国"));
        assertThat(sysDictValueDOS1.get(1).getDictName(), is("美国"));
        System.out.println("结果集2：" + sysDictValueDOS1.toString());

        List<SysDictValueDO> sysDictValueDOS3 = sysDictValueService.getDictValueByGroupId("DANTI_COUNTRY",
                "0e4b26e45d0348088503efc8768afcdc");
        assertThat(sysDictValueDOS3.size(), is(2));
        assertThat(sysDictValueDOS3.get(0).getDictName(), is("中国"));
        assertThat(sysDictValueDOS3.get(1).getDictName(), is("美国"));
        System.out.println("结果集3：" + sysDictValueDOS3.toString());
    }

    @Test
    public void getDictValueByAppIdTest() {
        List<SysDictValueDO> sysDictValueDOS = sysDictValueService.getDictValueByAppId("DANTI_POST_TYPE", "");
        assertThat(sysDictValueDOS.size(), is(2));
        assertThat(sysDictValueDOS.get(0).getDictName(), is("研发岗"));
        assertThat(sysDictValueDOS.get(1).getDictName(), is("管理岗"));
        System.out.println("结果集1：" + sysDictValueDOS.toString());

        List<SysDictValueDO> sysDictValueDOS1 = sysDictValueService.getDictValueByAppId("DANTI_GAME",
                "bt-ceshidemo111");
        assertThat(sysDictValueDOS1.size(), is(3));
        assertThat(sysDictValueDOS1.get(0).getDictName(), is("LOL"));
        assertThat(sysDictValueDOS1.get(1).getDictName(), is("LOLPC"));
        assertThat(sysDictValueDOS1.get(2).getDictName(), is("CF"));
        System.out.println("结果集2：" + sysDictValueDOS1.toString());

    }

    @Test
    public void saveDictValueTest1() {
        SysDictValueDO sysDictValueDO = new SysDictValueDO();
        sysDictValueDO.setParentId("0");
        sysDictValueDO.setTypeId("3ertb30dbda145ffa9ff847404425e2c");
        sysDictValueDO.setType("DANTI_GAME");
        sysDictValueDO.setAppId("bt-ceshidemo111");
        sysDictValueDO.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        sysDictValueDO.setScope(3);
        sysDictValueDO.setDictName("逆战");
        sysDictValueDO.setDictValue("nizhan");
        sysDictValueDO.setSortIndex(22);
        boolean flg = sysDictValueService.saveDictValue(sysDictValueDO);
        assertThat(flg, is(true));
        SysDictValueDO newSysDictValue = sysDictValueService.getOne(new QueryWrapper<SysDictValueDO>()
                .eq("dict_name", "逆战"));
        assertThat((newSysDictValue != null), is(true));
        System.out.println("结果集：" + newSysDictValue);
    }

    @Test
    public void saveDictValueTest2() {
        SysDictValueDO sysDictValueDO = new SysDictValueDO();
        sysDictValueDO.setParentId("edrf804290774390a8e38715a200c5ea");
        sysDictValueDO.setTypeId("3ertb30dbda145ffa9ff847404425e2c");
        sysDictValueDO.setType("DANTI_GAME");
        sysDictValueDO.setAppId("bt-ceshidemo111");
        sysDictValueDO.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        sysDictValueDO.setScope(3);
        sysDictValueDO.setDictName("LOL手游");
        sysDictValueDO.setDictValue("LOLshouyou");
        sysDictValueDO.setSortIndex(23);
        boolean flg = sysDictValueService.saveDictValue(sysDictValueDO);
        assertThat(flg, is(true));
        SysDictValueDO newSysDictValue = sysDictValueService.getOne(new QueryWrapper<SysDictValueDO>()
                .eq("dict_name", "LOL手游"));
        assertThat((newSysDictValue != null), is(true));
        System.out.println("结果集：" + newSysDictValue);
    }

    @Test
    public void updateDictValueTest1() {
        saveDictValueTest2();
        SysDictValueDO sysDictValueDO = sysDictValueService.getOne(new QueryWrapper<SysDictValueDO>()
                .eq("dict_name", "LOL"));
        System.out.println("修改前：" + sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                .eq("type_id", sysDictValueDO.getTypeId()).likeRight("sort_path", sysDictValueDO.getSortPath()))
                .toString());
        sysDictValueDO.setSortIndex(456);
        sysDictValueDO.setDictName("修改后LOL");
        boolean flg = sysDictValueService.updateDictValue(sysDictValueDO);
        assertThat(flg, is(true));
        SysDictValueDO newSysDictValue = sysDictValueService.getById(sysDictValueDO.getId());
        assertThat(newSysDictValue.getDictName(), is("修改后LOL"));
        assertThat(newSysDictValue.getSortPath(), is("000456"));
        assertThat(sysDictValueService.count(new QueryWrapper<SysDictValueDO>()
                .eq("type_id", sysDictValueDO.getTypeId()).likeRight("sort_path", sysDictValueService.getById
                        (sysDictValueDO.getId()).getSortPath())), is(3));
        System.out.println("修改后：" + sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                .eq("type_id", sysDictValueDO.getTypeId()).likeRight("sort_path", sysDictValueService.getById
                        (sysDictValueDO.getId()).getSortPath()))
                .toString());
    }

    @Test
    public void updateDictValueTest2() {
        SysDictValueDO sysDictValueDO = sysDictValueService.getOne(new QueryWrapper<SysDictValueDO>()
                .eq("dict_name", "LOLPC"));
        System.out.println("修改前：" + sysDictValueDO);
        sysDictValueDO.setSortIndex(456);
        sysDictValueDO.setDictName("修改后LOLPC");
        boolean flg = sysDictValueService.updateDictValue(sysDictValueDO);
        assertThat(flg, is(true));
        SysDictValueDO newSysDictValue = sysDictValueService.getById(sysDictValueDO.getId());
        assertThat(newSysDictValue.getDictName(), is("修改后LOLPC"));
        assertThat(newSysDictValue.getSortPath(), is("000001000456"));
        System.out.println("修改后：" + newSysDictValue);
    }

    @Test
    public void deleteByIds() {
        List<String> ids = new ArrayList<>();
        ids.add("edrf804290774390a8e38715a200c5ea");
        ids.add("vhbj8563583f487b8ea0845ce8319774");
        boolean flg = sysDictValueService.deleteByIds(ids);
        assertThat(flg, is(true));
        assertThat(sysDictValueService.count(new QueryWrapper<SysDictValueDO>()
                .eq("type", "DANTI_GAME")), is(0));
    }
}