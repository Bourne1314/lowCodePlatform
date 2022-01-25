package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/26 19:43
 */
public class FirstManualNodeNotFoundException extends BpmSystemException {
    private String id;

    public FirstManualNodeNotFoundException(String id) {
        super(BpmErrorCode.S00018, LocaleUtils.getFirstManualNodeNotFound());
        this.id = id;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", id);
    }
}
