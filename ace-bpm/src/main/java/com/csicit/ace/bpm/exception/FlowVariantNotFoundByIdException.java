package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/20 10:58
 */
public class FlowVariantNotFoundByIdException extends BpmSystemException {
    private final String variantId;
    private final String flowId;

    public FlowVariantNotFoundByIdException(String variantId, String flowId) {
        super(BpmErrorCode.S00053, LocaleUtils.getFlowVariantNotFound(variantId, flowId));
        this.variantId = variantId;
        this.flowId = flowId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("variantId", variantId);
        args.put("flowId", flowId);
    }
}
