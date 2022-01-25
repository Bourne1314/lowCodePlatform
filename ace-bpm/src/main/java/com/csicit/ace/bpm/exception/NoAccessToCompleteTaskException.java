package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/18 15:09
 */
public class NoAccessToCompleteTaskException extends BpmSystemException {
    private String taskId;

    public NoAccessToCompleteTaskException(String taskId) {
        super(BpmErrorCode.S00010, LocaleUtils.getNoAccessToCompleteTask());
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}
