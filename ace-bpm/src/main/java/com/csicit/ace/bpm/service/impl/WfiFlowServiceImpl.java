package com.csicit.ace.bpm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.activiti.utils.QueryUtil;
import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.enums.TableName;
import com.csicit.ace.bpm.exception.*;
import com.csicit.ace.bpm.mapper.WfiFlowWrapper;
import com.csicit.ace.bpm.pojo.domain.*;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.DeliverNode;
import com.csicit.ace.bpm.pojo.vo.free.FreeStep;
import com.csicit.ace.bpm.pojo.vo.free.FreeStepInfo;
import com.csicit.ace.bpm.pojo.vo.preset.PresetInfo;
import com.csicit.ace.bpm.pojo.vo.preset.PresetRoute;
import com.csicit.ace.bpm.pojo.vo.preset.PresetUser;
import com.csicit.ace.bpm.pojo.vo.wfd.*;
import com.csicit.ace.bpm.service.*;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.interfaces.service.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author JonnyJiang
 * @date 2019/9/5 18:07
 */
@Service
public class WfiFlowServiceImpl extends BaseServiceImpl<WfiFlowWrapper, WfiFlowDO> implements WfiFlowService {
    private static final String COL_FLOW_CODE = "FLOW_CODE";
    private static final String COL_FLOW_NO = "FLOW_NO";
    private static final String COL_ID = "ID";
    private static final String COL_APP_ID = "APP_ID";
    /**
     * 缓存时间
     */
    @Value("${ace.bpm.wfiFlow.cacheTime:600}")
    private long CACHE_TIME;
    @Autowired
    private WfiVFlowService wfiVFlowService;
    @Autowired
    private WfdVFlowService wfdVFlowService;
    @Autowired
    private WfiTaskPendingService wfiTaskPendingService;
    @Autowired
    private WfiRoutePresetService wfiRoutePresetService;
    @Autowired
    private QueryUtil queryUtil;
    @Autowired
    private IUser iUser;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private BpmAdapter bpmAdapter;

    /**
     * 执行流程定义更新，不校验
     *
     * @param wfiFlow     流程实例
     * @param model       流程定义
     * @param flowVersion 当前版本
     * @author JonnyJiang
     * @date 2020/11/12 9:28
     */

    private void updateModel(WfiFlowDO wfiFlow, String model, Integer flowVersion) {
        Integer currentVersion = wfiVFlowService.getFlowVersionByFlowId(wfiFlow.getId());
        WfiVFlowDO currentWfiVFlow;
        if (IntegerUtils.equals(currentVersion, WfiVFlowService.FIRST_FLOW_VERSION)) {
            WfdVFlowDO wfdVFlow = wfdVFlowService.getById(wfiFlow.getVFlowId());
            if (wfdVFlow == null) {
                throw new WfdVFlowNotFoundByIdException(wfiFlow.getVFlowId());
            }
            currentWfiVFlow = new WfiVFlowDO();
            currentWfiVFlow.setFlowId(wfiFlow.getId());
            currentWfiVFlow.setModel(wfiFlow.getModel());
            currentWfiVFlow.setBpmn(wfdVFlow.getBpmn());
            currentWfiVFlow.setFlowVersion(WfiVFlowService.FIRST_FLOW_VERSION);
            currentWfiVFlow.setVersionEndDate(LocalDateTime.now());
            wfiVFlowService.save(currentWfiVFlow);
        } else {
            List<WfiVFlowDO> wfiVFlows = wfiVFlowService.list(new QueryWrapper<WfiVFlowDO>().eq("flow_id", wfiFlow.getId()).eq("flow_version", flowVersion));
            if (wfiVFlows.size() > 0) {
                currentWfiVFlow = wfiVFlows.get(0);
                if (currentWfiVFlow.getVersionEndDate() != null) {

                } else {
                    wfiVFlowService.update(new WfiVFlowDO(), new UpdateWrapper<WfiVFlowDO>().set("version_end_date", LocalDateTime.now()).eq("id", currentWfiVFlow.getId()));
                }
            } else {
                throw new WfiVFlowNotFoundByFlowVersionException(wfiFlow.getId(), flowVersion);
            }
        }
        if (IntegerUtils.equals(currentVersion, flowVersion)) {
            // 如果版本一致，则可以直接更新
            WfiVFlowDO wfiVFlow = new WfiVFlowDO();
            wfiVFlow.setFlowId(wfiFlow.getId());
            wfiVFlow.setModel(model);
            wfiVFlow.setBpmn(currentWfiVFlow.getBpmn());
            wfiVFlow.setFlowVersion(flowVersion + 1);
            wfiVFlowService.save(wfiVFlow);
            deleteCache(wfiFlow.getId());
            update(new WfiFlowDO(), new UpdateWrapper<WfiFlowDO>().set("model", model).eq("id", wfiFlow.getId()));
        } else {
            throw new WfiVFlowVersionObsoleteException(wfiFlow.getId(), currentVersion, flowVersion);
        }
    }

    @Override
    public void updateByNew(WfiFlowDO wfiFlowNew, Integer flowVersion) {
        WfiFlowDO wfiFlow = getById(wfiFlowNew.getFlowId());
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByIdException(wfiFlowNew.getFlowId());
        }
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Flow newFlow = FlowUtils.getFlow(wfiFlowNew.getModel());
        updateFlowByNew(flow, newFlow);
        updateModel(wfiFlow, flow.toJson(), flowVersion);
    }

    @Override
    public void updateNodeFreeSteps(TaskInstance taskInstance, FreeStepInfo freeStepInfo) {
        Flow flow = taskInstance.getFlow();
        Node node = flow.getNodeById(freeStepInfo.getNodeId());
        if (!NodeType.Free.isEquals(node.getNodeType())) {
            throw new NodeTypeNotMatchException(NodeType.Free, node.getNodeType());
        }
        // 判断当前任务是不是自由流节点的任务
        Node currentNode = taskInstance.getNode();
        if (StringUtils.equals(currentNode.getId(), node.getId())) {
            // 如果是当前节点与目标节点是同一节点
            WfiDeliverDO wfiDeliverFrom = queryUtil.getWfiDeliverFrom(freeStepInfo.getTaskId());
            DeliverInfo deliverInfoFrom = wfiDeliverFrom.getDeliverInfoClass();
            DeliverNode deliverNodeFrom = deliverInfoFrom.getDeliverNodeByNodeId(node.getId());
            // 还原当前自由流步骤（含）之前的自由流设置
            NodeFreeStep nodeFreeStep = node.getNodeFreeStepById(deliverNodeFrom.getNodeFreeStepId());
            // 清理前序设置的自由流步骤，自动忽略序号小于当前步骤的自由流步骤
            NodeFreeStep cNodeFreeStep;
            for (int i = 0; i < node.getNodeFreeSteps().size(); ) {
                cNodeFreeStep = node.getNodeFreeSteps().get(i);
                if (cNodeFreeStep.getStepNo() > nodeFreeStep.getStepNo()) {
                    node.getNodeFreeSteps().remove(i);
                } else {
                    i++;
                }
            }
            // 添加新设置的自由流步骤
            FreeStep cFreeStep;
            for (int i = 0; i < freeStepInfo.getFreeSteps().size(); ) {
                cFreeStep = freeStepInfo.getFreeSteps().get(i);
                if (cFreeStep.getStepNo() > nodeFreeStep.getStepNo()) {
                    NodeFreeStep newNodeFreeStep = new NodeFreeStep();
                    newNodeFreeStep.setStepNo(cFreeStep.getStepNo());
                    newNodeFreeStep.setId(cFreeStep.getId());
                    newNodeFreeStep.setHostMode(cFreeStep.getHostMode());
                    newNodeFreeStep.getDeliverUsers().addAll(cFreeStep.getDeliverUsers());
                    node.addNodeFreeStep(newNodeFreeStep);
                } else {
                    i++;
                }
            }
        } else {
            // 如果不是同一节点
            node.getNodeFreeSteps().clear();
            FreeStep cFreeStep;
            for (int i = 0; i < freeStepInfo.getFreeSteps().size(); i++) {
                cFreeStep = freeStepInfo.getFreeSteps().get(i);
                NodeFreeStep newNodeFreeStep = new NodeFreeStep();
                newNodeFreeStep.setStepNo(cFreeStep.getStepNo());
                newNodeFreeStep.setId(cFreeStep.getId());
                newNodeFreeStep.setHostMode(cFreeStep.getHostMode());
                newNodeFreeStep.getDeliverUsers().addAll(cFreeStep.getDeliverUsers());
                node.addNodeFreeStep(newNodeFreeStep);
            }
        }
        updateModel(taskInstance.getWfiFlow(), flow.toJson(), freeStepInfo.getFlowVersion());
    }

    /**
     * 更新流程定义
     *
     * @param flow    流程定义
     * @param newFlow 新流程定义
     * @author JonnyJiang
     * @date 2020/11/12 10:27
     */

    private void updateFlowByNew(Flow flow, Flow newFlow) {
        // 遍历所有节点，更新可变属性
        for (Node node : flow.getNodes()) {
            updateNodeByNew(node, newFlow.getNodeById(node.getId()));
        }
        // 遍历所有分支，更新可变属性
        for (Link link : flow.getLinks()) {
            updateLinkByNew(link, newFlow.getLinkById(link.getId()));
        }
        // 遍历所有属性，更新可变属性
        flow.setMsgTemplateCodeWaitJob(newFlow.getMsgTemplateCodeWaitJob());
        flow.setVariableFieldWaitJob(newFlow.getVariableFieldWaitJob());
        flow.setMsgTemplateCodeOverJob(newFlow.getMsgTemplateCodeOverJob());
        flow.setVariableFieldOverJob(newFlow.getVariableFieldOverJob());
        flow.setEnableSetUrgency(newFlow.getEnableSetUrgency());
        flow.setFormUrl(newFlow.getFormUrl());
        flow.setFormDatasourceId(newFlow.getFormDatasourceId());
        flow.setFormDataTable(newFlow.getFormDataTable());
        flow.setFormIdName(newFlow.getFormIdName());
        flow.setFormCascadeDel(newFlow.getFormCascadeDel());
        flow.setFormFields(newFlow.getFormFields());
        flow.setFormSecretLevelField(newFlow.getFormSecretLevelField());
        flow.setFormResultField(newFlow.getFormResultField());
        flow.setFormOperations(newFlow.getFormOperations());
        flow.setFormSaveOperate(newFlow.getFormSaveOperate());
        flow.setVariants(newFlow.getVariants());
        flow.setRules(newFlow.getRules());
        flow.setFlowChart(newFlow.getFlowChart());
        flow.setEvents(newFlow.getEvents());
        flow.setMsgTemplateNewWork(newFlow.getMsgTemplateNewWork());
        flow.setMsgTemplateFinished(newFlow.getMsgTemplateFinished());
        flow.setMsgScopeFinished(newFlow.getMsgScopeFinished());
        flow.setMsgChannel(newFlow.getMsgChannel());
        flow.setSyncSettings(newFlow.getSyncSettings());
        // 查询监控权限，永远以最新的流程定义为准
//        flow.setQueryAuthId(newFlow.getQueryAuthId());
//        flow.setAdminAuthId(newFlow.getAdminAuthId());
        flow.setModel(newFlow.getModel());
    }

    /**
     * 更新流程节点属性
     * 流入流出模式暂不支持修改
     *
     * @param node    原节点
     * @param newNode 新节点
     * @author JonnyJiang
     * @date 2020/11/17 14:04
     */

    private void updateNodeByNew(Node node, Node newNode) {
        node.setName(newNode.getName());
        node.setCode(newNode.getCode());
        node.setRemark(newNode.getRemark());
        if (NodeType.Start.isEquals(node.getNodeType())) {
            node.setInitAuthId(newNode.getInitAuthId());
        } else if (NodeType.Manual.isEquals(node.getNodeType()) || NodeType.Free.isEquals(node.getNodeType())) {
            node.setHostMode(newNode.getHostMode());
            node.setHostId(newNode.getHostId());
            node.setEnableAssistAhd(newNode.getEnableAssistAhd());
            node.setAllowPassMode(newNode.getAllowPassMode());
            node.setAllowPassRuleId(newNode.getAllowPassRuleId());
            node.setAllowPassRuleModifier(newNode.getAllowPassRuleModifier());
            node.setAllowPassAfterHostCompleted(newNode.getAllowPassAfterHostCompleted());
            node.setWaitForPassUsers(newNode.getWaitForPassUsers());
            node.setWaitForPassCount(newNode.getWaitForPassCount());
            node.setWaitForPassPercent(newNode.getWaitForPassPercent());
            node.setWorkResultPrompt(newNode.getWorkResultPrompt());
            node.setWorkResultOptions(newNode.getWorkResultOptions());
            node.setMinSelResult(newNode.getMinSelResult());
            node.setMaxSelResult(newNode.getMaxSelResult());
            node.setEnableVote(newNode.getEnableVote());
            node.setVoteViewMode(newNode.getVoteViewMode());
            node.setAllowHostInvite(newNode.getAllowHostInvite());
            node.setAllowAssistInvite(newNode.getAllowAssistInvite());
            node.setRemarkMode(newNode.getRemarkMode());
            node.setAllowHostAuthorizeViewer(newNode.getAllowHostAuthorizeViewer());
            node.setAllowAssitAuthorizeViewer(newNode.getAllowAssitAuthorizeViewer());
            node.setAllowHostDelete(newNode.getAllowHostDelete());
            node.setAllowAssitDelete(newNode.getAllowAssitDelete());
            node.setAllowPassToAgent(newNode.getAllowPassToAgent());
            node.setMultiInstanceMode(newNode.getMultiInstanceMode());
            node.setParticipateRule(newNode.getParticipateRule());
            node.setEnableManualSelParticipant(newNode.getEnableManualSelParticipant());
            node.setEnableHostOnly(newNode.getEnableHostOnly());
            node.setEnableReject(newNode.getEnableReject());
            node.setRejectTo(newNode.getRejectTo());
            node.setRejectToStep(newNode.getRejectToStep());
//            node.setEnableRejectHaltBranches(newNode.getEnableRejectHaltBranches());
//            node.setEnableRejectedReflow(newNode.getEnableRejectedReflow());
            node.setAllowConfigReject(newNode.getAllowConfigReject());
            node.setAllowDrawBack(newNode.getAllowDrawBack());
            node.setOperationsHostBd(newNode.getOperationsHostBd());
            node.setOperationsHostAd(newNode.getOperationsHostAd());
            node.setOperationsAssistBd(newNode.getOperationsAssistBd());
            node.setOperationsAssistAd(newNode.getOperationsAssistAd());
            node.setTimeLimitGeneral(newNode.getTimeLimitGeneral());
            node.setTimeLimitUrgent(newNode.getTimeLimitUrgent());
            node.setTimeLimitExtraUrgent(newNode.getTimeLimitExtraUrgent());
            node.setTimeLimitUnitG(newNode.getTimeLimitUnitG());
            node.setTimeLimitUnitU(newNode.getTimeLimitUnitU());
            node.setTimeLimtUnitEu(newNode.getTimeLimtUnitEu());
            node.setOverTimeMode(newNode.getOverTimeMode());
            node.setOverTimeRemindTime(newNode.getOverTimeRemindTime());
            node.setOverTimeRemindIntv(newNode.getOverTimeRemindIntv());
            node.setOverTimeMsgChannel(newNode.getOverTimeMsgChannel());
            node.setFlowInMsgTemplate(newNode.getFlowInMsgTemplate());
            node.setFlowOutMsgTemplate(newNode.getFlowOutMsgTemplate());
            node.setFlowInMsgSendTo(newNode.getFlowInMsgSendTo());
            node.setFlowOutMsgSendTo(newNode.getFlowOutMsgSendTo());
            node.setFlowInMsgChannel(newNode.getFlowInMsgChannel());
            node.setFlowOutMsgChannel(newNode.getFlowOutMsgChannel());
            node.setFlowInSelParticipantMode(newNode.getFlowInSelParticipantMode());
            node.setFlowInNodeSelParticipant(newNode.getFlowInNodeSelParticipant());
            node.setFlowInMsgTemplateCode(newNode.getFlowInMsgTemplateCode());
            node.setVariableFieldInMsg(newNode.getVariableFieldInMsg());
            node.setAllowPresetRoute(newNode.getAllowPresetRoute());
            node.setMustPresetRoute(newNode.getMustPresetRoute());
        } else if (NodeType.End.isEquals(node.getNodeType())) {
            node.setResultValue(newNode.getResultValue());
            node.setSaveResultValue(newNode.getSaveResultValue());
        }
        node.setEvents(newNode.getEvents());
        node.setAllowPresetRoute(newNode.getAllowPresetRoute());
        node.setMustPresetRoute(newNode.getMustPresetRoute());
    }

    /**
     * 更新分支
     *
     * @param link    原分支
     * @param newLink 新分支
     * @author JonnyJiang
     * @date 2020/11/17 14:05
     */

    private void updateLinkByNew(Link link, Link newLink) {
        link.setSortIndex(newLink.getSortIndex());
        link.setIsMandatory(newLink.getIsMandatory());
        link.setIsDefault(newLink.getIsDefault());
        link.setConditionMode(newLink.getConditionMode());
        link.setRuleExpression(newLink.getRuleExpression());
        link.setResult(newLink.getResult());
        link.setCountMode(newLink.getCountMode());
        link.setOperator(newLink.getOperator());
        link.setCount(newLink.getCount());
    }

    @Override
    public WfiFlowDO getByCode(String code, String businessKey) {
        return baseMapper.selectOne(new QueryWrapper<WfiFlowDO>().eq("APP_ID", appName).eq("FLOW_CODE", code).eq("BUSINESS_KEY", businessKey));
    }

    private void cache(String id, WfiFlowDO wfiFlow) {
        cacheUtil.hset(TableName.WFI_FLOW, id, wfiFlow, CACHE_TIME);
    }

    private void deleteCache(String id) {
        if (StringUtils.isNotEmpty(id)) {
            cacheUtil.hDelete(TableName.WFI_FLOW, id);
        }
    }

    @Override
    public WfiFlowDO getById(Serializable id) {
        try {
            Object value = cacheUtil.hget(TableName.WFI_FLOW, (String) id);
            if (value != null) {
                return JsonUtils.castObject(value, WfiFlowDO.class);
            }
        }catch (Exception ex) {

        }
        WfiFlowDO wfiFlow = super.getById(id);
        if (wfiFlow != null) {
            cache(wfiFlow.getId(), wfiFlow);
        }
        return wfiFlow;
    }

    @Override
    public Collection<WfiFlowDO> listByIds(Collection<? extends Serializable> idList) {
        List<Serializable> ids = new ArrayList<>();
        Collection<WfiFlowDO> wfiFlows = new ArrayList<>();
        for (Serializable id : idList) {
            Object value = cacheUtil.hget(TableName.WFI_FLOW, (String) id);
            if (value != null) {
                wfiFlows.add(JsonUtils.castObject(value, WfiFlowDO.class));
            } else {
                ids.add(id);
            }
        }
        if (ids.size() > 0) {
            Collection<WfiFlowDO> list = super.listByIds(ids);
            if (list.size() > 0) {
                wfiFlows.addAll(list);
                for (WfiFlowDO wfiFlow : list) {
                    cache(wfiFlow.getId(), wfiFlow);
                }
            }
        }
        return wfiFlows;
    }

    @Override
    public boolean save(WfiFlowDO entity) {
        deleteCache(entity.getId());
        return super.save(entity);
    }

    @Override
    public boolean update(WfiFlowDO entity, Wrapper<WfiFlowDO> updateWrapper) {
        deleteCache(entity.getId());
        return super.update(entity, updateWrapper);
    }

    @Override
    public Integer deleteFormByBusinessKey(String tableName, String columnName, String businessKey) {
        if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(columnName) || StringUtils.isEmpty(businessKey)) {
            return 0;
        }
        return baseMapper.deleteFormByBusinessKey(columnNameFilter(tableName), columnNameFilter(columnName), businessKey);
    }

    @Override
    public Integer syncData(String tableName, String idColName, String businessKey, String columnName, Object val) {
        return baseMapper.syncData(columnNameFilter(tableName), columnNameFilter(columnName), val, columnNameFilter(idColName), businessKey);
    }

    @Override
    public String getAvailableFlowNo(String flowCode, String wfiFlowId, String flowNo) {
        String availableFlowNo = flowNo;
        int extSeqNo = 1;
        while (count(new QueryWrapper<WfiFlowDO>().eq(COL_FLOW_CODE, flowCode).eq(COL_FLOW_NO, availableFlowNo).eq(COL_APP_ID, appName).notIn(COL_ID, wfiFlowId)) > 0) {
            availableFlowNo = flowNo + "-" + extSeqNo++;
        }
        return availableFlowNo;
    }

    @Override
    public void updateFlowNo(String flowCode, String wfiFlowId, String flowNo) {
        deleteCache(wfiFlowId);
        update(new WfiFlowDO(), new UpdateWrapper<WfiFlowDO>().set(COL_FLOW_NO, getAvailableFlowNo(flowCode, wfiFlowId, flowNo)).eq(COL_ID, wfiFlowId));
    }

    @Override
    public List<WfiFlowDO> listInvalidFromActiviti() {
        return list(new QueryWrapper<WfiFlowDO>().notInSql("ID", "SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST"));
    }

    @Override
    public Boolean businessExists(String tableName, String idName, String businessKey) {
        Integer count = baseMapper.getBusinessCount(columnNameFilter(tableName), columnNameFilter(idName), businessKey);
        return count > 0;
    }

    @Override
    public Object getFormValue(String tableName, String columnName, String formIdName, String businessKey) {
        return baseMapper.getFormValue(columnNameFilter(tableName), columnNameFilter(columnName), columnNameFilter(formIdName), businessKey);
    }

    private void resolveNodePreset(Node node, PresetInfo presetInfo) {
        for (Link link : node.getFlowOutLinks()) {
            // 根据分支找到预设路径
            PresetRoute presetRoute = presetInfo.getPresetRoutes().stream().filter(o -> StringUtils.equals(link.getId(), o.getFlowInLinkId())).findFirst().orElse(null);
            if (presetRoute != null) {
                link.setPresetedLink(IntegerUtils.TRUE_VALUE);
                link.getToNode().setPresetedNode(IntegerUtils.TRUE_VALUE);
                if (presetRoute.getPresetUsers().size() > 0) {
                    // 如果预设了经办人
                    link.getToNode().clearPresetedUsers();
                    for (PresetUser presetUser : presetRoute.getPresetUsers()) {
                        NodePresetUser nodePresetUser = new NodePresetUser();
                        nodePresetUser.setSortIndex(presetUser.getSortIndex());
                        nodePresetUser.setNode(link.getToNode());
                        nodePresetUser.setUserId(presetUser.getUserId());
                        nodePresetUser.setUserType(presetUser.getUserType());
                        nodePresetUser.setRealName(presetUser.getRealName());
                        link.getToNode().addPresetedUser(nodePresetUser);
                    }
                } else {
                    if (IntegerUtils.isTrue(node.getMustPresetRouteUser())) {
                        throw new MustPresetRouteUserException(node.getCode(), node.getName(), presetInfo.getTaskId());
                    }
                }
            } else {
                link.setPresetedLink(IntegerUtils.FALSE_VALUE);
                // 判断节点是不是预设节点，如果是预设节点，则不处理，否则设置为非预设节点
                if (!presetInfo.getPresetRoutes().stream().anyMatch(o -> StringUtils.equals(o.getNodeId(), link.getToNodeId()))) {
                    link.getToNode().setPresetedRoute(IntegerUtils.FALSE_VALUE);
                    link.getToNode().clearPresetedUsers();
                }
            }
            resolveNodePreset(link.getToNode(), presetInfo);
        }
    }

    @Override
    public void presetRoute(PresetInfo presetInfo, WfiFlowDO wfiFlow, Node node) {
        node.setPresetedRoute(IntegerUtils.TRUE_VALUE);
        resolveNodePreset(node, presetInfo);
        updateModel(wfiFlow, node.getFlow().toJson(), presetInfo.getFlowVersion());
        // 保存预设信息
        wfiRoutePresetService.remove(new QueryWrapper<WfiRoutePresetDO>().eq("task_id", presetInfo.getTaskId()));
        WfiRoutePresetDO wfiRoutePreset = new WfiRoutePresetDO();
        wfiRoutePreset.setFlowId(presetInfo.getFlowId());
        wfiRoutePreset.setTaskId(presetInfo.getTaskId());
        wfiRoutePreset.setPresetTime(LocalDateTime.now());
        wfiRoutePreset.setPresetInfo(JSONObject.toJSONString(presetInfo));
        wfiRoutePresetService.save(wfiRoutePreset);
    }

    private Boolean isSpecialChar(char c) {
        if (Objects.equals(c, '\"')) {
            return true;
        } else if (Objects.equals(c, '_')) {
            return true;
        }
        return false;
    }

    private String columnNameFilter(String columnName) {
        if (StringUtils.isNotEmpty(columnName)) {
            char[] chars = columnName.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (Character.isLetter(c) || isSpecialChar(c) || Character.isDigit(c)) {
                    continue;
                }
                columnName = columnName.replace(String.valueOf(c), "");
            }
        }
        return columnName;
    }
}