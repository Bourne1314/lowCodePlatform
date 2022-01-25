package com.csicit.ace.cloudImpl.service;

import com.csicit.ace.interfaces.service.IBladeVisual;
import org.springframework.stereotype.Service;

/**
 * 大屏消息
 *
 * @author zuogang
 * @date 2020/7/30 14:35
 */
@Service("bladeVisual")
public class BladeVisualImpl extends BaseImpl implements IBladeVisual {
    /**
     * 大屏消息推送
     *
     * @param displayContent 显示内容
     * @param code           大屏通知标识
     * @return
     * @author zuogang
     * @date 2020/7/30 14:18
     */
    @Override
    public void bladeVisualMsgPush(String displayContent, String code) {
        gatewayService.bladeVisualMsgPush(displayContent, code, appName);
    }
}
