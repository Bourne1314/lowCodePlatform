package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/20 14:05
 */
public class UnsupportedVariableIdException extends BpmSystemException {
    private String variantId;

    public UnsupportedVariableIdException(String variantId) {
        super(BpmErrorCode.S00055, LocaleUtils.getUnsupportedVariableId());
        this.variantId = variantId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("variantId", variantId);
    }
}