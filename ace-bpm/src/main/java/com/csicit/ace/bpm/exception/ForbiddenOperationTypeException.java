package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/1 11:05
 */
public class ForbiddenOperationTypeException extends BpmSystemException {
    private Integer operationType;

    public ForbiddenOperationTypeException(Integer operationType) {
        super(BpmErrorCode.S00034, LocaleUtils.getForbiddenOperationType());
        this.operationType = operationType;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("operationType", operationType);
    }
}