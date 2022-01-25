package com.csicit.ace.fileserver.core.globalfileconfig;

import com.csicit.ace.common.enums.GlobalFileConfiguration;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;

/**
 * @author JonnyJiang
 * @date 2020/6/12 10:44
 */
public class Normal extends FileConfigurationDO {
    private static final String GROUP_DATASOURCE_ID = "platform";

    public Normal() {
        setId(GlobalFileConfiguration.Normal.getId());
        setConfigurationKey(GlobalFileConfiguration.Normal.getKey());
        setGroupDatasourceId(GROUP_DATASOURCE_ID);
    }
}