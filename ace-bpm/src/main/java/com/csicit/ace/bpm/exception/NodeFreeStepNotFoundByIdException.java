package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/11/23 17:54
 */
public class NodeFreeStepNotFoundByIdException extends BpmSystemException {
    private final String nodeFreeStepId;
    private final String nodeId;

    public NodeFreeStepNotFoundByIdException(String nodeFreeStepId, String nodeId) {
        super(BpmErrorCode.S00079, LocaleUtils.getNodeFreeStepNotFoundById());
        this.nodeFreeStepId = nodeFreeStepId;
        this.nodeId = nodeId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeFreeStepId", nodeFreeStepId);
        args.put("nodeId", nodeId);
    }
}