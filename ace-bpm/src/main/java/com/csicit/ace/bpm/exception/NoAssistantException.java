package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/10/10 10:49
 */
public class NoAssistantException extends BpmSystemException {
    private final String nodeCode;
    private final String nodeName;

    public NoAssistantException(String nodeCode, String nodeName) {
        super(BpmErrorCode.S00065, LocaleUtils.getNoAssistant(nodeName, nodeCode));
        this.nodeCode = nodeCode;
        this.nodeName = nodeName;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        super.onLogging(args);
        args.put("nodeCode", nodeCode);
        args.put("nodeName", nodeName);
    }
}
