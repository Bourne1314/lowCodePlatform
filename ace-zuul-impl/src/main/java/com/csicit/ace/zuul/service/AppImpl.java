package com.csicit.ace.zuul.service;

import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IApp;
import org.springframework.stereotype.Service;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/3/5 14:34
 */
@Service("app")
public class AppImpl extends BaseImpl implements IApp {

    @Override
    public SysGroupAppDO getCurrentApp() {
        return getAppById(appName);
    }

    @Override
    public SysGroupAppDO getAppById(String appId) {
        if (StringUtils.isBlank(appId)) {
            return null;
        }
        return clientService.getAppById(appId);
    }
}
