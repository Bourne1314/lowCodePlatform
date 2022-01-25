package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;

import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/6/2 15:27
 */
public class DeliverUserExistException extends BpmSystemException {
    private String wfiDeliverId;
    List<SysUserDO> users;

    public DeliverUserExistException(String wfiDeliverId, List<SysUserDO> users) {
        super(BpmErrorCode.S00021, LocaleUtils.getDeliverUserExist(users));
        this.wfiDeliverId = wfiDeliverId;
        this.users = users;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("wfiDeliverId", wfiDeliverId);
        args.put("users", users);
    }
}
