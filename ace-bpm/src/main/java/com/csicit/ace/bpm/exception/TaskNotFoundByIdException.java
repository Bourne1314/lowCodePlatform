package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/18 14:10
 */
public class TaskNotFoundByIdException extends BpmSystemException {
    private String taskId;

    public TaskNotFoundByIdException(String taskId) {
        super(BpmErrorCode.S00006, LocaleUtils.getTaskNotFoundById());
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}
