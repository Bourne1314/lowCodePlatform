package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/12 8:04
 */
public class WfdVFlowNotFoundByIdException extends BpmSystemException {
    private String id;

    public WfdVFlowNotFoundByIdException(String id) {
        super(BpmErrorCode.S00001, LocaleUtils.getWfdVFlowNotFoundById());
        this.id = id;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        super.onLogging(args);
        args.put("id", id);
    }
}