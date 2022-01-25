package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/10/13 10:50
 */
public class NoAccessToClaimTaskException extends BpmSystemException {
    private String taskId;

    public NoAccessToClaimTaskException(String taskId) {
        super(BpmErrorCode.S00073, LocaleUtils.getNoAccessToClaimTask(taskId));
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}
