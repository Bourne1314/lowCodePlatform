package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * @author JonnyJiang
 * @date 2020/7/16 15:46
 */
public class TaskRejectToIsNotSpecifiedException extends BpmSystemException {

    public TaskRejectToIsNotSpecifiedException() {
        super(BpmErrorCode.S00046, LocaleUtils.getTaskRejectIsNotSpecified());
    }
}
