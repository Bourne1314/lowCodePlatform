package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2021/5/20 11:24
 */
public class DeliverUserSortIndexIsNullException extends BpmSystemException {
    private final String userId;
    private final String realName;

    public DeliverUserSortIndexIsNullException(String userId, String realName) {
        super(BpmErrorCode.S00101, LocaleUtils.getDeliverUserSortIndexIsNull(userId, realName));
        this.userId = userId;
        this.realName = realName;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("userId", userId);
        args.put("realName", realName);
    }
}
