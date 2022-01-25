package com.csicit.ace.orgauth.core.message;

import com.csicit.ace.common.pojo.domain.SysMessageDO;

/**
 * @author shanwj
 * @date 2019/7/5 11:19
 *
 * 消息发送接口
 */
public interface IMessage {

    /**
     * 获取接口服务名称
     *
     */
    String getServiceName();

    /**
     * 获取接口类型
     *
     */
    String getServicType();

    /**
     * 发送消息
     */
    void sendMsg(SysMessageDO sysMessageDO);

}
