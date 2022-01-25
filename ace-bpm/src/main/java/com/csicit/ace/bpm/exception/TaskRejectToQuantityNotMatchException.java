package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/16 10:16
 */
public class TaskRejectToQuantityNotMatchException extends BpmSystemException {
    /**
     * 期待数量
     */
    private final Integer expect;
    /**
     * 实际数量
     */
    private final Integer actual;

    public TaskRejectToQuantityNotMatchException(Integer expect, Integer actual) {
        super(BpmErrorCode.S00043, LocaleUtils.getTaskRejectToQuantityNotMatch());
        this.expect = expect;
        this.actual = actual;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("expect", expect);
        args.put("actual", actual);
    }
}
