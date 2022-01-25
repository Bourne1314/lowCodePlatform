package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/12/1 17:36
 */
public class NextNodeFreeStepNotFoundException extends BpmSystemException {
    private final String wfiFlowId;
    private final String nodeId;
    private final String nodeFreeStepId;

    public NextNodeFreeStepNotFoundException(String wfiFlowId, String nodeId, String nodeFreeStepId) {
        super(BpmErrorCode.S00083, LocaleUtils.getNextNodeFreeStepNotFound());
        this.wfiFlowId = wfiFlowId;
        this.nodeId = nodeId;
        this.nodeFreeStepId = nodeFreeStepId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("wfiFlowId", wfiFlowId);
        args.put("nodeId", nodeId);
        args.put("nodeFreeStepId", nodeFreeStepId);
    }
}
