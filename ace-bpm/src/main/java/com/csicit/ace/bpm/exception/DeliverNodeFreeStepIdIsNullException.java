package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/12/3 2:58
 */
public class DeliverNodeFreeStepIdIsNullException extends BpmSystemException {
    private final String wfiFlowId;
    private final String taskId;
    private final String nodeId;
    private final String nodeName;

    public DeliverNodeFreeStepIdIsNullException(String wfiFlowId, String taskId, String nodeId, String nodeName) {
        super(BpmErrorCode.S00086, LocaleUtils.getDeliverNodeFreeStepIdIsNull(nodeName));
        this.wfiFlowId = wfiFlowId;
        this.taskId = taskId;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("wfiFlowId", wfiFlowId);
        args.put("taskId", taskId);
        args.put("nodeId", nodeId);
        args.put("nodeName", nodeName);
    }
}
