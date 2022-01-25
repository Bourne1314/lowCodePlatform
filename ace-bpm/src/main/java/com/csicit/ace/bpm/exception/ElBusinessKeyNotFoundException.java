package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * @author JonnyJiang
 * @date 2020/10/13 10:11
 */
public class ElBusinessKeyNotFoundException extends BpmSystemException {
    public ElBusinessKeyNotFoundException() {
        super(BpmErrorCode.S00070, LocaleUtils.getElBusinessKeyNotFound());
    }
}