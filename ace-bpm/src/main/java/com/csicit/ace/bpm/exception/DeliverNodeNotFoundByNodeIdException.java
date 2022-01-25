package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/10/13 10:41
 */
public class DeliverNodeNotFoundByNodeIdException extends BpmSystemException {
    private String nodeId;

    public DeliverNodeNotFoundByNodeIdException(String nodeId) {
        super(BpmErrorCode.S00072, LocaleUtils.getDeliverNodeNotFoundByNodeId(nodeId));
        this.nodeId = nodeId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeId", nodeId);
    }
}