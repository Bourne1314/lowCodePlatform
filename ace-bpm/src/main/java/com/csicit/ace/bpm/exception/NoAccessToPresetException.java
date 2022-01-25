package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/8/25 17:13
 */
public class NoAccessToPresetException extends BpmSystemException {
    private final String flowId;
    private final String taskId;
    private final String userId;

    public NoAccessToPresetException(String flowId, String taskId, String userId) {
        super(BpmErrorCode.S00063, LocaleUtils.getNoAccessToPreset());
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
