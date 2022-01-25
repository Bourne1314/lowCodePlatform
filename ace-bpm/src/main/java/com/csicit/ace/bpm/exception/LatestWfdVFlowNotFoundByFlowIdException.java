package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

public class LatestWfdVFlowNotFoundByFlowIdException extends BpmSystemException {
    private String flowId;

    public LatestWfdVFlowNotFoundByFlowIdException(String flowId) {
        super(BpmErrorCode.S00091, LocaleUtils.getLatestWfdVFlowNotFound());
        this.flowId = flowId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
    }
}