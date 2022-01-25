package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

public class MustPresetRouteUserException extends BpmSystemException {
    private final String nodeCode;
    private final String nodeName;
    private final String taskId;

    public MustPresetRouteUserException(String nodeCode, String nodeName, String taskId) {
        super(BpmErrorCode.S00099, LocaleUtils.getMustPresetRouteUser());
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
