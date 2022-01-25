package com.csicit.ace.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.bpm.mapper.WfiTaskPendingMapper;
import com.csicit.ace.bpm.pojo.domain.WfiTaskPendingDO;
import com.csicit.ace.bpm.service.WfiTaskPendingService;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/6/28 14:36
 */
@Service
public class WfiTaskPendingServiceImpl extends BaseServiceImpl<WfiTaskPendingMapper, WfiTaskPendingDO> implements WfiTaskPendingService {
    @Override
    public void clear(List<String> flowIds) {
        remove(new QueryWrapper<WfiTaskPendingDO>().eq("app_id", appName).in("flow_id", flowIds));
    }

    @Override
    public WfiTaskPendingDO getByTaskId(String flowId, String taskId, String userId) {
        List<WfiTaskPendingDO> wfiTaskPendings = list(new QueryWrapper<WfiTaskPendingDO>().eq("flow_id", flowId).eq("task_id", taskId).eq("user_id", userId).eq("app_id", appName));
        if (wfiTaskPendings.size() > 0) {
            return wfiTaskPendings.get(0);
        }
        return null;
    }

    @Override
    public List<WfiTaskPendingDO> listByTaskIds(String[] taskIds) {
        return list(new QueryWrapper<WfiTaskPendingDO>().in("task_id", (Object[]) taskIds).eq("app_id", appName).orderByAsc("create_time"));
    }

    @Override
    public List<WfiTaskPendingDO> listByFlowIds(List<String> flowIds) {
        return list(new QueryWrapper<WfiTaskPendingDO>().in("flow_id", flowIds).eq("app_id", appName).orderByAsc("create_time"));
    }

    @Override
    public List<WfiTaskPendingDO> listByFlowId(String flowId) {
        return list(new QueryWrapper<WfiTaskPendingDO>().eq("flow_id", flowId).eq("app_id", appName).orderByAsc("create_time"));
    }
}
