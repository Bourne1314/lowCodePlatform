package com.csicit.ace.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.bpm.enums.UserTaskState;
import com.csicit.ace.bpm.mapper.WfiUserTaskStateMapper;
import com.csicit.ace.bpm.pojo.domain.WfiUserTaskStateDO;
import com.csicit.ace.bpm.service.WfiUserTaskStateService;
import com.csicit.ace.common.exception.RException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/10 8:44
 */
@Service
public class WfiUserTaskStateServiceImpl extends ServiceImpl<WfiUserTaskStateMapper, WfiUserTaskStateDO> implements WfiUserTaskStateService {

    /**
     * 修改用户-任务办理状态
     *
     * @param taskId
     * @param state
     * @return
     * @author FourLeaves
     * @date 2020/4/10 10:28
     */
    @Override
    public boolean updateTaskState(String taskId, UserTaskState state) {
        return update(new WfiUserTaskStateDO(), new UpdateWrapper<WfiUserTaskStateDO>()
                .eq("task_id", taskId).set("state", state));
    }

    /**
     * 修改用户-任务办理状态
     *
     * @param userId
     * @param taskId
     * @param state
     * @return
     * @author FourLeaves
     * @date 2020/4/10 10:28
     */
    @Override
    public boolean updateUserTaskState(String userId, String flowId, String taskId, UserTaskState state) {
        WfiUserTaskStateDO wfiUserTaskStateDO = getOne(
                new QueryWrapper<WfiUserTaskStateDO>().eq("user_id", userId)
                        .eq("task_id", taskId));
        if (wfiUserTaskStateDO == null) {
            wfiUserTaskStateDO = new WfiUserTaskStateDO();
            wfiUserTaskStateDO.setState(state.getState());
            wfiUserTaskStateDO.setTaskId(taskId);
            wfiUserTaskStateDO.setUserId(userId);
            wfiUserTaskStateDO.setFlowId(flowId);
            return save(wfiUserTaskStateDO);
        } else {
            // 如果更新的优先级大于数据库里的  那么更新
            if (UserTaskState.compareByState(state.getState(), wfiUserTaskStateDO.getState()) || Objects.equals(state.getState(), UserTaskState.UN_CLAIM.getState())) {
                wfiUserTaskStateDO.setState(state.getState());
                return updateById(wfiUserTaskStateDO);
            }
            return true;
        }
    }

    @Override
    public boolean updateUserTaskState(List<String> userIds, String flowId, String taskId, UserTaskState state) {
        for (String userId : userIds) {
            if (!updateUserTaskState(userId, flowId, taskId, state)) {
                throw new RException("");
            }
        }
        return true;
    }
}
