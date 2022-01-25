package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/10/16 11:50
 */
public class PassPercentNotMatchException extends BpmSystemException {
    private final String nodeCode;
    private final String nodeName;
    private final Long finishedPercent;
    private final Integer waitForPassPercent;
    private final String taskId;

    public PassPercentNotMatchException(String nodeCode, String nodeName, Long finishedPercent, Integer waitForPassPercent, String taskId) {
        super(BpmErrorCode.S00076, LocaleUtils.getPassPercentNotMatch(finishedPercent, waitForPassPercent, taskId));
        this.nodeCode = nodeCode;
        this.nodeName = nodeName;
        this.finishedPercent = finishedPercent;
        this.waitForPassPercent = waitForPassPercent;
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeCode", nodeCode);
        args.put("nodeName", nodeName);
        args.put("finishedPercent", finishedPercent);
        args.put("waitForPassPercent", waitForPassPercent);
        args.put("taskId", taskId);
    }
}