package com.csicit.ace.interfaces.service;

/**
 * 大屏接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/14 8:28
 */
public interface IBladeVisual {

    /**
     * 大屏消息推送
     *
     * @param displayContent 显示内容
     * @param code           大屏通知标识
     * @return
     * @author zuogang
     * @date 2020/7/30 14:18
     */
    void bladeVisualMsgPush(String displayContent, String code);
}
