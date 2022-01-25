package com.csicit.ace.platform.core.service;

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
     * 获取单条数据
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/7/30 8:29
     */
    BladeVisualMsgDO getInfo(String id);

    /**
     * 新增
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/7/30 8:29
     */
    boolean saveBladeVisualMsg(BladeVisualMsgDO instance);

    /**
     * 修改
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/7/30 8:48
     */
    boolean updBladeVisualMsg(BladeVisualMsgDO instance);

    /**
     * 删除
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/7/30 9:03
     */
    boolean delBladeVisualMsg(String id);

    /**
     * 业务平台内部定时任务
     * 扫描大屏消息表数据，在时间范围内向前台推送消息
     *
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    void bladeVisualMsgPush();
}
