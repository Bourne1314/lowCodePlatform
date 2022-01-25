package com.csicit.ace.bpm.activiti.impl.v7v1v81.oracle;

import com.csicit.ace.bpm.pojo.domain.WfiBackupDO;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/3/5 11:11
 */
public class History extends com.csicit.ace.bpm.activiti.impl.v7v1v81.History {
    public History(WfiBackupDO wfiBackup) {
        super(wfiBackup);
    }

    @Override
    protected void insertActHiComment(Map<String, Object> map) {
        wfiBackupService.insertOracleActHiComment(map);
    }

    @Override
    protected void insertActGeBytearray(Map<String, Object> map) {
        wfiBackupService.insertOracleActGeBytearray(map);
    }

    public static Map<String, Object> resolveWfiBackup(WfiBackupDO wfiBackup) {
        Map<String, Object> map = com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveWfiBackup(wfiBackup);
        char[] backupData = wfiBackup.getBackupData().toCharArray();
        map.put("bacupData", backupData);
        return map;
    }
}
