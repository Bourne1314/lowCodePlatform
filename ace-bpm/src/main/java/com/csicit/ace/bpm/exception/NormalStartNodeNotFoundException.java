package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/26 19:37
 */
public class NormalStartNodeNotFoundException extends BpmSystemException {
    private String id;

    public NormalStartNodeNotFoundException(String id) {
        super(BpmErrorCode.S00017, LocaleUtils.getNormalStartNodeNotFound());
        this.id = id;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", id);
    }
}
