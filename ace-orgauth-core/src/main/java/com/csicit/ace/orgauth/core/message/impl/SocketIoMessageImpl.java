package com.csicit.ace.orgauth.core.message.impl;

import com.csicit.ace.orgauth.core.message.BaseMessageImpl;
import com.csicit.ace.orgauth.core.message.IMessage;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/7/5 11:28
 */
@Service("msgSocket")
public class SocketIoMessageImpl extends BaseMessageImpl implements IMessage {

    @Override
    public String getServiceName() {
        return "msgSocket";
    }

    @Override
    public String getServicType() {
        return "站内通知";
    }

    @Override
    public void sendMsg(SysMessageDO sysMessageDO) {
        if (msgPush != null) {
            msgPush.sendMsg(getMsgContent(sysMessageDO, "socket"));
        }
    }

}
