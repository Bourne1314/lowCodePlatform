package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * 不允许加签
 *
 * @author JonnyJiang
 * @date 2020/6/3 16:54
 */
public class NotAllowInviteException extends BpmSystemException {
    private String flowId;

    public NotAllowInviteException(String flowId) {
        super(BpmErrorCode.S00025, LocaleUtils.getNotAllowInvite());
        this.flowId = flowId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
    }
}
