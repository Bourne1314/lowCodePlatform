package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/11/25 10:17
 */
public class NodeFreeStepIdIsNullException extends BpmSystemException {
    private final String taskId;
    private final String wfiDeliverId;

    public NodeFreeStepIdIsNullException(String taskId, String wfiDeliverId) {
        super(BpmErrorCode.S00080, LocaleUtils.getNodeFreeStepIdIsNull());
        this.taskId = taskId;
        this.wfiDeliverId = wfiDeliverId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
        args.put("wfiDeliverId", wfiDeliverId);
    }
}