package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/2 11:51
 */
public class NotAllowAssitDeleteException extends BpmSystemException {
    private String taskId;

    public NotAllowAssitDeleteException(String taskId) {
        super(BpmErrorCode.S00042, LocaleUtils.getNotAllowAssitDelete());
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}
