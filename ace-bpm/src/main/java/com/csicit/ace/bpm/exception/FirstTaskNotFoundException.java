package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/16 14:19
 */
public class FirstTaskNotFoundException extends BpmSystemException {
    private String flowId;

    public FirstTaskNotFoundException(String flowId) {
        super(BpmErrorCode.S00045, LocaleUtils.getFirstTaskNotFound());
        this.flowId = flowId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
    }
}
