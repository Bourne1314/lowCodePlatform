package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * 分支指定结果不能为空
 *
 * @author JonnyJiang
 * @date 2020/5/28 20:03
 */
public class LinkResultIsNullException extends BpmSystemException {
    public LinkResultIsNullException() {
        super(BpmErrorCode.S00019, LocaleUtils.getLinkResultIsNull());
    }
}