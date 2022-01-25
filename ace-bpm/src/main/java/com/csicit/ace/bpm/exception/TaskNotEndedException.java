package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/6/12 9:02
 */
public class TaskNotEndedException extends BpmSystemException {
    private String taskId;

    public TaskNotEndedException(String taskId) {
        super(BpmErrorCode.S00031, LocaleUtils.getTaskNotEnded());
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}