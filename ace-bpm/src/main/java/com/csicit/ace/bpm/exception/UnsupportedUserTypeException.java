package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/1 11:25
 */
public class UnsupportedUserTypeException extends BpmSystemException {
    private Integer userType;

    public UnsupportedUserTypeException(Integer userType) {
        super(BpmErrorCode.S00036, LocaleUtils.getUnsupportedUserType());
        this.userType = userType;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("userType", userType);
    }
}
