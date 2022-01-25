package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * 未指定表单设置中的结果字段异常
 *
 * @author JonnyJiang
 * @date 2020/7/17 11:11
 */
public class FormResultFieldNotSpecifiedException extends BpmSystemException {
    public FormResultFieldNotSpecifiedException() {
        super(BpmErrorCode.S00050, LocaleUtils.getResultVarNameNotSpecified());
    }
}