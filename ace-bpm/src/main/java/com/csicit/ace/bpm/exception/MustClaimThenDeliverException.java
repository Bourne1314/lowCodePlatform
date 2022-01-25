package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/6/30 8:27
 */
public class MustClaimThenDeliverException extends BpmSystemException {
    private String taskId;

    public MustClaimThenDeliverException(String taskId) {
        super(BpmErrorCode.S00033, LocaleUtils.getMustClaimThenDeliver());
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}
