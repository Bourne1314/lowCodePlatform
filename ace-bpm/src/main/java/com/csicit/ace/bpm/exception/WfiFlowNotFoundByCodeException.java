package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/12 11:28
 */
public class WfiFlowNotFoundByCodeException extends BpmSystemException {
    private String flowCode;
    private String businessKey;

    public WfiFlowNotFoundByCodeException(String flowCode, String businessKey) {
        super(BpmErrorCode.S00004, LocaleUtils.getWfiFlowNotFoundByCode());
        this.flowCode = flowCode;
        this.businessKey = businessKey;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("FlowCode", flowCode);
        args.put("BusinessKey", businessKey);
    }
}
