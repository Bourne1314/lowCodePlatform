package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/16 14:09
 */
public class RejectToNodeIsNotFirstTaskException extends BpmSystemException {
    private final String expectTaskId;
    private final String actualTaskId;

    public RejectToNodeIsNotFirstTaskException(String expectTaskId, String actualTaskId) {
        super(BpmErrorCode.S00044, LocaleUtils.getRejectToNodeIsNotFirstTask());
        this.expectTaskId = expectTaskId;
        this.actualTaskId = actualTaskId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("expectTaskId", expectTaskId);
        args.put("actualTaskId", actualTaskId);
    }
}