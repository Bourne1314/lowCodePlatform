package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/20 17:42
 */
public class UnsupportedFormSecretLevelException extends BpmSystemException {
    private String secretLevel;

    public UnsupportedFormSecretLevelException(String secretLevel) {
        super(BpmErrorCode.S00058, LocaleUtils.getUnsupportedFormSecretLevel());
        this.secretLevel = secretLevel;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("secretLevel", secretLevel);
    }
}