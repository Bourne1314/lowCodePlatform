package com.csicit.ace.bpm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.bpm.exception.*;
import com.csicit.ace.bpm.mapper.WfdVFlowMapper;
import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.csicit.ace.bpm.service.WfdVFlowService;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


/**
 * 流程定义 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 11:43:23
 */
@Service
public class WfdVFlowServiceImpl extends BaseServiceImpl<WfdVFlowMapper, WfdVFlowDO> implements WfdVFlowService {

    @Override
    public boolean saveWorkFlow(JSONObject json) {

        return false;
    }

    @Override
    public void updateLatestByFlowId(String flowId, Integer latest) {
        baseMapper.update(new WfdVFlowDO(), new UpdateWrapper<WfdVFlowDO>().set("IS_LATEST", latest).eq("FLOW_ID", flowId).eq("APP_ID", appName));
    }

    @Override
    public WfdVFlowDO getLatestByFlowId(String flowId) {
        List<WfdVFlowDO> wfdVFlows = list(new QueryWrapper<WfdVFlowDO>().eq("APP_ID", appName).eq("FLOW_ID", flowId).and(o -> o.eq("IS_LATEST", 1)));
        if (wfdVFlows.size() > 1) {
            throw new FoundMoreThenOneLatestWfdVFlowByFlowIdException(flowId, wfdVFlows);
        } else if (wfdVFlows.size() == 1) {
            return wfdVFlows.get(0);
        }
        return null;
    }

    @Override
    public WfdVFlowDO getLatestByCode(String code) {
        List<WfdVFlowDO> wfdVFlows = list(new QueryWrapper<WfdVFlowDO>().eq("APP_ID", appName).eq("CODE", code).and(o -> o.eq("IS_LATEST", 1)));
        if (wfdVFlows.size() > 1) {
            throw new FoundMoreThenOneLatestWfdVFlowByCodeException(code, wfdVFlows);
        } else if (wfdVFlows.size() == 1) {
            return wfdVFlows.get(0);
        }
        return null;
    }

    @Override
    public WfdVFlowDO getEffectiveByCode(String code, LocalDateTime date) {
        List<WfdVFlowDO> wfdVFlows = list(new QueryWrapper<WfdVFlowDO>().eq("APP_ID", appName).eq("CODE", code).le("VERSION_BEGIN_DATE", date).and(o -> o.isNull("VERSION_END_DATE").or().ge("VERSION_END_DATE", date)));
        if (wfdVFlows.size() > 1) {
            throw new FoundMoreThenOneEffectiveWfdVFlowByCodeException(code, date, wfdVFlows);
        } else if(wfdVFlows.size() == 0) {
            throw new WfdVFlowNotFoundByCodeException(code);
        }
        return wfdVFlows.get(0);
    }

    @Override
    public WfdVFlowDO getEffectiveByFlowId(String flowId, LocalDateTime date) {
        List<WfdVFlowDO> wfdVFlows = list(new QueryWrapper<WfdVFlowDO>().eq("APP_ID", appName).eq("FLOW_ID", flowId).le("VERSION_BEGIN_DATE", date).and(o -> o.isNull("VERSION_END_DATE").or().ge("VERSION_END_DATE", date)));
        if (wfdVFlows.size() > 1) {
            throw new FoundMoreThenOneEffectiveWfdVFlowByFlowIdException(flowId, date, wfdVFlows);
        } else if(wfdVFlows.size() == 0) {
            throw new WfdVFlowNotFoundByIdException(flowId);
        }
        return wfdVFlows.get(0);
    }

    @Override
    public WfdVFlowDO getMaxVersion(String flowId) {
        Optional<WfdVFlowDO> wfdVFlowOp = this.list(new QueryWrapper<WfdVFlowDO>().eq("APP_ID", appName).eq("flow_id", flowId)).stream().max(Comparator.comparingInt(WfdVFlowDO::getFlowVersion));
        return wfdVFlowOp.orElse(null);
    }

    @Override
    public Integer updateVersionEndDate(String flowId, int flowVersion, LocalDateTime versionEndDate) {
        return baseMapper.update(new WfdVFlowDO(), new UpdateWrapper<WfdVFlowDO>().set("VERSION_END_DATE", versionEndDate).eq("FLOW_ID", flowId).eq("APP_ID", appName).eq("FLOW_VERSION", flowVersion));
    }

    @Override
    public Integer updateUsedById(String id, int used, String processDefinitionId) {
        return baseMapper.update(new WfdVFlowDO(), new UpdateWrapper<WfdVFlowDO>().set("IS_USED", used).set("PROCESS_DEFINITION_ID", processDefinitionId).eq("APP_ID", appName).eq("ID", id));
    }

    @Override
    public WfdVFlowDO getByVersion(String flowId, Integer version) {
        List<WfdVFlowDO> wfdVFlows = list(new QueryWrapper<WfdVFlowDO>().eq("APP_ID", appName).eq("flow_id", flowId).eq("flow_version", version));
        if (wfdVFlows.size() > 0) {
            return wfdVFlows.get(0);
        }
        return null;
    }
}
