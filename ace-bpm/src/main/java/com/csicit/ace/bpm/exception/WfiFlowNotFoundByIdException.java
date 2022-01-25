package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/12 11:10
 */
public class WfiFlowNotFoundByIdException extends BpmSystemException {
    private String id;

    public WfiFlowNotFoundByIdException(String id) {
        super(BpmErrorCode.S00003, LocaleUtils.getWfiFlowNotFoundById());
        this.id = id;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", id);
    }
}
