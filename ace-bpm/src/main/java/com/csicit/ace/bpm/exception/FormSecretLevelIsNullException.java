package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/20 17:19
 */
public class FormSecretLevelIsNullException extends BpmSystemException {
    private final String tableName;
    private final String columnName;
    private final String businessKey;

    public FormSecretLevelIsNullException(String tableName, String columnName, String businessKey) {
        super(BpmErrorCode.S00057, LocaleUtils.getFormSecretLevelIsNull());
        this.tableName = tableName;
        this.columnName = columnName;
        this.businessKey = businessKey;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("tableName", tableName);
        args.put("columnName", columnName);
        args.put("businessKey", businessKey);
    }
}