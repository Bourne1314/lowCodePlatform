package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/6/11 17:58
 */
public class HostFinishedException extends BpmSystemException {
    private String flowId;
    private String taskId;

    public HostFinishedException(String flowId, String taskId) {
        super(BpmErrorCode.S00028, LocaleUtils.getHostFinished());
        this.flowId = flowId;
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
        args.put("taskId", taskId);
    }
}
