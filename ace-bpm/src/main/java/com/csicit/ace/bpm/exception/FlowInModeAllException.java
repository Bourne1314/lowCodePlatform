package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * 等待所有流入分支路径抵达激活步骤实例异常
 *
 * @author JonnyJiang
 * @date 2020/7/17 11:19
 */
public class FlowInModeAllException extends BpmSystemException {
    private final Node node;
    private final Node fromNode;

    public FlowInModeAllException(Node node, Node fromNode) {
        super(BpmErrorCode.S00059, LocaleUtils.getFlowInModeAll(node, fromNode));
        this.node = node;
        this.fromNode = fromNode;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeId", node.getId());
        args.put("nodeCode", node.getCode());
        args.put("nodeName", node.getName());
        args.put("fromNodeId", fromNode.getId());
        args.put("fromNodeCodeId", fromNode.getCode());
        args.put("fromNodeNameId", fromNode.getName());
    }
}