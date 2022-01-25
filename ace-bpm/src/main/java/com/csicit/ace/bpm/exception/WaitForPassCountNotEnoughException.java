package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/12/9 19:59
 */
public class WaitForPassCountNotEnoughException extends BpmSystemException {
    private final String wfiFlowId;
    private final Node node;
    private final int deliverUserCount;

    public WaitForPassCountNotEnoughException(String wfiFlowId, Node node, int deliverUserCount) {
        super(BpmErrorCode.S00089, LocaleUtils.getWaitForPassCountNotEnough(node.getName(), node.getWaitForPassCount(), deliverUserCount));
        this.wfiFlowId = wfiFlowId;
        this.node = node;
        this.deliverUserCount = deliverUserCount;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("wfiFlowId", wfiFlowId);
        args.put("nodeId", node.getId());
        args.put("nodeName", node.getName());
        args.put("nodeCode", node.getCode());
        args.put("waitForPassCount", node.getWaitForPassCount());
        args.put("deliverUserCount", deliverUserCount);
    }
}