package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * @author JonnyJiang
 * @date 2020/10/13 10:08
 */
public class ElFlowInstanceIdNotFoundException extends BpmSystemException {
    public ElFlowInstanceIdNotFoundException() {
        super(BpmErrorCode.S00069, LocaleUtils.getElFlowInstanceIdNotFound());
    }
}
