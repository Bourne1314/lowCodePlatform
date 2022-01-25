package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/1 14:56
 */
public class PassCountNotMatchException extends BpmSystemException {
    private final long finishedCount;
    private final long waitForPassCount;
    private final String taskId;

    public PassCountNotMatchException(long finishedCount, long waitForPassCount, String taskId) {
        super(BpmErrorCode.S00037, LocaleUtils.getPassCountNotMatch(finishedCount, waitForPassCount, taskId));
        this.finishedCount = finishedCount;
        this.waitForPassCount = waitForPassCount;
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("finishedCount", finishedCount);
        args.put("waitForPassCount", waitForPassCount);
        args.put("taskId", taskId);
    }
}
