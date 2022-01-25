package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/12 11:05
 */
public class WfdVFlowNotFoundByCodeException extends BpmSystemException {
    private String flowCode;

    public WfdVFlowNotFoundByCodeException(String flowCode) {
        super(BpmErrorCode.S00002, LocaleUtils.getWfdVFlowNotFoundByCode());
        this.flowCode = flowCode;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("FlowCode", flowCode);
    }
}
