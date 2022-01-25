package com.csicit.ace.dbplus.config;

import com.csicit.ace.common.log.AceLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author shanwj
 * @date 2020/4/14 16:19
 */
@Component
public abstract class AbstractAppStartupConfigImpl {

    @Nullable
    @Autowired
    public IScanData scanDataImpl;

    @Autowired
    public AceLogger aceLogger;

}
