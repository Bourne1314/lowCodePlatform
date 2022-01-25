package com.csicit.ace.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.bpm.mapper.WfiRoutePresetWrapper;
import com.csicit.ace.bpm.pojo.domain.WfiRoutePresetDO;
import com.csicit.ace.bpm.service.WfiRoutePresetService;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/8/11 16:23
 */
@Service
public class WfiRoutePresetServiceImpl extends BaseServiceImpl<WfiRoutePresetWrapper, WfiRoutePresetDO> implements WfiRoutePresetService {

    @Override
    public WfiRoutePresetDO getByTaskId(String taskId) {
        return list(new QueryWrapper<WfiRoutePresetDO>().eq("task_id", taskId).orderByDesc("preset_time")).stream().findFirst().orElse(null);
    }

    @Override
    public List<WfiRoutePresetDO> listByFlowId(String flowId) {
        return list(new QueryWrapper<WfiRoutePresetDO>().eq("flow_id", flowId).orderByAsc("preset_time"));
    }

    @Override
    public Integer countByFlowId(String flowId) {
        return count(new QueryWrapper<WfiRoutePresetDO>().eq("flow_id", flowId));
    }
}
