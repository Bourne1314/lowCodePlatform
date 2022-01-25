package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2021/7/3 13:32
 */
public class NodeNotAllowPresetRouteException extends BpmSystemException {
    private Node node;

    public NodeNotAllowPresetRouteException(Node node) {
        super(BpmErrorCode.S00102, LocaleUtils.getNodeNotAllowPresetRoute());
        this.node = node;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", node.getFlow().getId());
        args.put("flowCode", node.getFlow().getCode());
        args.put("flowName", node.getFlow().getName());
        args.put("nodeId", node.getId());
        args.put("nodeCode", node.getCode());
        args.put("nodeName", node.getName());
    }
}
