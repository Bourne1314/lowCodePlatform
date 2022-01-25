package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/11/25 10:54
 */
public class NodeFreeStepIsNotNextStepException extends BpmSystemException {
    private final String wfiFlowId;
    private final String nodeId;
    private final String nodeFreeStepId;
    private final String nextNodeFreeStepId;

    public NodeFreeStepIsNotNextStepException(String wfiFlowId, String nodeId, String nodeFreeStepId, String nextNodeFreeStepId) {
        super(BpmErrorCode.S00081, LocaleUtils.getNodeFreeStepIsNotNextStep());
        this.wfiFlowId = wfiFlowId;
        this.nodeId = nodeId;
        this.nodeFreeStepId = nodeFreeStepId;
        this.nextNodeFreeStepId = nextNodeFreeStepId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("wfiFlowId", wfiFlowId);
        args.put("nodeId", nodeId);
        args.put("nodeFreeStepId", nodeFreeStepId);
        args.put("nextNodeFreeStepId", nextNodeFreeStepId);
    }
}