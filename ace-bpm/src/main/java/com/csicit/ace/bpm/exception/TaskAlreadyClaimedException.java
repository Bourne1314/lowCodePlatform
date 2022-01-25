package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/10/13 10:53
 */
public class TaskAlreadyClaimedException extends BpmSystemException {
    private String taskId;

    public TaskAlreadyClaimedException(String taskId) {
        super(BpmErrorCode.S00074, LocaleUtils.getTaskAlreadyClaimed(taskId));
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}