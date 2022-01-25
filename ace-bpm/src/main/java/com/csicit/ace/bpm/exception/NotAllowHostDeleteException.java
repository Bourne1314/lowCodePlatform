package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/2 11:43
 */
public class NotAllowHostDeleteException extends BpmSystemException {
    private String taskId;

    public NotAllowHostDeleteException(String taskId) {
        super(BpmErrorCode.S00041, LocaleUtils.getNotAllowHostDelete());
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}
