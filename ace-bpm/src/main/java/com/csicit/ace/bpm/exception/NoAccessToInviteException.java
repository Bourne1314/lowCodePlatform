package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/6/3 16:35
 */
public class NoAccessToInviteException extends BpmSystemException {
    private String flowId;
    private String userId;

    public NoAccessToInviteException(String flowId, String userId) {
        super(BpmErrorCode.S00024, LocaleUtils.getNoAccessToInvite());
        this.flowId = flowId;
        this.userId = userId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
        args.put("userId", userId);
    }
}
