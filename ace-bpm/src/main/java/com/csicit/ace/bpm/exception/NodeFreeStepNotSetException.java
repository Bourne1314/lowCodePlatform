package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/12/3 2:54
 */
public class NodeFreeStepNotSetException extends BpmSystemException {
    private final String wfiFlowId;
    private final String nodeId;
    private final String nodeName;

    public NodeFreeStepNotSetException(String wfiFlowId, String nodeId, String nodeName) {
        super(BpmErrorCode.S00085, LocaleUtils.getNodeFreeStepNotSet(nodeName));
        this.wfiFlowId = wfiFlowId;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("wfiFlowId", wfiFlowId);
        args.put("nodeId", nodeId);
        args.put("nodeName", nodeName);
    }
}
