package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/1 11:20
 */
public class WfiTaskPendingNotFoundException extends BpmSystemException {
    private String flowId;
    private String taskId;
    private String userId;

    public WfiTaskPendingNotFoundException(String flowId, String taskId, String userId) {
        super(BpmErrorCode.S00035, LocaleUtils.getWfiTaskPendingNotFound());
        this.flowId = flowId;
        this.taskId = taskId;
        this.userId = userId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
        args.put("taskId", taskId);
        args.put("userId", userId);
    }
}
