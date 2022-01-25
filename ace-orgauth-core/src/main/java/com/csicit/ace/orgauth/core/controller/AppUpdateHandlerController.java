package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.dbplus.config.IScanData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Api("应用升级数据操作")
@RequestMapping("/orgauth/appUpdateHandler")
public class AppUpdateHandlerController {
    @Autowired
    IScanData scanData;

    @RequestMapping(value = "/lockApp", method = RequestMethod.POST)
    public boolean lockApp(@RequestBody Map<String, String> map) {
        return scanData.lockApp(map);
    }

    @RequestMapping(value = "/unLockApp", method = RequestMethod.POST)
    public boolean unLockApp(@RequestBody Map<String, String> map) {
        return scanData.unLockApp(map);
    }

    @RequestMapping(value = "/updateDbItem", method = RequestMethod.POST)
    public boolean updateDbItem(@RequestBody AppUpgrade appUpgrade) {
        return scanData.updateDbItem(appUpgrade);
    }

    @RequestMapping(value = "/updAppVersion", method = RequestMethod.POST)
    public boolean updAppVersion(@RequestBody Map<String, String> map) {
        return scanData.updAppVersion(map);
    }
}
