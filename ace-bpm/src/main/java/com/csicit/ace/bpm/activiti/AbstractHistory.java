package com.csicit.ace.bpm.activiti;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.DbUpdateTask;
import com.csicit.ace.bpm.activiti.impl.v7v1v81.History;
import com.csicit.ace.bpm.pojo.domain.WfiBackupDO;
import com.csicit.ace.bpm.service.WfiBackupService;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.bpm.utils.SerializeUtils;
import com.csicit.ace.common.config.SpringContextUtils;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/2/10 8:16
 */
public abstract class AbstractHistory {
    protected static final WfiBackupService wfiBackupService = SpringContextUtils.getBean(WfiBackupService.class);
    protected WfiBackupDO wfiBackup;
    protected AbstractDbHelper dbHelper;

    public AbstractHistory(WfiBackupDO wfiBackup) {
        this.wfiBackup = wfiBackup;
        this.dbHelper = initDbHelper();
    }

    /**
     * 初始化数据库工具
     *
     * @return 数据库工具
     * @author JonnyJiang
     * @date 2020/4/13 15:35
     */

    protected abstract AbstractDbHelper initDbHelper();

    /**
     * 获取历史操作工具
     *
     * @param wfiBackup 历史备份
     * @return com.csicit.ace.bpm.activiti.AbstractHistory
     * @author JonnyJiang
     * @date 2020/4/13 15:36
     */

    public static AbstractHistory getHistory(WfiBackupDO wfiBackup) {
        if (History.ENGINE_VERSION.equals(wfiBackup.getEngineVersion())) {
            if (ProcessEngineConfigurationImpl.DATABASE_TYPE_MYSQL.equals(BpmConfiguration.getActualDatabaseType())) {
                return new com.csicit.ace.bpm.activiti.impl.v7v1v81.mysql.History(wfiBackup);
            } else if (ProcessEngineConfigurationImpl.DATABASE_TYPE_ORACLE.equals(BpmConfiguration.getActualDatabaseType())) {
                return new com.csicit.ace.bpm.activiti.impl.v7v1v81.oracle.History(wfiBackup);
            }  else if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
                return new com.csicit.ace.bpm.activiti.impl.v7v1v81.st.History(wfiBackup);
            } else {
                return new History(wfiBackup);
            }
        }
        throw new BpmException(LocaleUtils.getUnsupportedEngineVersion(wfiBackup.getEngineVersion()));
    }

    public void recovery() {
        recoverData((Map<String, Object>) SerializeUtils.deserializeAfterUncompress(wfiBackup.getBackupData()));
        wfiBackupService.remove(new QueryWrapper<WfiBackupDO>().eq("FLOW_ID", wfiBackup.getFlowId()).ge("VERSION", wfiBackup.getVersion()));
    }

    public abstract void recoverData(Map<String, Object> data);
}