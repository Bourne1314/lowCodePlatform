package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/18 14:48
 */
public class NodeNotFoundByCodeException extends BpmSystemException {
    private String nodeCode;
    private String flowId;

    public NodeNotFoundByCodeException(String nodeCode, String flowId) {
        super(BpmErrorCode.S00008, LocaleUtils.getNodeNotFoundByCode());
        this.nodeCode = nodeCode;
        this.flowId = flowId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeCode", nodeCode);
        args.put("flowId", flowId);
    }
}
