package com.csicit.ace.data.persistent.service.impl;

import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.start.IAceOnlyOneAppStartToDo;
import com.csicit.ace.data.persistent.service.SysConfigServiceD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 10:32
 */
@Service
public class ScanAceConfigImpl implements IAceOnlyOneAppStartToDo {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    SysConfigServiceD sysConfigServiceD;

    @Override
    public void run() {
        if (Constants.AppNames.contains(appName)) {
            sysConfigServiceD.scanConfig(appName);
        }
    }
}
