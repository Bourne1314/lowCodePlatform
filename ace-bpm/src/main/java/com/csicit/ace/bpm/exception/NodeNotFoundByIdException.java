package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/18 14:42
 */
public class NodeNotFoundByIdException extends BpmSystemException {
    private String nodeId;
    private String flowId;

    public NodeNotFoundByIdException(String nodeId, String flowId) {
        super(BpmErrorCode.S00007, LocaleUtils.getNodeNotFoundById());
        this.flowId = flowId;
        this.nodeId = nodeId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeId", nodeId);
        args.put("flowId", flowId);
    }
}
