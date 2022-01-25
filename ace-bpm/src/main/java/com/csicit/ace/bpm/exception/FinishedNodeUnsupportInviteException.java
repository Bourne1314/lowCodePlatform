package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * 已办结的节点不支持加签
 *
 * @author JonnyJiang
 * @date 2020/6/2 15:12
 */
public class FinishedNodeUnsupportInviteException extends BpmSystemException {
    private String taskId;

    public FinishedNodeUnsupportInviteException(String taskId) {
        super(BpmErrorCode.S00022, LocaleUtils.getFinishedNodeUnsupportInvite());
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}
