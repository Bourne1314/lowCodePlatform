package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/7/13 10:29
 */
@RestController
@RequestMapping("/orgauth/sysAuditLogs")
public class SysAuditLogControllerO {

    @Autowired
    SysAuditLogService sysAuditLogService;

    /**
     * 保存审计日志
     * @return
     * @author FourLeaves
     * @date 2020/7/13 10:44
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    boolean saveLog(@RequestBody AuditLogTypeDO type, @RequestParam("title") String title, @RequestParam("opContent") Object opContent, @RequestParam("groupId") String groupId, @RequestParam("appId") String appId) {
        return sysAuditLogService.saveLog(type, title, opContent, groupId, appId);
    }

}
