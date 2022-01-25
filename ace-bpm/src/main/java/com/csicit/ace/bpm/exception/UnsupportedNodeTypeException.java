package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/18 15:12
 */
public class UnsupportedNodeTypeException extends BpmSystemException {
    private String nodeType;

    public UnsupportedNodeTypeException(String nodeType) {
        super(BpmErrorCode.S00011, LocaleUtils.getUnsupportedNodeType(nodeType));
        this.nodeType = nodeType;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeType", nodeType);
    }
}
