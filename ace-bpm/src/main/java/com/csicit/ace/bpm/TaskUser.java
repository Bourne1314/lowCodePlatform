package com.csicit.ace.bpm;

/**
 * 任务经办人
 *
 * @author JonnyJiang
 * @date 2019/11/5 8:38
 */
public interface TaskUser {
    /**
     * 获取身份类型
     *
     * @return 身份类型
     * @author JonnyJiang
     * @date 2019/11/5 8:44
     */

    String getUserType();

    /**
     * 获取用户ID
     *
     * @return 用户ID
     * @author JonnyJiang
     * @date 2019/11/5 8:44
     */

    String getUserId();

    /**
     * 获取任务实例ID
     *
     * @return 任务实例ID
     * @author JonnyJiang
     * @date 2019/11/5 8:45
     */

    String getTaskInstanceId();

    /**
     * 获取流程实例ID
     *
     * @return 流程实例ID
     * @author JonnyJiang
     * @date 2019/11/5 8:45
     */

    String getFlowInstanceId();
}
