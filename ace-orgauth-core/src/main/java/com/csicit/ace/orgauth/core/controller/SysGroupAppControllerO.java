package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.service.SysGroupAppServiceD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/24 14:07
 */
@RestController
@RequestMapping("/orgauth/sysGroupApps")
public class SysGroupAppControllerO {

    @Autowired
    SysGroupAppServiceD sysGroupAppServiceD;

    /**
     * 获取应用
     *
     * @param appId
     * @return
     * @author FourLeaves
     * @date 2019/12/25 17:44
     */
    @RequestMapping(value = "/{appId}", method = RequestMethod.GET)
    SysGroupAppDO getAppById(@PathVariable("appId") String appId) {
        if (StringUtils.isNotBlank(appId)) {
            return sysGroupAppServiceD.getById(appId);
        }
        return null;
    }

    /**
     * 给App上锁
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 11:51
     */
    @RequestMapping(value = "/action/lockApp", method = RequestMethod.GET)
    boolean lockApp(@RequestParam("appName") String appName) {
        return sysGroupAppServiceD.lockApp(appName);
    }

    /**
     * 解锁app
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 11:51
     */
    @RequestMapping(value = "/action/unLockApp", method = RequestMethod.GET)
    boolean unLockApp(@RequestParam("appName") String appName) {
        return sysGroupAppServiceD.unLockApp(appName);
    }
}
