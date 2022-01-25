package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

public class WfiRoutePresetNotFoundByPresetedNodeException extends BpmSystemException {
    private final WfiFlowDO wfiFlow;
    private final Node node;

    public WfiRoutePresetNotFoundByPresetedNodeException(WfiFlowDO wfiFlow, Node node) {
        super(BpmErrorCode.S00098, LocaleUtils.getWfiRoutePresetNotFoundByPresetedNode(node.getName()));
        this.wfiFlow = wfiFlow;
        this.node = node;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("wfiFlowId", wfiFlow.getId());
        args.put("nodeId", node.getId());
        args.put("nodeName", node.getName());
        args.put("nodeCode", node.getCode());
    }
}
