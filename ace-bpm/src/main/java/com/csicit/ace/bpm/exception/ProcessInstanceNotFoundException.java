package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/18 15:20
 */
public class ProcessInstanceNotFoundException extends BpmSystemException {
    private String id;

    public ProcessInstanceNotFoundException(String id) {
        super(BpmErrorCode.S00012, LocaleUtils.getProcessInstanceNotFound());
        this.id = id;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", id);
    }
}
