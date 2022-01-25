package com.csicit.ace.platform.service;

import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.service.ReportInfoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReportInfoServiceTest extends BaseTest {
    @Autowired
    ReportInfoService reportInfoService;

    @Test
    public void getReportTreeTest() {
        TreeVO treeVO = reportInfoService.getReportTree("bt-ceshidemo111", 1);
        assertThat((treeVO != null), is(true));
        assertThat(treeVO.getLabel(), is("所有报表"));
        assertThat(treeVO.getType(), is("parentNode"));
        assertThat(treeVO.getChildren().get(0).getLabel(), is("报表1"));
        assertThat(treeVO.getChildren().get(0).getType(), is("parentNode"));
        assertThat(treeVO.getChildren().get(0).getChildren().get(0).getLabel(), is("单体测试报表1"));
        assertThat(treeVO.getChildren().get(0).getChildren().get(0).getType(), is("childNode"));
        assertThat(treeVO.getChildren().get(0).getChildren().get(1).getLabel(), is("单体测试报表2"));
        assertThat(treeVO.getChildren().get(0).getChildren().get(1).getType(), is("childNode"));
        System.out.println("结果集：" + treeVO.toString());
    }

//    @Test
//    public void saveReportTest() {
//    }

//    @Test
//    public void updateReport() {
//    }

    @Test
    public void importMrt() {
        boolean flg = reportInfoService.importMrt("19dbccf807844383b05cb6056090b414", "报表信息1123");
        assertThat(flg, is(true));
        assertThat(reportInfoService.getById("19dbccf807844383b05cb6056090b414").getMrtStr(), is("报表信息1123"));
        System.out.println("结果集：" + reportInfoService.getById("19dbccf807844383b05cb6056090b414"));
    }

//    @Test
//    public void deleteReports() {
//    }
}