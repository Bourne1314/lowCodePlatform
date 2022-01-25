package com.csicit.ace.bpm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.bpm.mapper.WfiDeliverWrapper;
import com.csicit.ace.bpm.pojo.domain.WfiDeliverDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.service.WfiDeliverService;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/9/5 18:07
 */
@Service
public class WfiDeliverServiceImpl extends BaseServiceImpl<WfiDeliverWrapper, WfiDeliverDO> implements WfiDeliverService {
    @Override
    public List<WfiDeliverDO> listByFlowId(String flowId) {
        return list(new QueryWrapper<WfiDeliverDO>().eq("FLOW_ID", flowId).orderByAsc("DELIVER_TIME"));
    }

    @Override
    public void updateDeliverInfo(String wfiDeliverId, DeliverInfo deliverInfo) {
        update(new WfiDeliverDO(), new UpdateWrapper<WfiDeliverDO>().set("DELIVER_INFO", JSONObject.toJSONString(deliverInfo)).eq("ID", wfiDeliverId));
    }
}
