package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/10/16 11:55
 */
public class WaitingForAssistException extends BpmSystemException {
    private final String nodeCode;
    private final String nodeName;
    private final String taskId;

    public WaitingForAssistException(String nodeCode, String nodeName, String taskId) {
        super(BpmErrorCode.S00077, LocaleUtils.getWaitingForAssist());
        this.nodeCode = nodeCode;
        this.nodeName = nodeName;
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeCode", nodeCode);
        args.put("nodeName", nodeName);
        args.put("taskId", taskId);
    }
}
