package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2021/2/8 16:32
 */
public class TargetNodeMustBePresetedNodeException extends BpmSystemException {
    private final String nodeId;
    private final String nodeName;
    private final String nodeCode;

    public TargetNodeMustBePresetedNodeException(String nodeId, String nodeName, String nodeCode) {
        super(BpmErrorCode.S00100, LocaleUtils.getTargetNodeMustBePresetedNode(nodeName));
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.nodeCode = nodeCode;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeId", nodeId);
        args.put("nodeName", nodeName);
        args.put("nodeCode", nodeCode);
    }
}