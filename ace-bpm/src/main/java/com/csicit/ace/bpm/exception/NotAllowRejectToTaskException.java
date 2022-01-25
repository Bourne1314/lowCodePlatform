package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * @author JonnyJiang
 * @date 2020/7/16 17:21
 */
public class NotAllowRejectToTaskException extends BpmSystemException {
    public NotAllowRejectToTaskException() {
        super(BpmErrorCode.S00047, LocaleUtils.getNotAllowRejectToTask());
    }
}
