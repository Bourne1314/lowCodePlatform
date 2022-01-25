package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * 未找到指定id的系统用户异常
 *
 * @author JonnyJiang
 * @date 2020/5/25 17:58
 */
public class SysUserNotFoundByIdException extends BpmSystemException {
    private String userId;

    public SysUserNotFoundByIdException(String userId) {
        super(BpmErrorCode.S00014, LocaleUtils.getSysUserNotFoundById());
        this.userId = userId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("userId", userId);
    }
}
