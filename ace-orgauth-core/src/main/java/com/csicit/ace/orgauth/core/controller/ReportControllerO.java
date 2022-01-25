package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.orgauth.core.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shanwj
 * @date 2019/12/12 11:46
 */
@RequestMapping("/orgauth/reports")
@RestController
public class ReportControllerO {

    @Resource(name = "reportServiceO")
    private ReportService reportService;

    @RequestMapping(value = "/query/reportTree/{parentId}/{type}/{appId}", method = RequestMethod.GET)
    public List<TreeVO> getReportTree(@PathVariable("parentId") String parentId,
                                      @PathVariable("type") int type,
                                      @PathVariable("appId") String appId) {
        return reportService.getReportTree(parentId, type, appId);
    }
}
