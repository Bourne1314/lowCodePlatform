package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * @author JonnyJiang
 * @date 2020/7/17 8:41
 */
public class RejectToStepNotSpecifiedException extends BpmSystemException {
    public RejectToStepNotSpecifiedException() {
        super(BpmErrorCode.S00048, LocaleUtils.getRejectToStepNotSpecified());
    }
}
