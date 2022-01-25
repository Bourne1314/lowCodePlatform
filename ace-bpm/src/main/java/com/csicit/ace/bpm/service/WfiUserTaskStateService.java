package com.csicit.ace.bpm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.bpm.enums.UserTaskState;
import com.csicit.ace.bpm.pojo.domain.WfiUserTaskStateDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/10 8:44
 */
@Transactional
public interface WfiUserTaskStateService extends IService<WfiUserTaskStateDO> {
    /**
     * 修改用户-任务办理状态
     *
     * @param userId
     * @param flowId
     * @param taskId
     * @param state
     * @return
     * @author FourLeaves
     * @date 2020/4/10 10:28
     */
    boolean updateUserTaskState(String userId, String flowId, String taskId, UserTaskState state);

    /**
     * 修改多个用户-任务办理状态
     *
     * @param userIds
     * @param flowId
     * @param taskId
     * @param state
     * @return
     * @author FourLeaves
     * @date 2020/4/10 10:28
     */
    boolean updateUserTaskState(List<String> userIds, String flowId, String taskId, UserTaskState state);

    /**
     * 修改任务办理状态
     *
     * @param taskId
     * @param state
     * @return
     * @author FourLeaves
     * @date 2020/4/10 10:28
     */
    boolean updateTaskState(String taskId, UserTaskState state);
}
