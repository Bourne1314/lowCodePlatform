package com.csicit.ace.data.persistent.messenger.impl;


import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.data.persistent.messenger.AbstractMessengerImpl;
import com.csicit.ace.data.persistent.messenger.IMessenger;
import org.springframework.stereotype.Service;

/** 
 * 邮件信使实现
 *
 * @author shanwj
 * @date 2020/4/13 18:37
 */
@Service
public class EmailMessengerImpl extends AbstractMessengerImpl implements IMessenger {

    @Override
    public String getAnnotationValue() {
        return getAnnotationValue(this.getClass());
    }

    @Override
    public String getType() {
        return "邮件";
    }

    @Override
    public void sendMsg(SysMessageDO sysMessageDO) {

    }
}
