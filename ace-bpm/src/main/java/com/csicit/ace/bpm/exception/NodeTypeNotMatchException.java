package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/11/25 15:04
 */
public class NodeTypeNotMatchException extends BpmSystemException {

    private final NodeType expectNodeType;
    private final String actualNodeType;

    public NodeTypeNotMatchException(NodeType expectNodeType, String actualNodeType) {
        super(BpmErrorCode.S00082, LocaleUtils.getNodeTypeNotMatch());
        this.expectNodeType = expectNodeType;
        this.actualNodeType = actualNodeType;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("expectNodeType", expectNodeType.getValue());
        args.put("actualNodeType", actualNodeType);
    }
}