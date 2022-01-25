package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/26 19:32
 */
public class FlowInstanceNotFoundException extends BpmSystemException {
    private String id;

    public FlowInstanceNotFoundException(String id) {
        super(BpmErrorCode.S00016, LocaleUtils.getFlowInstanceNotFound());
        this.id = id;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", id);
    }
}
