package com.csicit.ace.orgauth.core.service;

import com.csicit.ace.common.pojo.domain.BladeVisualMsgDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 大屏消息 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2020-07-29 16:49:49
 */
@Transactional
public interface BladeVisualMsgService extends IBaseService<BladeVisualMsgDO> {
    /**
     * 大屏消息推送
     *
     * @param displayContent 显示内容
     * @param code           大屏通知标识
     * @param appName        应用标识
     * @return
     * @author zuog
     */
    void bladeVisualMsgPush(String displayContent, String code, String appName);
}
