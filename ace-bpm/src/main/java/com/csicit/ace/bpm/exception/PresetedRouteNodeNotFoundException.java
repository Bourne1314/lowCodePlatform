package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/12/18 0:04
 */
public class PresetedRouteNodeNotFoundException extends BpmSystemException {
    private final String wfiFlowId;
    private final Node node;

    public PresetedRouteNodeNotFoundException(String wfiFlowId, Node node) {
        super(BpmErrorCode.S00096, LocaleUtils.getPresetedRouteNodeNotFound());
        this.wfiFlowId = wfiFlowId;
        this.node = node;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("wfiFlowId", wfiFlowId);
        args.put("nodeId", node.getId());
        args.put("nodeName", node.getName());
        args.put("nodeCode", node.getCode());
    }
}
