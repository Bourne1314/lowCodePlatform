package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/12/18 0:43
 */
public class AllowModifyPresetRouteByDescendantMustBeTrueException extends BpmSystemException {
    private Node node;

    public AllowModifyPresetRouteByDescendantMustBeTrueException(Node node) {
        super(BpmErrorCode.S00097, LocaleUtils.getAllowModifyPresetRouteByDescendantMustBeTrue(node.getName()));
        this.node = node;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", node.getFlow().getId());
        args.put("nodeId", node.getId());
        args.put("nodeName", node.getName());
        args.put("nodeCode", node.getCode());
    }
}
