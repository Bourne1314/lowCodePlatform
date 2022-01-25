package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/8/25 15:35
 */
public class WfiVFlowNotFoundByFlowVersionException extends BpmSystemException {
    private final String flowId;
    private final Integer flowVersion;

    public WfiVFlowNotFoundByFlowVersionException(String flowId, Integer flowVersion) {
        super(BpmErrorCode.S00061, LocaleUtils.getWfiVFlowNotFound(flowVersion));
        this.flowId = flowId;
        this.flowVersion = flowVersion;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
        args.put("flowVersion", flowVersion);
    }
}
