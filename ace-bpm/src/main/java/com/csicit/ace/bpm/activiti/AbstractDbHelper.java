package com.csicit.ace.bpm.activiti;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.activiti.impl.v7v1v81.DbHelper;
import com.csicit.ace.bpm.activiti.impl.v7v1v81.History;
import com.csicit.ace.bpm.pojo.domain.WfiBackupDO;
import com.csicit.ace.bpm.service.ActGeBytearrayService;
import com.csicit.ace.bpm.service.WfiBackupService;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.config.SpringContextUtils;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据库工具
 *
 * @author JonnyJiang
 * @date 2020/4/13 15:26
 */
@Transactional
public abstract class AbstractDbHelper {
    protected static final WfiBackupService wfiBackupService = SpringContextUtils.getBean(WfiBackupService.class);
    protected static final ActGeBytearrayService actGeBytearrayService = SpringContextUtils.getBean(ActGeBytearrayService.class);

    /**
     * 清空
     *
     * @param flowInstanceId 流程实例id
     * @author JonnyJiang
     * @date 2020/4/13 15:30
     */

    public abstract void clear(String flowInstanceId);

    /**
     * 获取数据库操作工具
     *
     * @return 数据库操作工具
     * @author JonnyJiang
     * @date 2020/4/13 15:38
     */

    public static AbstractDbHelper getDbHelper() {
        if (History.ENGINE_VERSION.equals(BpmManager.ENGINE_VERSION)) {
            return DbHelper.getInstance();
        }
        throw new BpmException(LocaleUtils.getUnsupportedEngineVersion(BpmManager.ENGINE_VERSION));
    }

}
