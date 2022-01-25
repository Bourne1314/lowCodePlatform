package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/10/10 17:13
 */
public class NoSpecifiedHostException extends BpmSystemException {
    private final String nodeName;
    private final String nodeCode;

    public NoSpecifiedHostException(String nodeName, String nodeCode) {
        super(BpmErrorCode.S00067, LocaleUtils.getNoSpecifiedHost(nodeName, nodeCode));
        this.nodeName = nodeName;
        this.nodeCode = nodeCode;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeName", nodeName);
        args.put("nodeCode", nodeCode);
    }
}
