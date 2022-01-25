package com.csicit.ace.data.persistent.messenger.impl;


import com.csicit.ace.data.persistent.messenger.AbstractMessengerImpl;
import com.csicit.ace.data.persistent.messenger.IMessenger;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import org.springframework.stereotype.Service;

/** 
 * 微信公众号信使实现
 *
 * @author shanwj
 * @date 2020/4/13 18:37
 */
@Service
public class WxSubscriptionMessengerImpl extends AbstractMessengerImpl implements IMessenger {

    @Override
    public String getAnnotationValue() {
        return getAnnotationValue(this.getClass());
    }

    @Override
    public String getType() {
        return "微信公众号";
    }

    @Override
    public void sendMsg(SysMessageDO sysMessageDO) {

    }
}
