package com.csicit.ace.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.bpm.mapper.WfiVFlowWrapper;
import com.csicit.ace.bpm.pojo.domain.WfiVFlowDO;
import com.csicit.ace.bpm.service.WfiVFlowService;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author JonnyJiang
 * @date 2020/8/11 16:23
 */
@Service
public class WfiVFlowServiceImpl extends BaseServiceImpl<WfiVFlowWrapper, WfiVFlowDO> implements WfiVFlowService {
    @Override
    public Integer getFlowVersionByFlowId(String flowId) {
        Integer cnt = count(new UpdateWrapper<WfiVFlowDO>().eq("flow_id", flowId));
        return cnt > 0 ? cnt : FIRST_FLOW_VERSION;
    }
}
