package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/6/12 8:48
 */
public class NoAccessToWithdrawWorkException extends BpmSystemException {
    private final String flowId;
    private final String userId;

    public NoAccessToWithdrawWorkException(String flowId, String userId) {
        super(BpmErrorCode.S00030, LocaleUtils.getNoAccessToWithdrawWork());
        this.flowId = flowId;
        this.userId = userId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
        args.put("userId", userId);
    }
}