package com.csicit.ace.bpm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.pojo.domain.WfiTaskPendingDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/6/28 14:36
 */
@Transactional
public interface WfiTaskPendingService extends IService<WfiTaskPendingDO> {
    /**
     * 清理待办工作
     *
     * @param flowIds 流程实例id列表
     * @author JonnyJiang
     * @date 2020/6/28 16:11
     */

    void clear(List<String> flowIds);

    /**
     * 获取待办工作
     *
     * @param flowId 流程实例id
     * @param taskId 任务id
     * @param userId 用户id
     * @return com.csicit.ace.bpm.pojo.domain.WfiTaskPendingDO
     * @author JonnyJiang
     * @date 2020/7/1 11:15
     */

    WfiTaskPendingDO getByTaskId(String flowId, String taskId, String userId);

    /**
     * 获取待办工作
     *
     * @param taskIds 任务id集合
     * @return 待办工作
     * @author JonnyJiang
     * @date 2020/7/2 9:18
     */

    List<WfiTaskPendingDO> listByTaskIds(String[] taskIds);

    /**
     * 获取待办工作
     *
     * @param flowIds 流程实例ID集合
     * @return 待办工作
     * @author JonnyJiang
     * @date 2020/7/2 10:24
     */

    List<WfiTaskPendingDO> listByFlowIds(List<String> flowIds);

    /**
     * 获取待办工作
     *
     * @param flowId 流程实例ID
     * @return 待办工作
     * @author JonnyJiang
     * @date 2020/7/2 11:24
     */

    List<WfiTaskPendingDO> listByFlowId(String flowId);
}
