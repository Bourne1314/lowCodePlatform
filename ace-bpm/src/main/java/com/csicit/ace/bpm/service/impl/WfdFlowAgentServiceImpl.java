package com.csicit.ace.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.bpm.mapper.WfdFlowAgentMapper;
import com.csicit.ace.bpm.pojo.domain.WfdFlowAgentDO;
import com.csicit.ace.bpm.pojo.domain.WfdFlowCategoryDO;
import com.csicit.ace.bpm.pojo.domain.WfdFlowDO;
import com.csicit.ace.bpm.pojo.vo.FlowAgentDO;
import com.csicit.ace.bpm.service.WfdFlowAgentService;
import com.csicit.ace.bpm.service.WfdFlowCategoryService;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * 工作代办规则 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */
@Service("wfdFlowAgentService")
public class WfdFlowAgentServiceImpl extends BaseServiceImpl<WfdFlowAgentMapper, WfdFlowAgentDO> implements
        WfdFlowAgentService {

    @Autowired
    WfdFlowCategoryService wfdFlowCategoryService;

    @Autowired
    WfdFlowService wfdFlowService;

    /**
     * 保存代办规则
     *
     * @param flowAgentDO
     * @return
     * @author zuogang
     * @date 2019/9/17 9:15
     */
    @Override
    public boolean saveAgent(FlowAgentDO flowAgentDO) {
        List<WfdFlowAgentDO> flowAgentDOs = new ArrayList<>(16);
        List<String> flowIDList = new ArrayList<>(16);
        if (Objects.equals(flowAgentDO.getFlowType(), "parentNode")) {
            // 获取该应用的所有流程作为代办规则

            wfdFlowCategoryService.list(new QueryWrapper<WfdFlowCategoryDO>()
                    .eq("app_id", flowAgentDO.getAppId())).stream().forEach(wfdFlowCategoryDO -> {
                flowIDList.addAll(wfdFlowService.list(new QueryWrapper<WfdFlowDO>()
                        .eq("category_id", wfdFlowCategoryDO.getId())).stream().map(WfdFlowDO::getId)
                        .collect(Collectors.toList()));
            });

        } else if (Objects.equals(flowAgentDO.getFlowType(), "categoryNode")) {
            // 获取该流程类别下的所有流程作为代办规则
            WfdFlowCategoryDO wfdFlowCategoryDO = wfdFlowCategoryService.getOne(new QueryWrapper<WfdFlowCategoryDO>()
                    .eq("id", flowAgentDO.getFlowId()));
            if (wfdFlowCategoryDO != null) {
                flowIDList.addAll(wfdFlowService.list(new QueryWrapper<WfdFlowDO>()
                        .eq("category_id", wfdFlowCategoryDO.getId()))
                        .stream().map(WfdFlowDO::getId).collect(Collectors.toList()));
            }
        } else if (Objects.equals(flowAgentDO.getFlowType(), "childNode")) {
            // 获取该流程作为代办规则
            flowIDList.add(flowAgentDO.getFlowId());
        }

        if (flowIDList.size() > 0) {
            // 求出当前使用用户已有的代办规则，与flowIDList冲突的ID，删除并重新追加
            List<WfdFlowAgentDO> oldAgents = list(new QueryWrapper<WfdFlowAgentDO>()
                    .eq("origin_user_id", securityUtils.getCurrentUserId()));
            List<String> removeIdList = new ArrayList<>(16);
            oldAgents.stream().forEach(wfdFlowAgentDO -> {
                if (flowIDList.contains(wfdFlowAgentDO.getFlowId())) {
                    removeIdList.add(wfdFlowAgentDO.getId());
                }
            });
            if (removeIdList.size() > 0) {
                removeByIds(removeIdList);
            }

            flowIDList.stream().forEach(flowId -> {
                WfdFlowAgentDO wfdFlowAgentDO = new WfdFlowAgentDO();
                wfdFlowAgentDO.setId(UUID.randomUUID().toString());

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                if (Objects.equals(0, flowAgentDO.getBeginTimeFlag()) && StringUtils.isNotBlank(flowAgentDO
                        .getBeginTime())) {
                    // 设置有效期起始
                    wfdFlowAgentDO.setBeginTime(LocalDateTime.parse(flowAgentDO.getBeginTime(), df));

                }
                if (Objects.equals(0, flowAgentDO.getEndTimeFlag()) && StringUtils.isNotBlank(flowAgentDO
                        .getEndTime())) {
                    // 设置有效期结束
                    wfdFlowAgentDO.setEndTime(LocalDateTime.parse(flowAgentDO.getEndTime(), df));
                }
                wfdFlowAgentDO.setEnabled(flowAgentDO.getEnabled());
                wfdFlowAgentDO.setFlowId(flowId);
                wfdFlowAgentDO.setOriginUserId(securityUtils.getCurrentUserId());
                wfdFlowAgentDO.setAgentUserId(flowAgentDO.getAgentUserId());
                wfdFlowAgentDO.setAgentUserName(flowAgentDO.getAgentUserName());
                flowAgentDOs.add(wfdFlowAgentDO);
            });
        }

        if (flowAgentDOs.size() > 0) {
            if (!saveBatch(flowAgentDOs)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 启用委托规则
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2019/9/17 9:15
     */
    @Override
    public boolean enableRule(String[] ids) {
        List<WfdFlowAgentDO> agentDOS = new ArrayList<>(16);
        for (String id : ids) {
            WfdFlowAgentDO wfdFlowAgentDO = getById(id);
            if (wfdFlowAgentDO != null) {
                wfdFlowAgentDO.setEnabled(1);
                agentDOS.add(wfdFlowAgentDO);
            }
        }
        if (!updateBatchById(agentDOS)) {
            return false;
        }
        return true;
    }

    /**
     * 禁用委托规则
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2019/9/17 9:15
     */
    @Override
    public boolean disableRule(String[] ids) {
        List<WfdFlowAgentDO> agentDOS = new ArrayList<>(16);
        for (String id : ids) {
            WfdFlowAgentDO wfdFlowAgentDO = getById(id);
            if (wfdFlowAgentDO != null) {
                wfdFlowAgentDO.setEnabled(0);
                agentDOS.add(wfdFlowAgentDO);
            }
        }
        if (!updateBatchById(agentDOS)) {
            return false;
        }
        return true;
    }
}
