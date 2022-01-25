package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/2 11:17
 */
public class NoTaskPendingException extends BpmSystemException {
    private String flowId;

    public NoTaskPendingException(String flowId) {
        super(BpmErrorCode.S00040, LocaleUtils.getNoTaskPendingException());
        this.flowId = flowId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
    }
}
