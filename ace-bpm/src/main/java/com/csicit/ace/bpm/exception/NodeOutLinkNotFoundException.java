package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/18 15:01
 */
public class NodeOutLinkNotFoundException extends BpmSystemException {
    private String nodeId;

    public NodeOutLinkNotFoundException(String nodeId) {
        super(BpmErrorCode.S00009, LocaleUtils.getOutLinkNotFound());
        this.nodeId = nodeId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeId", nodeId);
    }
}