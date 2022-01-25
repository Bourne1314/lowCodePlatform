package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/11/12 10:01
 */
public class LinkNotFoundByIdException extends BpmSystemException {
    private final String id;
    private final String flowId;

    public LinkNotFoundByIdException(String id, String flowId) {
        super(BpmErrorCode.S00078, LocaleUtils.getLinkNotFoundById());
        this.id = id;
        this.flowId = flowId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", id);
        args.put("flowId", flowId);
    }
}
