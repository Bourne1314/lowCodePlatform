package com.csicit.ace.data.persistent.messenger;

import com.csicit.ace.common.pojo.domain.SysMessageDO;

/**
 * 消息信使接口
 *
 * @author shanwj
 * @date 2020/4/13 17:58
 */
public interface IMessenger {

    /**
     * 获取信使名称注解
     * 后续
     * @return
     */
    String getAnnotationValue();

    /**
     * 获取信使类型
     * @return
     */
    String getType();

    /**
     * 发送消息
     * @param sysMessageDO 消息对象
     */
    void sendMsg(SysMessageDO sysMessageDO);
}
