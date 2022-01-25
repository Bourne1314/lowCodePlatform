package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/2 10:54
 */
public class UnsupportedTaskPendingUserTypeException extends BpmSystemException {
    private UserType userType;

    public UnsupportedTaskPendingUserTypeException(UserType userType) {
        super(BpmErrorCode.S00039, LocaleUtils.getUnsupportedTaskPendingUserType());
        this.userType = userType;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("userType", userType.getValue());
    }
}
