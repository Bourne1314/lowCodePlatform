package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/6/9 10:05
 */
public class DeliverUserNotFoundByUserIdException extends BpmSystemException {
    private String nodeId;
    private String userId;

    public DeliverUserNotFoundByUserIdException(String nodeId, String userId) {
        super(BpmErrorCode.S00027, LocaleUtils.getDeliverUserNotFoundByUserId());
        this.nodeId = nodeId;
        this.userId = userId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeId", nodeId);
        args.put("userId", userId);
    }
}
