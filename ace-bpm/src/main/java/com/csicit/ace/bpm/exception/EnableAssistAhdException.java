package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * 确定主办人后，其他人不允许作为协办人继续办理
 *
 * @author JonnyJiang
 * @date 2020/6/30 8:41
 */
public class EnableAssistAhdException extends BpmSystemException {
    private String taskId;

    public EnableAssistAhdException(String taskId) {
        super(BpmErrorCode.S00033, LocaleUtils.getEnableAssistAhd());
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}
