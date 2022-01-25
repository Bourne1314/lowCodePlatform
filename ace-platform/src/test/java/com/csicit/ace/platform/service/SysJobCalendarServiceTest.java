package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysJobCalendarDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.SysJobCalendarService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SysJobCalendarServiceTest extends BaseTest {
    @Autowired
    SysJobCalendarService sysJobCalendarService;

    @Test
    public void getHasWorkYearListTest() {
        List<String> strings = sysJobCalendarService.getHasWorkYearList("b96daa08d59c467a9bd6cca17e46f53f");
        assertThat(strings.size(), is(2));
        assertThat(strings.get(0), is("2019"));
        assertThat(strings.get(1), is("2020"));
        System.out.println("结果集：" + strings.toString());
    }

    @Test
    public void getWorkDayDataListTest1() {
        setWorkDayTest();
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", "b96daa08d59c467a9bd6cca17e46f53f");
        params.put("currentYear", 2020);
        params.put("currentMonth", 1);
        R r = sysJobCalendarService.getWorkDayDataList(params);
        System.out.println("previousMonthToDays:" + r.get("previousMonthToDays"));
        assertThat(((ArrayList) r.get("previousMonthToDays")).size(), is(0));
        System.out.println("currentMonthToDays:" + r.get("currentMonthToDays"));
        assertThat(((ArrayList) r.get("currentMonthToDays")).size(), is(31));
        System.out.println("nextMonthToDays:" + r.get("nextMonthToDays"));
        assertThat(((ArrayList) r.get("currentMonthToDays")).size(), is(31));
    }

    @Test
    public void getWorkDayDataListTest2() {
        setWorkDayTest();
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", "b96daa08d59c467a9bd6cca17e46f53f");
        params.put("currentYear", 2020);
        params.put("currentMonth", 8);
        R r = sysJobCalendarService.getWorkDayDataList(params);
        System.out.println("previousMonthToDays:" + r.get("previousMonthToDays"));
        assertThat(((ArrayList) r.get("previousMonthToDays")).size(), is(31));
        System.out.println("currentMonthToDays:" + r.get("currentMonthToDays"));
        assertThat(((ArrayList) r.get("currentMonthToDays")).size(), is(31));
        System.out.println("nextMonthToDays:" + r.get("nextMonthToDays"));
        assertThat(((ArrayList) r.get("currentMonthToDays")).size(), is(31));
    }

    @Test
    public void getWorkDayDataListTest3() {
        setWorkDayTest();
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", "b96daa08d59c467a9bd6cca17e46f53f");
        params.put("currentYear", 2020);
        params.put("currentMonth", 12);
        R r = sysJobCalendarService.getWorkDayDataList(params);
        System.out.println("previousMonthToDays:" + r.get("previousMonthToDays"));
        assertThat(((ArrayList) r.get("previousMonthToDays")).size(), is(30));
        System.out.println("currentMonthToDays:" + r.get("currentMonthToDays"));
        assertThat(((ArrayList) r.get("currentMonthToDays")).size(), is(31));
        System.out.println("nextMonthToDays:" + r.get("nextMonthToDays"));
        assertThat(((ArrayList) r.get("currentMonthToDays")).size(), is(31));
    }

    @Test
    public void setWorkStateTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", "b96daa08d59c467a9bd6cca17e46f53f");
        params.put("currentYear", 2020);
        params.put("currentMonth", 12);
        params.put("clickDay", 28);
        System.out.println("修改前：" + sysJobCalendarService.getOne(new QueryWrapper<SysJobCalendarDO>()
                .eq("id", "3ed7ef0652644f13a9dsdce41093cd24")));
        boolean flg = sysJobCalendarService.setWorkState(params);
        assertThat(flg, is(true));
        assertThat(sysJobCalendarService.getOne(new QueryWrapper<SysJobCalendarDO>()
                .eq("id", "3ed7ef0652644f13a9dsdce41093cd24")).getState(), is(1));
        System.out.println("修改后：" + sysJobCalendarService.getOne(new QueryWrapper<SysJobCalendarDO>()
                .eq("id", "3ed7ef0652644f13a9dsdce41093cd24")));
    }

    @Test
    public void setWorkStateTest2() {
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", "b96daa08d59c467a9bd6cca17e46f53f");
        params.put("currentYear", 2020);
        params.put("currentMonth", 1);
        params.put("clickDay", 18);
        System.out.println("修改前：" + sysJobCalendarService.getOne(new QueryWrapper<SysJobCalendarDO>()
                .eq("id", "2e8e67371882464d94891a16f861969f")));
        boolean flg = sysJobCalendarService.setWorkState(params);
        assertThat(flg, is(true));
        assertThat(sysJobCalendarService.getOne(new QueryWrapper<SysJobCalendarDO>()
                .eq("id", "2e8e67371882464d94891a16f861969f")).getState(), is(0));
        System.out.println("修改后：" + sysJobCalendarService.getOne(new QueryWrapper<SysJobCalendarDO>()
                .eq("id", "2e8e67371882464d94891a16f861969f")));
    }

    @Test
    public void setWorkDayTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", "b96daa08d59c467a9bd6cca17e46f53f");
        params.put("year", 2020);
        List<String> types = new ArrayList<>(16);
        types.add("sunday");
        types.add("saturday");
        params.put("type", types);
        List<String> restValue1 = new ArrayList<>(16);
        restValue1.add("20200122");
        restValue1.add("20200209");
        params.put("restValue1", restValue1);
        List<Map> dynamicItems = new ArrayList<>(16);
        Map restValue2 = new LinkedHashMap<>();
        restValue2.put("key", "20200210");
        restValue2.put("value", "20200216");
        Map restValue3 = new LinkedHashMap<>();
        restValue3.put("key", "20200217");
        restValue3.put("value", "20200223");
        dynamicItems.add(restValue2);
        dynamicItems.add(restValue3);
        params.put("dynamicItems", dynamicItems);

        boolean flg = sysJobCalendarService.setWorkDay(params);
        assertThat(flg, is(true));
        assertThat(sysJobCalendarService.count(new QueryWrapper<SysJobCalendarDO>()
                .eq("year", 2020).eq("organization_id", "b96daa08d59c467a9bd6cca17e46f53f")), is(366));
        System.out.println("结果集个数：" + sysJobCalendarService.count(new QueryWrapper<SysJobCalendarDO>()
                .eq("year", 2020).eq("organization_id", "b96daa08d59c467a9bd6cca17e46f53f")));
    }
}