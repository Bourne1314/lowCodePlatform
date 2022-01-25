package com.csicit.ace.fileserver.core.utils;

import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.enums.GlobalFileConfiguration;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.fileserver.core.globalfileconfig.DataScreenPicture;
import com.csicit.ace.fileserver.core.globalfileconfig.Normal;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;

/**
 * 全局文件工具
 *
 * @author JonnyJiang
 * @date 2020/6/12 10:39
 */
public class GlobalFileUtil {
    public static FileConfigurationDO getGlobalFileConfigurationById(String id) {
        if (GlobalFileConfiguration.Normal.isIdEquals(id)) {
            return new Normal();
        } else if (GlobalFileConfiguration.DataScreenPicture.isIdEquals(id)) {
            SecurityUtils securityUtils = SpringContextUtils.getBean(SecurityUtils.class);
            return new DataScreenPicture(securityUtils.getCurrentUser());
        }
        return null;
    }

    public static FileConfigurationDO getGlobalFileConfigurationByKey(String key) {
        if (GlobalFileConfiguration.Normal.isKeyEquals(key)) {
            return new Normal();
        } else if (GlobalFileConfiguration.DataScreenPicture.isKeyEquals(key)) {
            SecurityUtils securityUtils = SpringContextUtils.getBean(SecurityUtils.class);
            return new DataScreenPicture(securityUtils.getCurrentUser());
        }
        return null;
    }
}