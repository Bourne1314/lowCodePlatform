package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/10/10 14:03
 */
public class SpecifiedHostNoAccessException extends BpmSystemException {
    private final String nodeName;
    private final String nodeCode;
    private final String host;

    public SpecifiedHostNoAccessException(String nodeName, String nodeCode, String host) {
        super(BpmErrorCode.S00066, LocaleUtils.getSpecifiedHostNoAccess(nodeName, nodeCode, host));
        this.nodeName = nodeName;
        this.nodeCode = nodeCode;
        this.host = host;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeName", nodeName);
        args.put("nodeCode", nodeCode);
        args.put("host", host);
    }
}
