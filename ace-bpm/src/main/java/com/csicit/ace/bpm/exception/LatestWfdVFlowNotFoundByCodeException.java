package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

public class LatestWfdVFlowNotFoundByCodeException extends BpmSystemException {
    private String code;

    public LatestWfdVFlowNotFoundByCodeException(String code) {
        super(BpmErrorCode.S00093, LocaleUtils.getLatestWfdVFlowNotFound());
        this.code = code;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("code", code);
    }
}