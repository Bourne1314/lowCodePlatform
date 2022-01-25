package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2021/7/25 19:04
 */
public class WfiRoutePresetNotFoundByFlowIdException extends BpmSystemException {
    private String flowId;

    public WfiRoutePresetNotFoundByFlowIdException(String flowId) {
        super(BpmErrorCode.S00103, LocaleUtils.getWfiRoutePresetNotFoundByFlowId());
        this.flowId = flowId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
    }
}
