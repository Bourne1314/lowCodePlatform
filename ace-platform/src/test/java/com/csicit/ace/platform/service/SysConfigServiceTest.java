package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.SysConfigService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysConfigServiceTest extends BaseTest {

    @Autowired
    SysConfigService sysConfigService;

//    @Test
//    public void initConfig() {
//    }

    @Test
    public void getValueByAppTest() {
        String value = sysConfigService.getValueByApp("bt-ceshidemo111", "dantitestDoubleT115");
        System.out.println("结果集：" + value);
        assertThat(value, is("222.1"));
    }

//    @Test
//    public void getValueByGroup() {
//    }

    @Test
    public void getValueTest() {
        String value = sysConfigService.getValue("platformName");
        assertThat((value != null), is(true));
        System.out.println("结果集：" + value);
    }

    @Test
    public void saveConfigTest1() {
        SysConfigDO config = new SysConfigDO();
        config.setScope(1);
        config.setName("租户测试配置");
        config.setValue("zuhuconfig");
        config.setSortIndex(456);
        config.setType("String");
        config.setUpdateType(0);
        R r = sysConfigService.saveConfig(config);
        assertThat("保存成功!", is(r.get("msg")));
        assertThat(sysConfigService.count(new QueryWrapper<SysConfigDO>()
                .eq("name", "租户测试配置")), is(1));
        System.out.println("结果集：" + sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                .eq("name", "租户测试配置")));
    }

    @Test
    public void saveConfigTest2() {
        SysConfigDO config = new SysConfigDO();
        config.setScope(2);
        config.setName("集团测试配置");
        config.setValue("groupconfig");
        config.setSortIndex(456);
        config.setType("String");
        config.setUpdateType(0);
        config.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        R r = sysConfigService.saveConfig(config);
        assertThat("保存成功!", is(r.get("msg")));
        assertThat(sysConfigService.count(new QueryWrapper<SysConfigDO>()
                .eq("name", "集团测试配置")), is(1));
        System.out.println("结果集：" + sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                .eq("name", "集团测试配置")));
    }

    @Test
    public void saveConfigTest3() {
        SysConfigDO config = new SysConfigDO();
        config.setScope(3);
        config.setName("应用测试配置");
        config.setValue("appconfig");
        config.setSortIndex(456);
        config.setType("String");
        config.setUpdateType(0);
        config.setAppId("bt-ceshidemo111");
        config.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        R r = sysConfigService.saveConfig(config);
        assertThat("保存成功!", is(r.get("msg")));
        assertThat(sysConfigService.count(new QueryWrapper<SysConfigDO>()
                .eq("name", "应用测试配置")), is(1));
        System.out.println("结果集：" + sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                .eq("name", "应用测试配置")));
    }

    @Test
    public void updateConfigTest() {
        SysConfigDO config = sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                .eq("name", "dantitestDoubleT1218"));
        config.setValue("12331");
        config.setName("dantitestDoubleT121812");
        R r = sysConfigService.updateConfig(config);
        assertThat("更新成功!", is(r.get("msg")));
        SysConfigDO newConfig1 = sysConfigService.getById("1crf2f27bc734774bf966b5fd0abeb2c");
        assertThat(newConfig1.getName(), is("dantitestDoubleT121812"));
        assertThat(newConfig1.getValue(), is("12331"));
        System.out.println("修改后1：" + newConfig1);

        SysConfigDO config2 = sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                .eq("name", "dantitestDoubleT1217"));
        config2.setValue("456");
        config2.setName("dantitestDoubleT121712");
        R r2 = sysConfigService.updateConfig(config2);
        assertThat("更新成功!", is(r2.get("msg")));
        SysConfigDO newConfig2 = sysConfigService.getById("abn1c34fa1414146b430306f15dc9aa5");
        assertThat(newConfig2.getName(), is("dantitestDoubleT121712"));
        assertThat(newConfig2.getValue(), is("456"));
        System.out.println("修改后2：" + newConfig2);

        SysConfigDO config3 = sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                .eq("name", "dantitestDoubleT115"));
        config3.setValue("789");
        config3.setName("dantitestDoubleT11512");
        R r3 = sysConfigService.updateConfig(config3);
        assertThat("更新成功!", is(r3.get("msg")));
        SysConfigDO newConfig3 = sysConfigService.getById("42w0cb06f285450da23e128d3286cf8f");
        assertThat(newConfig3.getName(), is("dantitestDoubleT11512"));
        assertThat(newConfig3.getValue(), is("789"));
        System.out.println("修改后3：" + newConfig3);
    }

//    @Test
//    public void delete() {
//    }

    @Test
    public void initSaveOrUpdateConfigByKeyTest() {
        System.out.println("修改前：" + sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                .eq("name", "dantitestDoubleT115")));
        boolean flg = sysConfigService.initSaveOrUpdateConfigByKey("dantitestDoubleT115", "12.1", "");
        assertThat(flg, is(true));
        SysConfigDO newConfig = sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                .eq("name", "dantitestDoubleT115"));
        assertThat(newConfig.getValue(), is("12.1"));
        System.out.println("修改后：" + newConfig);

        boolean flg2 = sysConfigService.initSaveOrUpdateConfigByKey("danticeshi11", "1123.1", "");
        assertThat(flg2, is(true));
        SysConfigDO newConfig2 = sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                .eq("name", "danticeshi11"));
        assertThat((newConfig2 != null), is(true));
        System.out.println("结果集：" + newConfig2);
    }
}