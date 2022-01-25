package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/20 11:53
 */
public class WfdGlobalVariantNotFoundException extends BpmSystemException {

    private String variantId;

    public WfdGlobalVariantNotFoundException(String variantId) {
        super(BpmErrorCode.S00054, LocaleUtils.getWfdGlobalVariantNotFound(variantId));
        this.variantId = variantId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("variantId", variantId);
    }
}
