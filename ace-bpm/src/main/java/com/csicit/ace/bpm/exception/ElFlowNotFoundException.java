package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * @author JonnyJiang
 * @date 2020/10/13 10:13
 */
public class ElFlowNotFoundException extends BpmSystemException {
    public ElFlowNotFoundException() {
        super(BpmErrorCode.S00071, LocaleUtils.getElFlowNotFound());
    }
}
