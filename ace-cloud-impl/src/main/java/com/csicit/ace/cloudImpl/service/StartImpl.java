package com.csicit.ace.cloudImpl.service;

import com.csicit.ace.common.utils.DiscoveryUtils;
import com.csicit.ace.interfaces.service.IStart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/24 11:46
 */
@Service("start")
public class StartImpl extends BaseImpl implements IStart {

    @NonNull
    @Autowired
    DiscoveryUtils discoveryUtils;

    @Override
    public boolean canAppStartToDo() {
        // nacos上注册的应用应该没有健康实例
        if (discoveryUtils != null) {
            if (discoveryUtils.getHealthyInstances(appName).size() > 1) {
                return false;
            }
        }
        return gatewayService.lockApp(appName);
    }

    @Override
    public boolean completeAppStartToDo() {
        return gatewayService.unLockApp(appName);
    }
}
