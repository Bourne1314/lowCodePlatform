package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/8/25 15:42
 */
public class WfiVFlowVersionObsoleteException extends BpmSystemException {

    private final String flowId;
    private final Integer currentVersion;
    private final Integer flowVersion;

    public WfiVFlowVersionObsoleteException(String flowId, Integer currentVersion, Integer flowVersion) {
        super(BpmErrorCode.S00062, LocaleUtils.getWfiVFlowVersionObsolete());
        this.flowId = flowId;
        this.currentVersion = currentVersion;
        this.flowVersion = flowVersion;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
        args.put("currentVersion", currentVersion);
        args.put("flowVersion", flowVersion);
    }
}