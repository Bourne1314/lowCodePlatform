package com.csicit.ace.bpm;

import java.util.Date;

/**
 * 流程实例
 *
 * @author JonnyJiang
 * @date 2019/9/5 15:58
 */
public interface FlowInstance {
    /**
     * 获取流程实例ID
     *
     * @return 流程实例ID
     * @author JonnyJiang
     * @date 2019/11/8 11:53
     */

    String getId();

    /**
     * 获取流程标识
     *
     * @return 流程标识
     * @author JonnyJiang
     * @date 2019/11/8 11:53
     */

    String getCode();

    /**
     * 获取业务标识
     *
     * @return 业务标识
     * @author JonnyJiang
     * @date 2019/11/8 11:54
     */

    String getBusinessKey();

    /**
     * 获取发起人ID
     *
     * @return 发起人ID
     * @author JonnyJiang
     * @date 2019/11/8 11:54
     */

    String getStarterId();

    /**
     * 获取办结时间
     *
     * @return 办结时间
     * @author JonnyJiang
     * @date 2019/11/11 20:35
     */

    Date getEndTime();

    /**
     * 获取发起时间
     *
     * @return 发起时间
     * @author JonnyJiang
     * @date 2019/11/11 20:37
     */

    Date getStartTime();
}
