package com.csicit.ace.orgauth.core.message.impl;

import com.csicit.ace.orgauth.core.message.BaseMessageImpl;
import com.csicit.ace.orgauth.core.message.IMessage;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/7/5 14:55
 */
@Service("msgEmail")
public class EmailMessageImpl extends BaseMessageImpl implements IMessage {

    @Override
    public String getServiceName() {
        return "msgEmail";
    }

    @Override
    public String getServicType() {
        return "邮件";
    }

    @Override
    public void sendMsg(SysMessageDO sysMessageDO) {
        emailDo();
        msgPush.sendMsg(getMsgContent(sysMessageDO,"email"));
    }

    private void emailDo(){
        System.out.println("this is email do things");
    }
}
