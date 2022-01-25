package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/9/21 21:56
 */
public class WfiRoutePresetNotFoundByTaskIdException extends BpmSystemException {
    private String taskId;

    public WfiRoutePresetNotFoundByTaskIdException(String taskId) {
        super(BpmErrorCode.S00064, LocaleUtils.getWfiRoutePresetNotFoundByTaskId());
        this.taskId = taskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
    }
}
