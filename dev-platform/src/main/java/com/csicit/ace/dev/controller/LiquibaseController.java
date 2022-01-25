package com.csicit.ace.dev.controller;

import com.csicit.ace.dev.service.LiquibaseService;
import com.csicit.ace.dev.service.ProChangelogHistoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 数据库版本管理 接口访问层
 *
 * @author zuog
 * @version V1.0
 * @date 2019-11-07 10:30:31
 */

@RestController
@RequestMapping("/liquibase")
@Api("数据库版本管理")
public class LiquibaseController {
    @Autowired
    LiquibaseService liquibaseService;
    @Autowired
    ProChangelogHistoryService proChangelogHistoryService;

    @RequestMapping(value = "/create/newVersion/changelog/{serviceId}", method = RequestMethod.GET)
    public void createChangeLogNewVersion(HttpServletResponse response, @PathVariable("serviceId") String serviceId) throws
            IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"changeLog.zip\"");
        liquibaseService.createChangeLogNewVersion(serviceId,response.getOutputStream());

//        response.addHeader("Content-Length", "" + data.length);
//        response.setContentType("application/octet-stream; charset=UTF-8");

//        IOUtils.write(data, response.getOutputStream());

    }
    @RequestMapping(value = "/create/allVersion/changelog/{serviceId}", method = RequestMethod.GET)
    public void createChangeLogAllVersion(HttpServletResponse response, @PathVariable("serviceId") String serviceId) throws
            IOException {
//        byte[] data = liquibaseService.createChangeLogAllVersion(serviceId);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"changeLog-all.zip\"");
        liquibaseService.createChangeLogAllVersion(serviceId,response.getOutputStream());
//        response.addHeader("Content-Length", "" + data.length);
//        response.setContentType("application/octet-stream; charset=UTF-8");
//
//        IOUtils.write(data, response.getOutputStream());

    }

}
