package com.csicit.ace.bpm.activiti.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.bpm.*;
import com.csicit.ace.bpm.FlowInstance;
import com.csicit.ace.bpm.activiti.ProcessVariableName;
import com.csicit.ace.bpm.activiti.TaskVariableName;
import com.csicit.ace.bpm.activiti.WfiDeliverTasks;
import com.csicit.ace.bpm.activiti.utils.QueryUtil;
import com.csicit.ace.bpm.el.WfdFlowElService;
import com.csicit.ace.bpm.enums.HostMode;
import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.enums.OperationType;
import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.exception.*;
import com.csicit.ace.bpm.pojo.domain.*;
import com.csicit.ace.bpm.pojo.vo.*;
import com.csicit.ace.bpm.pojo.vo.preset.PresetInfo;
import com.csicit.ace.bpm.pojo.vo.preset.PresetRoute;
import com.csicit.ace.bpm.pojo.vo.process.*;
import com.csicit.ace.bpm.pojo.vo.wfd.*;
import com.csicit.ace.bpm.pojo.vo.wfi.WfiNodeUserVO;
import com.csicit.ace.bpm.pojo.vo.wfi.WfiNodeVO;
import com.csicit.ace.bpm.service.*;
import com.csicit.ace.bpm.utils.*;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.interfaces.service.IFile;
import com.csicit.ace.interfaces.service.IOrg;
import com.csicit.ace.interfaces.service.ISecurity;
import com.csicit.ace.interfaces.service.IUser;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.checkerframework.common.util.report.qual.ReportOverride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JonnyJiang
 * @date 2019/12/6 17:26
 */
@Component("bpmAdapter")
public class BpmAdapterImpl implements BpmAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BpmAdapterImpl.class);
    private static final Integer SIZE_SORT_BY_PINYIN = 30;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private TaskService taskService;
    @Autowired
    WfdFlowService wfdFlowService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    private WfdVFlowService wfdVFlowService;
    @Autowired
    private WfiFlowService wfiFlowService;
    @Autowired
    private ISecurity iSecurity;
    @Autowired
    private IUser iUser;
    @Autowired
    private WfiDeliverService wfiDeliverService;
    @Autowired
    private QueryUtil queryUtil;
    @Autowired
    private RuleUtils ruleUtils;
    @Autowired
    private WfdDelegateWorkService wfdDelegateWorkService;
    @Autowired
    private WfiUserTaskStateService wfiUserTaskStateService;
    @Autowired
    private WfiFocusedWorkService wfiFocusedWorkService;
    @Autowired
    private WfiTaskPendingService wfiTaskPendingService;
    @Autowired
    private WfiVFlowService wfiVFlowService;
    @Autowired
    private WfiRoutePresetService wfiRoutePresetService;
    @Autowired
    private WfdCollectionUtils wfdCollectionUtils;
    @Autowired
    private WfdFlowElService wfdFlowElService;
    @Autowired
    private IOrg iorg;
    @Autowired
    private BpmAdapter bpmAdapter;
    @Autowired
    private IFile ifile;


    @Override
    public List<TaskInstance> listAllTasksByFlowInstanceId(String flowInstanceId) {
        List<HistoricTaskInstance> tasks = queryUtil.getHistoricTaskInstanceQuery(flowInstanceId).list();
        return com.csicit.ace.bpm.activiti.impl.historic.TaskInstanceImpl.castToBpmTask(tasks);
    }

    @Override
    public List<TaskInstance> listCompletedTasksByFlowInstanceId(String flowInstanceId) {
        List<HistoricTaskInstance> tasks = queryUtil.getHistoricTaskInstanceQuery(flowInstanceId).finished().orderByHistoricTaskInstanceEndTime().asc().list();
        return com.csicit.ace.bpm.activiti.impl.historic.TaskInstanceImpl.castToBpmTask(tasks);
    }

    @Override
    public WfdVFlowDO getEffectiveWfdVFlowByCode(String code) {
        WfdVFlowDO wfdVFlow = wfdVFlowService.getEffectiveByCode(code, LocalDateTime.now());
        if (wfdVFlow == null) {
            throw new WfdVFlowNotFoundByCodeException(code);
        }
        return wfdVFlow;
    }

    @Override
    public List<TaskUser> getTaskUsersByTaskId(String taskId) {
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
        return TaskUserImpl.castToTaskUsers(identityLinks);
    }

    @Override
    public List<String> getUserIdsByNodeId(String flowInstanceId, String nodeId) {
        List<String> userIds = new ArrayList<>();
        List<HistoricTaskInstance> tasks = queryUtil.getHistoricTaskInstanceQuery(flowInstanceId).taskName(nodeId).list();
        for (HistoricTaskInstance task : tasks) {
            System.out.println("getUserIdsByNodeId");
            System.out.println(task);
            userIds.addAll(taskService.getIdentityLinksForTask(task.getId()).stream().map(IdentityLink::getUserId)
                    .collect(Collectors.toList()));
        }
        return userIds;
    }

    @Override
    public List<TaskInstance> getTasksByNodeId(String flowInstanceId, String nodeId) {
        List<TaskInstance> taskInstances = new ArrayList<>();
        List<HistoricTaskInstance> tasks = queryUtil.getHistoricTaskInstanceQuery(flowInstanceId).taskName(nodeId).orderByHistoricTaskInstanceStartTime().asc().list();
        Collection<WfiFlowDO> wfiFlows = wfiFlowService.listByIds(tasks.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet()));
        for (HistoricTaskInstance task : tasks) {
            WfiFlowDO wfiFlow = getWfiFlowById(wfiFlows, task.getProcessInstanceId());
            taskInstances.add(new com.csicit.ace.bpm.activiti.impl.historic.TaskInstanceImpl(task, wfiFlow));
        }
        return taskInstances;
    }

    @Override
    public List<HistoricTaskInstance> getTasksByNodeIds(String flowInstanceId, List<String> nodeIds) {
        return queryUtil.getHistoricTaskInstanceQuery(flowInstanceId).taskNameIn(nodeIds).orderByHistoricTaskInstanceStartTime().asc().list();
    }

    @Override
    public List<NodeVo> listNodesByFlowCode(String flowCode) {
        WfdVFlowDO wfdVFlow = getEffectiveWfdVFlowByCode(flowCode);
        Flow flow = FlowUtils.getFlow(wfdVFlow.getModel());
        List<NodeVo> nodes = new ArrayList<>();
        for (Node node : flow.getNodes()) {
            nodes.add(new NodeVo(node));
        }
        return nodes;
    }

    @Override
    public List<NodeVo> listManualNodesByFlowCode(String flowCode) {
        WfdVFlowDO wfdVFlow = getEffectiveWfdVFlowByCode(flowCode);
        Flow flow = FlowUtils.getFlow(wfdVFlow.getModel());
        List<NodeVo> nodes = new ArrayList<>();
        for (Node node : flow.getNodes()) {
            if (NodeType.Manual.isEquals(node.getNodeType()) || NodeType.Free.isEquals(node.getNodeType())) {
                nodes.add(new NodeVo(node));
            }
        }
        return nodes;
    }

    @Override
    public WfiFlowDO getWfiFlowById(String id) {
        WfiFlowDO wfiFlow = wfiFlowService.getById(id);
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByIdException(id);
        }
        return wfiFlow;
    }

    @Override
    public WfiFlowDO getWfiFlowByBusinessKey(String code, String businessKey) {
        WfiFlowDO wfiFlow = wfiFlowService.getByCode(code, businessKey);
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByCodeException(code, businessKey);
        }
        return wfiFlow;
    }

    @Override
    public Date getNodeStartTime(String flowInstanceId, String nodeCode) {
        WfiFlowDO wfiFlow = getWfiFlowById(flowInstanceId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeByCode(nodeCode);
        List<HistoricTaskInstance> historicTaskInstances = queryUtil.getHistoricTaskInstanceQuery(flowInstanceId)
                .taskName(node.getId()).orderByHistoricTaskInstanceEndTime().asc().list();
        if (historicTaskInstances.size() > 0) {
            return historicTaskInstances.get(0).getStartTime();
        }
        return null;
    }

    @Override
    public Date getNodeEndTime(String flowInstanceId, String nodeCode) {
        WfiFlowDO wfiFlow = getWfiFlowById(flowInstanceId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeByCode(nodeCode);
        List<HistoricTaskInstance> historicTaskInstances = queryUtil.getHistoricTaskInstanceQuery(flowInstanceId)
                .taskName(node.getId()).orderByHistoricTaskInstanceEndTime().desc().list();
        if (historicTaskInstances.size() > 0) {
            return historicTaskInstances.get(0).getEndTime();
        }
        return null;
    }

    @Override
    public FlowVO getHandingProcessByTaskId(String taskId) {
        TaskInstance task = getTaskInstanceById(taskId);
        return getHandingProcessByBusinessKey(task.getFlowCode(), task.getFlowBusinessKey());
    }

    private FlowVO getNewFlowVO(Flow flow) {
        FlowVO flowVo = new FlowVO();
        flowVo.setId(flow.getId());
        flowVo.setName(flow.getName());
        flowVo.setCode(flow.getCode());
        return flowVo;
    }

    private List<SysUserDO> listDeliverUsers(List<WfiDeliverDO> wfiDelivers) {
        // 所有办理过的用户ID
        List<String> deliverUserIds = wfiDelivers.stream().map(WfiDeliverDO::getUserId).collect(Collectors.toList());
        // 所有办理过的用户
        return iUser.getUsersByIds(deliverUserIds);
    }

    @Override
    public FlowVO getHandingProcessByBusinessKey(String flowCode, String businessKey) {
        WfiFlowDO wfiFlow = getWfiFlowByBusinessKey(flowCode, businessKey);
        securityUtils.getSession().setAttribute(SessionAttribute.WFD_FLOW_EL_FLOW_ID, wfiFlow.getId());
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        FlowVO flowVo = getNewFlowVO(flow);
        flowVo.setFlowInstanceId(wfiFlow.getId());

        HistoricProcessInstance historicProcessInstance = queryUtil.getHistoricProcessInstance(wfiFlow.getId());
        if (historicProcessInstance == null) {
            throw new ProcessInstanceNotFoundException(wfiFlow.getId());
        }
        if (historicProcessInstance.getEndTime() == null) {
            flowVo.setCompleted(0);
        } else {
            flowVo.setCompleted(1);
            flowVo.setEndTime(historicProcessInstance.getEndTime());
        }
        List<HistoricTaskInstance> historicTaskInstances = queryUtil.listHistoricTaskInstances(wfiFlow.getId());
        List<WfiDeliverDO> wfiDelivers = wfiDeliverService.listByFlowId(wfiFlow.getId());
        List<SysUserDO> deliverUsers = listDeliverUsers(wfiDelivers);
        // 储存节点信息
        Map<String, NodeVO> map = new HashMap<>();
        Map<String, Object> wfiDeliverIds = queryUtil.listTaskVariableValues(historicTaskInstances.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet()), TaskVariableName.WFI_DELIVER_ID, false);
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            for (Node node : flow.getNodes()) {
                if (NodeType.Start.isEquals(node.getNodeType()) && !map.containsKey(node.getId())) {
                    NodeVO nodeVo = new NodeVO();
                    nodeVo.setId(node.getId());
                    nodeVo.setFlowId(flowVo.getId());
                    nodeVo.setNodeType(node.getNodeType());
                    nodeVo.setCode(node.getCode());
                    nodeVo.setName(node.getName());
                    nodeVo.setCompleted(1);
                    map.put(node.getId(), nodeVo);
                } else if (NodeType.End.isEquals(node.getNodeType()) && !map.containsKey(node.getId())) {
                    NodeVO nodeVo = new NodeVO();
                    nodeVo.setId(node.getId());
                    nodeVo.setFlowId(flowVo.getId());
                    nodeVo.setNodeType(node.getNodeType());
                    nodeVo.setCode(node.getCode());
                    nodeVo.setName(node.getName());
                    nodeVo.setCompleted(0);
                    map.put(node.getId(), nodeVo);
                }
                if (node.getId().equals(historicTaskInstance.getName())) {
                    NodeVO nodeVo = new NodeVO();
//                    if (map.containsKey(node.getId())) {
//                        nodeVo = map.get(node.getId());
//                    }
                    nodeVo.setId(node.getId());
                    nodeVo.setFlowId(flowVo.getId());
                    nodeVo.setNodeType(node.getNodeType());
                    nodeVo.setCode(node.getCode());
                    nodeVo.setName(node.getName());
                    LocalDateTime endTime = null;
                    // 添加转交信息
                    boolean hasDeliverInfo = false;
                    if (wfiDeliverIds.containsKey(historicTaskInstance.getId())) {
                        hasDeliverInfo = true;
                        String deliverInfoId = (String) wfiDeliverIds.get(historicTaskInstance.getId());
                        for (int i = 0; i < wfiDelivers.size(); i++) {
                            WfiDeliverDO wfiDeliver = wfiDelivers.get(i);
                            if (Objects.equals(wfiDeliver.getId(), deliverInfoId)) {
                                DeliverVO deliverVo = new DeliverVO();
                                deliverVo.setSn(i);
                                deliverVo.setNodeId(node.getId());
                                if (StringUtils.isNotEmpty(wfiDeliver.getDeliverInfo())) {
                                    DeliverInfo deliverInfo = JSONObject.parseObject(wfiDeliver.getDeliverInfo(),
                                            DeliverInfo.class);
                                    deliverVo.setDeliverInfo(deliverInfo);
                                    // 驳回到的节点ID
                                    if (OperationType.Reject.isEquals(deliverInfo.getOperationType())) {
                                        for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
                                            nodeVo.addRejectToNodeId(deliverNode.getNodeId());
                                        }
                                    }
                                }
                                List<SysUserDO> sysUsers = deliverUsers.stream().filter(o -> o.getId().equals
                                        (wfiDeliver.getUserId())).collect(Collectors.toList());
                                if (sysUsers.size()
                                        > 0) {
                                    deliverVo.setRealName(sysUsers.get(0).getRealName());
                                }
                                deliverVo.setEndTime(wfiDeliver.getDeliverTime());
                                if (endTime == null) {
                                    endTime = deliverVo.getEndTime();
                                } else if (ObjectUtils.compare(endTime, deliverVo.getEndTime()) < 0) {
                                    endTime = deliverVo.getEndTime();
                                }
                            }
                        }
                    }

                    // 若是驳回 则此任务节点算作未完成
                    if (nodeIsCompleted(node, historicTaskInstances)) {
                        nodeVo.setCompleted(IntegerUtils.TRUE_VALUE);
                        nodeVo.setEndTime(endTime);
                    } else {
                        nodeVo.setCompleted(IntegerUtils.FALSE_VALUE);
                    }
                    if (historicTaskInstance.getEndTime() != null && !hasDeliverInfo) {
                        continue;
                    }
                    map.put(nodeVo.getId(), nodeVo);
                }
            }
        }
        // 驳回后 之前的节点 都是未完成
        List<String> nodeLines = queryUtil.getProcessNodeLines(flow);
        for (String key : map.keySet()) {
            NodeVO nodeVo = map.get(key);
            for (String rejectToNodeId : nodeVo.getRejectToNodeIds()) {
                NodeVO lastNode = map.get(rejectToNodeId);
                if (nodeVo.getEndTime() == null) {
                    lastNode.setCompleted(0);
                }
                map.put(rejectToNodeId, lastNode);
                for (String line : nodeLines) {
                    if (line.contains(rejectToNodeId)) {
                        String nodesStr = null;
                        try {
                            nodesStr = line.split(rejectToNodeId + "->")[1]
                                    .split(key)[0] + key;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (StringUtils.isNotBlank(nodesStr)) {
                            String[] nodeIds = nodesStr.split("->");
                            for (String nodeIdT : nodeIds) {
                                NodeVO nodeVoT = map.get(nodeIdT);
                                if (nodeVoT != null) {
                                    if (nodeVoT.getEndTime() != null && lastNode.getEndTime() != null && ObjectUtils.compare(lastNode.getEndTime(),
                                            nodeVoT.getEndTime()) <= 0) {
                                    } else {
                                        nodeVoT.setCompleted(0);
                                    }
                                    map.put(nodeIdT, nodeVoT);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Node node : flow.getNodes()) {
            String nodeId = node.getId();
            // 结束节点
            if (NodeType.End.isEquals(node.getNodeType())) {
                for (Link link : node.getFlowInLinks()) {
                    String fromNodeId = link.getFromNodeId();
                    if (map.containsKey(fromNodeId)) {
                        NodeVO fromNodeVO = map.get(fromNodeId);
                        /**
                         * 结束节点对应的上级节点有多个 需要判断此时的 link 是否符合路由规则
                         */
                        securityUtils.getSession().setAttribute(SessionAttribute.WFD_FLOW_EL_BUSINESS_KEY, businessKey);
                        if (fromNodeVO.getCompleted() == 1 && ruleUtils.calculateRule(flow, link.getRuleExpression())) {
                            NodeVO nodeVo = map.get(nodeId);
                            nodeVo.setRelevant(1);
                            nodeVo.setCompleted(1);
                            map.put(nodeId, nodeVo);
                        }
                    }
                }
            } else if (map.containsKey(nodeId)) {
                NodeVO nodeVo = map.get(nodeId);
                if (nodeVo.getCompleted() == 1) {
                    // 处理流出分支
                    for (Link link : node.getFlowOutLinks()) {
                        LinkVO linkVO = new LinkVO();
                        linkVO.setId(link.getId());
                        String toNodeId = link.getToNodeId();
                        if (map.containsKey(toNodeId)) {
                            NodeVO toNodeVO = map.get(toNodeId);
                            // 处理流出节点是结束节点
                            if (NodeType.End.isEquals(toNodeVO.getNodeType())) {
                                securityUtils.getSession().setAttribute(SessionAttribute.WFD_FLOW_EL_BUSINESS_KEY, businessKey);
                                if (ruleUtils.calculateRule(flow, link.getRuleExpression())) {
                                    linkVO.setRelevant(1);
                                    nodeVo.addFlowOutLinks(linkVO);
                                }
                            } else if (NodeType.Start.isEquals(node.getNodeType()) ||
                                    (toNodeVO.getEndTime() == null || (toNodeVO.getEndTime() != null && ObjectUtils.compare(toNodeVO.getEndTime(), nodeVo.getEndTime()) >= 0))) {
                                if (toNodeVO.getCompleted() == 1 || NodeType.End.isEquals(toNodeVO.getNodeType())) {
                                    linkVO.setRelevant(1);
                                }
                                nodeVo.addFlowOutLinks(linkVO);
                            }
                        }
                    }
                    map.put(nodeId, nodeVo);
                }
            }
        }
        // 判断是否相关
        // 已完成 且 必须在当前任务的序列中

        // 获取最近的任务节点
        if (historicTaskInstances.size() > 0) {
            historicTaskInstances.sort((o1, o2) -> ObjectUtils.compare(o1.getStartTime(), o2.getStartTime()));
            HistoricTaskInstance latestTask = historicTaskInstances.get(historicTaskInstances.size() - 1);
            List<String> linesDesc = queryUtil.getProcessNodeLinesDesc(flow, latestTask.getName());
            for (String lineDesc : linesDesc) {
                String[] nodeIds = lineDesc.split("->");
                for (int i = nodeIds.length - 1; i >= 0; i--) {
                    String nodeId = nodeIds[i];
                    if (map.containsKey(nodeId) && map.get(nodeId).getCompleted() == 1) {
                        map.get(nodeId).setRelevant(1);
                    }
                }
            }
        }
        map.values().forEach(nodeVO -> {
            if (nodeVO.getCompleted() == 0 && nodeVO.getEndTime() == null && !NodeType.End.isEquals(nodeVO.getNodeType())) {
                nodeVO.setPending(1);
            }
            flowVo.addNode(nodeVO);
        });
        return flowVo;
    }

    private Boolean nodeIsCompleted(Node node, List<HistoricTaskInstance> historicTaskInstances) {
        if (NodeType.Start.isEquals(node.getNodeType())) {
            return true;
        } else if (NodeType.Manual.isEquals(node.getNodeType()) || NodeType.Free.isEquals(node.getNodeType())) {
            List<HistoricTaskInstance> taskInstances = historicTaskInstances.stream().filter(o -> StringUtils.equals(node.getId(), o.getName())).collect(Collectors.toList());
            // 如果当前节点没有办理步骤，则表示当前步骤未办结或无需办理
            if (taskInstances.size() > 0) {
                // 当前步骤正在办理的步骤是否已办结
                if (taskInstances.stream().anyMatch(o -> o.getEndTime() == null)) {
                    // 如果存在未办结
                    return false;
                } else {
                    // 如果全部办结
                    return true;
                }
            } else {
                return false;
            }
        } else if (NodeType.Subflow.isEquals(node.getNodeType())) {
            // TODO: 2021/2/8 暂不支持
            return true;
        } else if (NodeType.End.isEquals(node.getNodeType())) {
            // 如果是结束节点，则需要判断转入节点是否选择了当前结束节点
        }
        return false;
    }

    @Override
    public FlowVO getProcessFlowByTaskId(String taskId) {
        TaskInstance taskInstance = getTaskInstanceById(taskId);
        // 获取流程实例
        return getProcessFlowByBusinessKey(taskInstance.getFlowCode(), taskInstance.getFlowBusinessKey());
    }

    @Override
    public FlowVO getProcessFlowByBusinessKey(String flowCode, String businessKey) {
        WfiFlowDO wfiFlow = getWfiFlowByBusinessKey(flowCode, businessKey);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        FlowVO flowVo = new FlowVO();
        flowVo.setId(flow.getId());
        flowVo.setFlowInstanceId(wfiFlow.getId());
        flowVo.setName(flow.getName());
        flowVo.setCode(flow.getCode());
        HistoricProcessInstance historicProcessInstance = queryUtil.getHistoricProcessInstance(wfiFlow.getId());
        if (historicProcessInstance.getEndTime() == null) {
            flowVo.setCompleted(IntegerUtils.FALSE_VALUE);
        } else {
            flowVo.setCompleted(IntegerUtils.TRUE_VALUE);
            flowVo.setEndTime(historicProcessInstance.getEndTime());
            flowVo.setResult((String) historicProcessInstance.getProcessVariables().get(ProcessVariableName.Result));
        }
        for (Node node : flow.getNodes()) {
            com.csicit.ace.bpm.pojo.vo.process.NodeVO nodeVo = new com.csicit.ace.bpm.pojo.vo.process.NodeVO();
            nodeVo.setId(node.getId());
            nodeVo.setFlowId(flow.getId());
            nodeVo.setName(node.getName());
            nodeVo.setCode(node.getCode());
            nodeVo.setNodeType(node.getNodeType());
            flowVo.addNode(nodeVo);
        }
        return flowVo;
    }

    private void resortHistoricTaskInstance(WfiFlowDO wfiFlow, Flow flow, List<HistoricTaskInstance> historicTaskInstances) {
        // 获取所有转交信息
        List<WfiDeliverDO> wfiDelivers = wfiDeliverService.listByFlowId(wfiFlow.getId());
        Map<String, Object> wfiDeliverIdFroms = queryUtil.listTaskVariableValues(historicTaskInstances.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet()), TaskVariableName.WFI_DELIVER_ID_FROM);
        historicTaskInstances.sort(((o1, o2) ->
        {
            if (o1.getEndTime() != null) {
                if (o2.getEndTime() != null) {
                    return o1.getEndTime().compareTo(o2.getEndTime());
                }
            } else {
                if (o2.getEndTime() != null) {
                    return 1;
                } else {
                    // 判断是否是一个节点
                    if (StringUtils.equals(o1.getName(), o2.getName())) {
                        // 判断是否来自同一次转交
                        String wfiDeliverIdFrom1 = (String) wfiDeliverIdFroms.get(o1.getId());
                        String wfiDeliverIdFrom2 = (String) wfiDeliverIdFroms.get(o2.getId());
                        if (StringUtils.equals(wfiDeliverIdFrom1, wfiDeliverIdFrom2)) {
                            // 如果是同一次转交生成的，判断是否有办理先后
                            WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverById(wfiDelivers, wfiDeliverIdFrom1);
                            DeliverInfo deliverInfo = wfiDeliver.getDeliverInfoClass();
                            DeliverNode deliverNode = deliverInfo.getDeliverNodeByNodeId(o1.getName());
                            if (deliverNode.getDeliverUsers().size() > 1) {
                                // 如果办理人有多个，则判断是否强制办理顺序
                                Node node = flow.getNodeById(o1.getName());
                                if (IntegerUtils.isTrue(node.getForceSequence())) {
                                    DeliverUser deliverUser1 = deliverNode.getDeliverUserByUserId(o1.getAssignee());
                                    DeliverUser deliverUser2 = deliverNode.getDeliverUserByUserId(o2.getAssignee());
                                    return ObjectUtils.compare(deliverUser1.getSortIndex(), deliverUser2.getSortIndex());
                                }
                            }
                        }
                    }
                }
            }
            return -1;
        }));
    }

    /**
     * 转化TaskVO
     *
     * @param wfiFlow 流程实例
     * @param list    历史任务列表
     * @return
     * @author FourLeaves
     * @date 2019/12/4 15:58
     */
    public List<TaskVO> formTaskInsToVO(WfiFlowDO wfiFlow, List<HistoricTaskInstance> list) {
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        resortHistoricTaskInstance(wfiFlow, flow, list);
        List<TaskVO> taskVOS = new ArrayList<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            // 获取审批人列表
            Set<String> assignees = list.stream().map(HistoricTaskInstance::getAssignee).collect(Collectors.toSet());
            List<WfdDelegateWorkDO> delegateWorkDOS = wfdDelegateWorkService.list(
                    new QueryWrapper<WfdDelegateWorkDO>().eq("PROCESS_ID", list.get(0).getProcessInstanceId()));
            if (CollectionUtils.isNotEmpty(delegateWorkDOS)) {
                assignees.addAll(delegateWorkDOS.stream().map(WfdDelegateWorkDO::getUserId).collect(Collectors.toSet()));
            }
            List<SysUserDO> users = iUser.getUsersByIds(new ArrayList<>(assignees));
            Map<String, SysUserDO> idAndUsers = new HashMap<>();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(users)) {
                users.stream().forEach(user -> {
                    idAndUsers.put(user.getId(), user);
                });
            }
            Map<String, Object> wfiDeliverIds = queryUtil.listTaskVariableValues(list.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet()), TaskVariableName.WFI_DELIVER_ID, false);
            for (int i = 0; i < list.size(); i++) {
                HistoricTaskInstance taskInstance = list.get(i);
                TaskVO taskVO = new TaskVO();
                taskVO.setId(taskInstance.getId());
                taskVO.setEndTime(taskInstance.getEndTime());
                taskVO.setClaimTime(taskInstance.getClaimTime());
                taskVO.setStartTime(taskInstance.getStartTime());

                Node node = flow.getNodeById(taskInstance.getName());
//                taskVO.setNodeName("[" + node.getCode() + "]" + node.getName());
                taskVO.setNodeName(node.getName());

                SysUserDO user = idAndUsers.get(taskInstance.getAssignee());
                taskVO.setHostUserId(taskInstance.getAssignee());
                taskVO.setHostRealName(user == null ? null : user.getRealName());

                WfdDelegateWorkDO work = delegateWorkDOS.stream().filter(workT -> Objects.equals(taskInstance.getId(), workT.getTaskId())).findFirst().orElse(null);
                if (work != null) {
                    // 委托人
                    if (taskInstance.getEndTime() == null) {
                        SysUserDO owner = idAndUsers.get(work.getUserId());
                        if (owner != null) {
                            taskVO.setOwnerId(work.getUserId());
                            taskVO.setOwnerRealName(owner.getRealName());
                        }
                    }
                    // 被委托人
                    else {
                        taskVO.setDelegatedUserId(work.getDelegateUserId());
                        taskVO.setDelegatedRealName(work.getDelegateRealName());
                    }
                }

                // 转交人
                if (wfiDeliverIds.containsKey(taskInstance.getId())) {
                    String deliverInfoId = (String) wfiDeliverIds.get(taskInstance.getId());
                    if (StringUtils.isNotBlank(deliverInfoId)) {
                        WfiDeliverDO wfiDeliverDO = wfiDeliverService.getById(deliverInfoId);
                        if (wfiDeliverDO != null) {
                            DeliverVO deliverVo = new DeliverVO();
                            DeliverInfo deliverInfo = JsonUtils.castObject(wfiDeliverDO.getDeliverInfo(), DeliverInfo.class);
                            if (!OperationType.StopAfterReject.isEquals(deliverInfo.getOperationType())) {
                                deliverVo.setDeliverInfo(deliverInfo);
                                taskVO.setDeliver(deliverVo);
                                List<DeliverNode> deliverNodes = deliverInfo.getDeliverNodes();
                                StringBuffer nodeNames = new StringBuffer();
                                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(deliverNodes)) {
                                    deliverNodes.stream().forEach(deliverNode -> {
                                        String nodeId = deliverNode.getNodeId();
                                        Node nodeT = flow.getNodeById(nodeId);
                                        if (nodeT != null) {
                                            nodeNames.append("," + nodeT.getName());
                                        }
                                    });
                                }
                                String nodeNamesStr = nodeNames.toString();
                                if (nodeNamesStr.length() > 0) {
                                    taskVO.setDeliverNodeNames(nodeNamesStr.substring(1));
                                }
                            }
                        }
                    }
                }
                taskVOS.add(taskVO);
            }
        }
        int index = 1;
        List<String> taskIds = taskVOS.parallelStream().map(TaskVO::getId).collect(Collectors.toList());
        List<WfiUserTaskStateDO> wfiUserTaskStateDOs = wfiUserTaskStateService.list(new QueryWrapper<WfiUserTaskStateDO>()
                .in("task_id", taskIds));
        Map<String, String> userIdTaskIdAndState = new HashMap<>();
        wfiUserTaskStateDOs.forEach(wfiUserTaskStateDO -> {
            userIdTaskIdAndState.put(wfiUserTaskStateDO.getUserId() + "-" + wfiUserTaskStateDO.getTaskId(), wfiUserTaskStateDO.getState());
        });
        for (int i = 0; i < taskVOS.size(); i++) {
            TaskVO taskVO = taskVOS.get(i);
            if (i > 0) {
                String lastNodeName = taskVOS.get(i - 1).getNodeName();
                if (!Objects.equals(taskVOS.get(i).getNodeName(), lastNodeName.substring(lastNodeName.indexOf("-") + 1))) {
                    index++;
                }
            }
            taskVO.setNodeName("#" + index + "-" + taskVO.getNodeName());
            taskVO.setState(userIdTaskIdAndState.get(taskVO.getHostUserId() + "-" + taskVO.getId()));
            taskVOS.set(i, taskVO);
        }
        return taskVOS;
    }


    @Override
    public Long getAllProcessTasksCountByBusinessKey(String flowCode, String businessKey) {
        WfiFlowDO wfiFlowDO = wfiFlowService.getByCode(flowCode, businessKey);
        if (wfiFlowDO == null) {
            return 0L;
        }
        return queryUtil.getHistoricTaskInstanceQuery(wfiFlowDO.getId()).count();
    }

    @Override
    public Long getProcessTasksCountByTaskId(String taskId, String nodeId) {
        HistoricTaskInstance task = queryUtil.getHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (task == null) {
            return 0L;
        }
        return queryUtil.getHistoricTaskInstanceQuery(task.getProcessInstanceId()).taskName(nodeId).count();
    }

    @Override
    public Long getAllProcessTasksCountByTaskId(String taskId) {
        HistoricTaskInstance task = queryUtil.getHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (task == null) {
            return 0L;
        }
        return queryUtil.getHistoricTaskInstanceQuery(task.getProcessInstanceId()).count();
    }

    @Override
    public Long getProcessTasksCountByBusinessKey(String flowCode, String businessKey, String nodeId) {
        WfiFlowDO wfiFlowDO = wfiFlowService.getByCode(flowCode, businessKey);
        if (wfiFlowDO == null) {
            return 0L;
        }
        return queryUtil.getHistoricTaskInstanceQuery(wfiFlowDO.getId()).taskName(nodeId).count();
    }

    @Override
    public List<TaskVO> listAllProcessTasksByBusinessKey(Integer current, Integer size, String flowCode, String
            businessKey) {
        WfiFlowDO wfiFlowDO = wfiFlowService.getByCode(flowCode, businessKey);
        if (wfiFlowDO != null) {
            int firstResult = size * (current - 1);
            int maxResults = size;
            return formTaskInsToVO(wfiFlowDO, queryUtil.getHistoricTaskInstanceQuery(wfiFlowDO.getId()).orderByHistoricTaskInstanceStartTime().asc().listPage(firstResult, maxResults));
        }
        return new ArrayList<>();
    }


    @Override
    public List<TaskVO> listAllProcessTasksByTaskId(Integer current, Integer size, String taskId) {
        HistoricTaskInstance task = queryUtil.getHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (task != null) {
            int firstResult = size * (current - 1);
            int maxResults = size;
            WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(task.getProcessInstanceId());
            return formTaskInsToVO(wfiFlow, queryUtil.getHistoricTaskInstanceQuery(task.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().listPage(firstResult, maxResults));
        }
        return new ArrayList<>();
    }

    @Override
    public List<TaskVO> listProcessTasksByBusinessKey(Integer current, Integer size, String flowCode, String
            businessKey, String nodeId) {
        WfiFlowDO wfiFlowDO = wfiFlowService.getByCode(flowCode, businessKey);
        if (wfiFlowDO != null) {
            int firstResult = current * (current - 1);
            int maxResults = size;
            return formTaskInsToVO(wfiFlowDO, queryUtil.getHistoricTaskInstanceQuery(wfiFlowDO.getId()).taskName(nodeId).orderByHistoricTaskInstanceStartTime().asc().listPage(firstResult, maxResults));
        }
        return new ArrayList<>();
    }

    @Override
    public List<TaskVO> listProcessTasksByTaskId(Integer current, Integer size, String taskId, String nodeId) {
        HistoricTaskInstance task = queryUtil.getHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (task != null) {
            int firstResult = size * (current - 1);
            int maxResults = size;
            WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(task.getProcessInstanceId());
            return formTaskInsToVO(wfiFlow, queryUtil.getHistoricTaskInstanceQuery(task.getProcessInstanceId()).taskName(nodeId)
                    .orderByHistoricTaskInstanceStartTime().asc().listPage(firstResult, maxResults));
        }
        return new ArrayList<>();
    }

    public List<NextStep> listNextStepsByTaskId(String taskId) {
        return listNextStepsByTaskId(taskId, null);
    }

    public List<NextStep> listNextStepsByTaskId(String taskId, List<String> workResults) {
        TaskInstance taskInstance = getTaskInstanceById(taskId);
        String deliverInfoIdFrom = (String) queryUtil.getTaskVariable(taskId, TaskVariableName.WFI_DELIVER_ID_FROM);
        return listNextStepsByNodeId(taskInstance.getFlowCode(), taskInstance.getFlowBusinessKey(), taskInstance.getNodeId(), workResults, deliverInfoIdFrom);
    }

    public List<NextStep> listNextStepsByNodeId(String flowCode, String businessKey, String nodeId) {
        return listNextStepsByNodeId(flowCode, businessKey, nodeId, null, null);
    }

    public List<NextStep> listNextStepsByNodeId(String flowCode, String businessKey, String nodeId, List<String> workResults, String deliverInfoIdFrom) {
        WfiFlowDO wfiFlow = getWfiFlowByBusinessKey(flowCode, businessKey);
        return listNextSteps(wfiFlow, nodeId, workResults, deliverInfoIdFrom);
    }

    @Override
    public List<NextStep> listNextSteps(WfiFlowDO wfiFlow, String nodeId, List<String> workResults, String deliverInfoIdFrom) {
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(nodeId);
        return listNextSteps(wfiFlow, flow, node, workResults, deliverInfoIdFrom);
    }

    @Override
    public List<OrgDepartmentDO> getDepartmentTree(String flowId,String nodeId){
        WfiFlowDO wfiFlow = getWfiFlowById(flowId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(nodeId);
        Integer secretLevel = FlowUtils.getSecretLevel(flow, wfdFlowElService);
        List<SysUserDO> sysUsers = wfdCollectionUtils.getUsers(node, secretLevel);
        if(sysUsers.size() > SIZE_SORT_BY_PINYIN)
        {
            sysUsers.sort(((o1, o2) -> StringUtils.compare(o1.getPinyin(), o2.getPinyin())));
        }
        Set<String> getPersonDocId = sysUsers.stream().map(SysUserDO::getPersonDocId).collect(Collectors.toSet());
        System.out.println(getPersonDocId);
        List<OrgDepartmentDO> orgDepartmentDOS = iorg.getOrgDepartmentListByUserIds(getPersonDocId);
        List<OrgDepartmentDO> departmentTree = TreeUtils.makeTree(orgDepartmentDOS,OrgDepartmentDO.class);
        return departmentTree;
    }

    @Override
    public List<OrgDepartmentDO> listdeliverDepartment(String flowId,String nodeId,String taskId){
        TaskInstance task = bpmAdapter.getTaskInstanceById(taskId);
        task.getFlowInstanceId();
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(task.getFlowInstanceId());
        String wfiDeliverIdFrom = (String) bpmAdapter.getTaskVariable(taskId, TaskVariableName.WFI_DELIVER_ID_FROM);
        List<String> workResults = new ArrayList<>();
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(task.getNodeId());
        queryUtil.checkPresetRoute(node, taskId);
        List<NextStep> nextSteps = bpmAdapter.listNextSteps(wfiFlow, task.getNodeId(), workResults, wfiDeliverIdFrom);
        List<StepUser> sysUserDOS1 = new ArrayList<>();
        Set<String> userIds = new HashSet<>();
        for(NextStep nextStep:nextSteps){
            if(nextStep.getNodeId().equals(nodeId)){
                sysUserDOS1 = nextStep.getNextStepUsers();
            }
        }
        for(StepUser stepUser:sysUserDOS1){
            userIds.add(stepUser.getUserId());
        }
        List<OrgDepartmentDO> orgDepartmentDOS = iorg.listdeliverDepartment(userIds);
        List<OrgDepartmentDO> departmentTree = TreeUtils.makeTree(orgDepartmentDOS,OrgDepartmentDO.class);
        return departmentTree;
    }

    @Override
    public List<SysUserDO> getUsersByDepartmentId(String userIds){
        List<SysUserDO> sysUserDOS = iorg.getUsersByDepartmentId(userIds);
        return sysUserDOS;
    };

    @Override
    public List<SysUserDO> getUsersByUserIds(List<String> userIds){
        List<SysUserDO> sysUserDOS = iorg.getUsersByUserIds(userIds);
        return sysUserDOS;
    }


    @Override
    public List<NextStep> listNextSteps(WfiFlowDO wfiFlow, Flow flow, Node node, List<String> workResults, String deliverInfoIdFrom) {
        List<DeliverInfo> deliverInfos = null;
        String nodeFreeStepId = null;
        // 获取当前节点已办结的任务所选择的工作结果
        if (StringUtils.isNotEmpty(deliverInfoIdFrom)) {
            // 当前节点已办结的任务
            List<HistoricTaskInstance> historicTaskInstances = queryUtil.getHistoricTaskInstanceQuery(wfiFlow.getId()).taskName(node.getId()).finished().list();
            // 生成这些已办结任务的转交信息id
            Map<String, Object> taskVariables = queryUtil.listTaskVariableValues(historicTaskInstances.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet()), TaskVariableName.WFI_DELIVER_ID_FROM, false);
            // 与当前任务一同生成的相同节点任务id
            Set<String> taskIds = new HashSet<>();
            taskVariables.forEach((k, v) -> {
                if (deliverInfoIdFrom.equals(v)) {
                    taskIds.add(k);
                }
            });
            // 与当前任务一同生成的相同节点且已办结任务的转交信息id
            Map<String, Object> deliverInfoIds = queryUtil.listTaskVariableValues(taskIds, TaskVariableName.WFI_DELIVER_ID, false);
            // 与当前任务一同生成的相同节点且已办结任务的转交信息
            Collection<WfiDeliverDO> wfiDelivers = wfiDeliverService.listByIds(deliverInfoIds.values().stream().map(o -> (String) o).collect(Collectors.toList()));
            deliverInfos = wfiDelivers.stream().map(o -> FlowUtils.getDeliverInfo(o)).collect(Collectors.toList());
            DeliverInfo deliverInfoFrom = queryUtil.getDeliverInfo(deliverInfoIdFrom);
            DeliverNode deliverNode = deliverInfoFrom.getDeliverNodeByNodeId(node.getId());
            nodeFreeStepId = deliverNode.getNodeFreeStepId();
        }
        return FlowUtils.listNextSteps(securityUtils.getSession(), wfiFlow.getId(), wfiFlow.getBusinessKey(), node, workResults, deliverInfos, nodeFreeStepId);
    }

    @Override
    public FlowInstance getFlowInstance(TaskInstance task) {
        return getFlowInstance(task.getFlowInstanceId());
    }

    @Override
    public FlowInstance getFlowInstance(String flowInstanceId) {
        ProcessInstance processInstance = queryUtil.getProcessInstance(flowInstanceId);
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = queryUtil.getHistoricProcessInstance(flowInstanceId);
            if (historicProcessInstance == null) {
                throw new FlowInstanceNotFoundException(flowInstanceId);
            } else {
                return new com.csicit.ace.bpm.activiti.impl.historic.FlowInstanceImpl(historicProcessInstance);
            }
        } else {
            return new FlowInstanceImpl(processInstance);
        }
    }

    @Override
    public FlowInstance getFlowInstanceByBusinessKey(String code, String businessKey) {
        WfiFlowDO wfiFlow = getWfiFlowByBusinessKey(code, businessKey);
        return getFlowInstance(wfiFlow.getId());
    }

    @Override
    public Boolean hasAdminAuth(Flow flow, SysUserDO currentUser) {
        return queryUtil.hasAdminAuth(flow.getId(), currentUser.getId());
    }

    @Override
    public Boolean hasQueryAuth(Flow flow, SysUserDO currentUser) {
        return hasQueryAuth(flow.getId(), currentUser);
    }

    @Override
    public Boolean hasQueryAuth(String flowId, SysUserDO currentUser) {
        return queryUtil.hasQueryAuth(flowId, currentUser.getId());
    }

    @Override
    public Boolean hasInitAuth(Flow flow, SysUserDO currentUser) {
        return hasInitAuth(currentUser.getId(), flow.getInitAuthId());
    }

    @Override
    public Boolean hasInitAuth(String userId, String initAuthId) {
        return iSecurity.hasAuthorityWithUserId(userId, initAuthId);
    }


    @Override
    public List<FlowInstance> listFlowInstancesByBusinessKey(String code, String businessKey) {
        List<HistoricProcessInstance> historicProcessInstances = queryUtil.getHistoricProcessInstanceQuery()
                .processDefinitionKey(code).processInstanceBusinessKey(businessKey).list();
        return castToFlowInstance(historicProcessInstances);
    }

    private List<FlowInstance> castToFlowInstance(List<HistoricProcessInstance> historicProcessInstances) {
        List<FlowInstance> l = new ArrayList<>();
        for (HistoricProcessInstance historicProcessInstance :
                historicProcessInstances) {
            l.add(new com.csicit.ace.bpm.activiti.impl.historic.FlowInstanceImpl(historicProcessInstance));
        }
        return l;
    }

    @Override
    public Boolean flowInstanceExists(String code, String businessKey) {
        return wfiFlowService.count(new QueryWrapper<WfiFlowDO>().eq("FLOW_CODE", code).eq("BUSINESS_KEY",
                businessKey).eq("APP_ID", securityUtils.getAppName())) > 0;
    }

    @Override
    public List<TaskInstance> listTasksByFlowInstanceId(String flowInstanceId) {
        List<Task> tasks = queryUtil.getTaskQuery(flowInstanceId).list();
        return com.csicit.ace.bpm.activiti.impl.TaskInstanceImpl.castToBpmTask(tasks);
    }

    @Override
    public TaskInstance getTaskInstanceById(String id) {
        Task task = queryUtil.getTaskQuery().taskId(id).singleResult();
        if (task == null) {
            HistoricTaskInstance historicTaskInstance = queryUtil.getHistoricTaskInstanceQuery().taskId(id).singleResult();
            if (historicTaskInstance == null) {
                throw new TaskNotFoundByIdException(id);
            } else {
                String wfiId = historicTaskInstance.getProcessInstanceId();
                WfiFlowDO wfiFlowDO = wfiFlowService.getById(wfiId);
                return new com.csicit.ace.bpm.activiti.impl.historic.TaskInstanceImpl(historicTaskInstance, wfiFlowDO);
            }
        } else {
            String wfiId = task.getProcessInstanceId();
            WfiFlowDO wfiFlowDO = wfiFlowService.getById(wfiId);
            return new TaskInstanceImpl(task, wfiFlowDO);
        }
    }

    /**
     * 增加流程实例信息
     *
     * @param taskInfo
     * @param wfiFlow
     * @return com.csicit.ace.bpm.pojo.vo.TaskMineVO
     * @author JonnyJiang
     * @date 2019/11/7 16:13
     */

    private TaskMineVO convertToTaskMine(TaskInfo taskInfo, WfiFlowDO wfiFlow) {
        TaskMineVO taskMine = new TaskMineVO();
        taskMine.setId(taskInfo.getId());
        if (wfiFlow != null) {
            taskMine.setWorkNo(wfiFlow.getFlowNo());
            Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
            Node node = flow.getNodeById(taskInfo.getName());
            taskMine.setNodeName(node.getName());
            taskMine.setBusinessKey(wfiFlow.getBusinessKey());
            taskMine.setAppId(wfiFlow.getAppId());
        }
        taskMine.setFlowInstanceId(taskInfo.getProcessInstanceId());
        return taskMine;
    }

    private TaskMineVO convertToTaskMine(WfiTaskPendingDO taskInfo, WfiFlowDO wfiFlow) {
        TaskMineVO taskMine = new TaskMineVO();
        taskMine.setId(taskInfo.getTaskId());
        if (wfiFlow != null) {
            taskMine.setWorkNo(wfiFlow.getFlowNo());
            Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
            Node node = flow.getNodeById(taskInfo.getNodeId());
            taskMine.setNodeName(node.getName());
            taskMine.setBusinessKey(wfiFlow.getBusinessKey());
            taskMine.setAppId(wfiFlow.getAppId());
        }
        taskMine.setFlowInstanceId(taskInfo.getFlowId());
        return taskMine;
    }

    private WfiFlowDO getWfiFlowById(Collection<WfiFlowDO> wfiFlows, String id) {
        for (WfiFlowDO wfiFlow : wfiFlows) {
            if (wfiFlow.getId().equals(id)) {
                return wfiFlow;
            }
        }
        return null;
    }

    @Override
    public List<TaskMineVO> listTaskMineByFlowCode(String flowCode, Integer current, Integer size, Integer completed) {
        List<TaskMineVO> taskMines = new ArrayList<>();
        Integer firstResult = ((current - 1) * size);
        Integer maxResults = size;

        if (IntegerUtils.isTrue(completed)) {
            List<HistoricTaskInstance> historicTaskInstances = queryUtil.getListTaskMineCompletedByFlowCodeQuery(flowCode).listPage
                    (firstResult, maxResults);
            Collection<WfiFlowDO> wfiFlows = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().in("id",
                    historicTaskInstances.stream()
                            .map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet()))
                    .select("id", "model", "flow_no", "flow_id", "business_key"));
            // fillWfiFlowModels(wfiFlows);
            for (HistoricTaskInstance task : historicTaskInstances) {
                WfiFlowDO wfiFlow = getWfiFlowById(wfiFlows, task.getProcessInstanceId());
                TaskMineVO taskMineVO = null;
                try {
                    taskMineVO = convertToTaskMine(task, wfiFlow);
                } catch (NodeNotFoundByIdException e) {
                    wfiFlow = wfiFlowService.getById(wfiFlow.getId());
                    taskMineVO = convertToTaskMine(task, wfiFlow);
                }
                taskMineVO.setFlowEndTime(task.getEndTime());
                taskMines.add(taskMineVO);
            }
        } else {
            Page<WfiTaskPendingDO> page = new Page<>(current, size);
            QueryWrapper<WfiTaskPendingDO> queryWrapper = queryUtil.getListTaskMineQueryByFlowCode(flowCode);
            List<WfiTaskPendingDO> wfiTaskPendings = wfiTaskPendingService.page(page, queryWrapper).getRecords();

            resolveTaskMinePending(taskMines, wfiTaskPendings);
        }
        LOGGER.debug("count: " + taskMines.size());
        combineFlowInfo(taskMines);
        return taskMines;
    }

    /**
     * 填充工作流实例model
     *
     * @param wfiFlows
     * @return
     * @author FourLeaves
     * @date 2020/12/22 8:37
     */
    public Collection<WfiFlowDO> fillWfiFlowModels(Collection<WfiFlowDO> wfiFlows) {
        List<WfdFlowDO> flowDOS = wfdFlowService.list(new QueryWrapper<WfdFlowDO>()
                .in("id", wfiFlows.stream().map(WfiFlowDO::getFlowId).collect(Collectors.toList())));
        if (CollectionUtils.isNotEmpty(flowDOS)) {
            Map<String, String> idAndModels = new HashMap<>();
            flowDOS.forEach(wfdFlowDO -> {
                idAndModels.put(wfdFlowDO.getId(), wfdFlowDO.getModel());
            });
            wfiFlows.stream().forEach(wfiFlowDO -> {
                wfiFlowDO.setModel(idAndModels.get(wfiFlowDO.getFlowId()));
            });
        }
        return wfiFlows;
    }

    @Override
    public List<TaskMineVO> listTaskMine(Integer current, Integer size, Integer completed) {
        return listTaskMineByFlowCode(QueryUtil.FILTER_ALL, current, size, completed);
    }

    @Override
    public List<TaskMineVO> listTaskMineByParams(Map<String, String> params) {
        int current = Integer.parseInt(params.get("current"));
        int size = Integer.parseInt(params.get("size"));
        Integer firstResult = ((current - 1) * size);
        Integer maxResults = size;

        int completed = Integer.parseInt(params.get("completed"));

        List<TaskMineVO> taskMines = new ArrayList<>();

        /**
         * 是否已办结
         */
        if (IntegerUtils.isTrue(completed)) {
            HistoricTaskInstanceQuery query = queryUtil.getListTaskMineByParamsQuery(params);
            if (query == null) {
                return new ArrayList<>();
            }
            List<HistoricTaskInstance> historicTaskInstances = query.listPage
                    (firstResult, maxResults);
            Collection<WfiFlowDO> wfiFlows = wfiFlowService
                    .list(new QueryWrapper<WfiFlowDO>().in("id",
                            historicTaskInstances.stream()
                                    .map(HistoricTaskInstance::getProcessInstanceId)
                                    .collect(Collectors.toSet()))
                            .select("id", "model", "flow_no", "flow_id", "business_key","app_id"));
            WfiFlowDO wfiFlow;
            // fillWfiFlowModels(wfiFlows);
            for (HistoricTaskInstance task : historicTaskInstances) {
                wfiFlow = getWfiFlowById(wfiFlows, task.getProcessInstanceId());
                TaskMineVO taskMineVO = null;
                try {
                    taskMineVO = convertToTaskMine(task, wfiFlow);
                } catch (NodeNotFoundByIdException e) {
                    wfiFlow = wfiFlowService.getById(wfiFlow.getId());
                    taskMineVO = convertToTaskMine(task, wfiFlow);
                }
                taskMineVO.setFlowEndTime(task.getEndTime());
                taskMines.add(taskMineVO);
            }
        } else {
            QueryWrapper<WfiTaskPendingDO> query = queryUtil.getWfiTaskPendingQueryWrapperByParams(params);
            if (query == null) {
                return new ArrayList<>();
            }
            Page<WfiTaskPendingDO> page = new Page<>(current, size);
            List<WfiTaskPendingDO> tasks = wfiTaskPendingService.page(page, query).getRecords();
            Collection<WfiFlowDO> wfiFlows = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().in("id",
                    tasks.stream().map(WfiTaskPendingDO::getFlowId).collect(Collectors.toSet()))
                    .select("id", "model", "flow_no", "flow_id", "business_key","app_id"));
            WfiFlowDO wfiFlow;
            // fillWfiFlowModels(wfiFlows);
            for (WfiTaskPendingDO task : tasks) {
                wfiFlow = getWfiFlowById(wfiFlows, task.getFlowId());
                TaskMineVO taskMineVO = null;
                try {
                    taskMineVO = convertToTaskMine(task, wfiFlow);
                } catch (NodeNotFoundByIdException e) {
                    wfiFlow = wfiFlowService.getById(wfiFlow.getId());
                    taskMineVO = convertToTaskMine(task, wfiFlow);
                }
                taskMineVO.setTaskStartTime(task.getCreateTime());

                taskMines.add(taskMineVO);
            }
        }
        combineFlowInfo(taskMines);
        return taskMines;
    }


    @Override
    public long getTaskMineCountByParams(Map<String, String> params) {
        int completed = Integer.parseInt(params.get("completed"));
        /**
         * 是否已办结
         */
        if (IntegerUtils.isTrue(completed)) {
            HistoricTaskInstanceQuery query = queryUtil.getListTaskMineByParamsQuery(params);
            if (query != null) {
                return query.count();
            }
        } else {
            QueryWrapper<WfiTaskPendingDO> query = queryUtil.getWfiTaskPendingQueryWrapperByParams(params);
            if (query != null) {
                return wfiTaskPendingService.count(query);
            }
        }
        return 0;
    }

    @Override
    public List<TaskMineVO> listTaskMineByFlowCode(String flowCode, Integer completed) {
        List<TaskMineVO> taskMines = new ArrayList<>();
        if (IntegerUtils.isTrue(completed)) {
            List<HistoricTaskInstance> historicTaskInstances = queryUtil.getListTaskMineCompletedByFlowCodeQuery(flowCode).list();
            Collection<WfiFlowDO> wfiFlows = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().in("id",
                    historicTaskInstances.stream().map(HistoricTaskInstance::getProcessInstanceId).collect
                            (Collectors
                                    .toSet())).select("id", "model", "flow_no", "flow_id", "business_key"));
            WfiFlowDO wfiFlow;
            // fillWfiFlowModels(wfiFlows);
            for (HistoricTaskInstance task : historicTaskInstances) {
                wfiFlow = getWfiFlowById(wfiFlows, task.getProcessInstanceId());
                TaskMineVO taskMineVO = null;
                try {
                    taskMineVO = convertToTaskMine(task, wfiFlow);
                } catch (NodeNotFoundByIdException e) {
                    wfiFlow = wfiFlowService.getById(wfiFlow.getId());
                    taskMineVO = convertToTaskMine(task, wfiFlow);
                }
                taskMines.add(taskMineVO);
            }
        } else {
            QueryWrapper<WfiTaskPendingDO> queryWrapper = queryUtil.getListTaskMineQueryByFlowCode(flowCode);
            List<WfiTaskPendingDO> wfiTaskPendings = wfiTaskPendingService.list(queryWrapper);

            resolveTaskMinePending(taskMines, wfiTaskPendings);
        }
        LOGGER.debug("count: " + taskMines.size());
        combineFlowInfo(taskMines);
        return taskMines;
    }

    private void resolveTaskMinePending(List<TaskMineVO> taskMines, List<WfiTaskPendingDO> wfiTaskPendings) {
        Collection<WfiFlowDO> wfiFlows = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().in("id",
                wfiTaskPendings.stream().map(WfiTaskPendingDO::getFlowId).collect(Collectors.toSet()))
                .select("id", "model", "flow_no", "flow_id", "business_key"));
        // fillWfiFlowModels(wfiFlows);
        for (WfiTaskPendingDO task : wfiTaskPendings) {
            WfiFlowDO wfiFlow = getWfiFlowById(wfiFlows, task.getFlowId());
            TaskMineVO taskMineVO = null;
            try {
                taskMineVO = convertToTaskMine(task, wfiFlow);
            } catch (NodeNotFoundByIdException e) {
                wfiFlow = wfiFlowService.getById(wfiFlow.getId());
                taskMineVO = convertToTaskMine(task, wfiFlow);
            }
            taskMineVO.setTaskStartTime(task.getCreateTime());
            taskMines.add(taskMineVO);
        }
    }

    @Override
    public List<TaskMineVO> listTaskMine(Integer completed) {
        return listTaskMineByFlowCode(QueryUtil.FILTER_ALL, completed);
    }

    @Override
    public Long getTaskMineTotalByFlowCode(String flowCode, Integer completed) {
        Long total;
        if (IntegerUtils.isTrue(completed)) {
            total = queryUtil.getListTaskMineCompletedByFlowCodeQuery(flowCode).count();
        } else {
            total = Long.valueOf(wfiTaskPendingService.count(queryUtil.getListTaskMineQueryByFlowCode(flowCode)));
        }
        return total;
    }

    @Override
    public Long getTaskMineTotal(Integer completed) {
        return getTaskMineTotalByFlowCode(QueryUtil.FILTER_ALL, completed);
    }

    @Override
    public R listTaskMonitor(Integer current, Integer size, String flowId, int completed, String searchStr) {
        List<String> queryAuthIds = iSecurity.getAllAuthIds();
        Set<String> wfdVIds = wfdVFlowService.list(new QueryWrapper<WfdVFlowDO>()
                .eq("flow_id", flowId).in("ADMIN_AUTH_ID", queryAuthIds).select("id")
        ).stream().map(WfdVFlowDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(wfdVIds)) {
            List<WfiFlowDO> wfiFlowDOS = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().in("V_FLOW_ID",
                    wfdVIds).like(StringUtils.isNotBlank(searchStr), "FLOW_NO", searchStr).select("id"));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(wfiFlowDOS)) {
                // 保存流程实例信息
//                Map<String, WfiFlowDO> idsAndWfiFlows = new HashMap<>();
//                wfiFlowDOS.forEach(wfiFlowDO -> {
//                    idsAndWfiFlows.put(wfiFlowDO.getId(), wfiFlowDO);
//                });
                List<String> procIds = wfiFlowDOS.stream().map(WfiFlowDO::getId).collect(Collectors.toList());
                List<HistoricProcessInstance> processInstances = queryUtil.getHistoricProcessInstanceQuery()
                        .processInstanceIds(new HashSet<>(procIds)).list();
                List<HistoricTaskInstance> taskInstances = new ArrayList<>();
                long count = 0L;
                if (completed == 1) {
                    count = queryUtil.getHistoricTaskInstanceQuery()
                            .processInstanceIdIn(wfiFlowDOS.stream().map(WfiFlowDO::getId).collect(Collectors.toList
                                    ())).taskCompletedBefore(new
                                    Date()).count();
                    taskInstances = queryUtil.getHistoricTaskInstanceQuery()
                            .processInstanceIdIn(procIds).taskCompletedBefore(new Date())
                            .orderByHistoricTaskInstanceStartTime().desc().listPage((current - 1) * size, size);
                } else {
                    count = queryUtil.getTaskQuery()
                            .processInstanceIdIn(wfiFlowDOS.stream().map(WfiFlowDO::getId).collect(Collectors.toList
                                    ())).count();
                    List<Task> tasks = queryUtil.getTaskQuery()
                            .processInstanceIdIn(procIds)
                            .orderByTaskCreateTime().desc().listPage((current - 1) * size, size);
                    if (CollectionUtils.isNotEmpty(tasks)) {
                        for (int i = 0; i < tasks.size(); i++) {
                            Task task;
                            task = tasks.get(i);
                            HistoricTaskInstance taskInstance = new HistoricTaskInstance() {
                                @Override
                                public String getDeleteReason() {
                                    return null;
                                }

                                @Override
                                public Date getStartTime() {
                                    return task.getCreateTime();
                                }

                                @Override
                                public Date getEndTime() {
                                    return null;
                                }

                                @Override
                                public Long getDurationInMillis() {
                                    return null;
                                }

                                @Override
                                public Long getWorkTimeInMillis() {
                                    return null;
                                }

                                @Override
                                public Date getClaimTime() {
                                    return null;
                                }

                                @Override
                                public Date getTime() {
                                    return null;
                                }

                                @Override
                                public String getId() {
                                    return task.getId();
                                }

                                @Override
                                public String getName() {
                                    return task.getName();
                                }

                                @Override
                                public String getDescription() {
                                    return null;
                                }

                                @Override
                                public int getPriority() {
                                    return 0;
                                }

                                @Override
                                public String getOwner() {
                                    return null;
                                }

                                @Override
                                public String getAssignee() {
                                    return task.getAssignee();
                                }

                                @Override
                                public String getProcessInstanceId() {
                                    return task.getProcessInstanceId();
                                }

                                @Override
                                public String getExecutionId() {
                                    return null;
                                }

                                @Override
                                public String getProcessDefinitionId() {
                                    return null;
                                }

                                @Override
                                public Date getCreateTime() {
                                    return task.getCreateTime();
                                }

                                @Override
                                public String getTaskDefinitionKey() {
                                    return null;
                                }

                                @Override
                                public Date getDueDate() {
                                    return null;
                                }

                                @Override
                                public String getCategory() {
                                    return null;
                                }

                                @Override
                                public String getParentTaskId() {
                                    return null;
                                }

                                @Override
                                public String getTenantId() {
                                    return null;
                                }

                                @Override
                                public String getFormKey() {
                                    return null;
                                }

                                @Override
                                public Map<String, Object> getTaskLocalVariables() {
                                    return null;
                                }

                                @Override
                                public Map<String, Object> getProcessVariables() {
                                    return null;
                                }
                            };
                            taskInstances.add(taskInstance);
                        }
                    }
                }
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(taskInstances)) {
                    // 查询办理人信息
                    List<String> userIDs = taskInstances.stream().map(HistoricTaskInstance::getAssignee).collect
                            (Collectors.toList());
                    Map<String, String> processInsIdsAndStarterIds = new HashMap<>();
                    processInstances.stream().forEach(processInstance -> {
                        userIDs.add(processInstance.getStartUserId());
                        processInsIdsAndStarterIds.put(processInstance.getId(), processInstance.getStartUserId());
                    });
                    Map<String, String> idsAndNames = new HashMap<>();
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userIDs)) {
                        List<SysUserDO> users = iUser.getUsersByIds(userIDs);
                        users.stream().forEach(user -> {
                            idsAndNames.put(user.getId(), StringUtils.isNotBlank(user.getRealName()) ? user
                                    .getRealName() : user.getUserName());
                        });
                    }
                    List<TaskMonitorVO> taskMonitorVOS = new ArrayList<>();
                    taskInstances.forEach(taskInstance -> {
                        TaskMonitorVO taskMonitorVO = new TaskMonitorVO();
                        taskMonitorVO.setId(taskInstance.getId());
                        taskMonitorVO.setFlowInstanceId(taskInstance.getProcessInstanceId());
                        taskMonitorVO.setEndTime(taskInstance.getEndTime());
                        taskMonitorVO.setCreateTime(taskInstance.getCreateTime());
                        WfiFlowDO wfiFlowDO = wfiFlowService.getById(taskInstance.getProcessInstanceId());
                        if (wfiFlowDO != null) {
                            taskMonitorVO.setWorkNo(wfiFlowDO.getFlowNo());
                            Flow flow = FlowUtils.getFlow(wfiFlowDO.getModel());
                            Node node = flow.getNodeById(taskInstance.getName());
                            taskMonitorVO.setName(node.getName());
                        }
                        taskMonitorVO.setAssignee(taskInstance.getAssignee());
                        taskMonitorVO.setAssigneeRealName(idsAndNames.get(taskInstance.getAssignee()));
                        taskMonitorVO.setStarterRealName(idsAndNames.get(processInsIdsAndStarterIds.get(taskInstance.getProcessInstanceId())));
                        taskMonitorVOS.add(taskMonitorVO);
                    });
                    return R.ok().put("tasks", taskMonitorVOS).put("total", count);
                }

            }
        }
        return R.ok();
    }

    @Override
    public List<TaskMonitorVO> listTaskMonitor(String flowId) {
        WfdVFlowDO wfdVFlowDO = wfdVFlowService.getEffectiveByFlowId(flowId, LocalDateTime.now());
        if (wfdVFlowDO != null) {
            // 判断用户有没有监控权限
            if (StringUtils.isNotBlank(wfdVFlowDO.getAdminAuthId())
                    && iSecurity.hasAuthority(wfdVFlowDO.getAdminAuthId())) {
                List<WfiFlowDO> wfiFlowDOS = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().eq("V_FLOW_ID",
                        wfdVFlowDO
                                .getId()));
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(wfiFlowDOS)) {
                    // 保存流程实例信息
                    Map<String, WfiFlowDO> idsAndWfiFlows = new HashMap<>();
                    wfiFlowDOS.forEach(wfiFlowDO -> {
                        idsAndWfiFlows.put(wfdVFlowDO.getId(), wfiFlowDO);
                    });
                    List<String> procIds = new ArrayList<>(idsAndWfiFlows.keySet());
                    List<HistoricTaskInstance> taskInstances = queryUtil.getHistoricTaskInstanceQuery()
                            .processInstanceIdIn(procIds).taskCompletedBefore(new Date())
                            .orderByHistoricTaskInstanceStartTime().desc().list();
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(taskInstances)) {
                        // 查询办理人信息
                        List<String> userIDs = taskInstances.stream().map(HistoricTaskInstance::getAssignee).collect
                                (Collectors.toList());
                        Map<String, String> idsAndNames = new HashMap<>();
                        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userIDs)) {
                            List<SysUserDO> users = iUser.getUsersByIds(userIDs);
                            users.stream().forEach(user -> {
                                idsAndNames.put(user.getId(), StringUtils.isNotBlank(user.getRealName()) ? user
                                        .getRealName() : user.getUserName());
                            });
                        }
                        List<TaskMonitorVO> taskMonitorVOS = new ArrayList<>();
                        taskInstances.forEach(taskInstance -> {
                            TaskMonitorVO taskMonitorVO = new TaskMonitorVO();
                            taskMonitorVO.setId(taskInstance.getId());
                            WfiFlowDO wfiFlowDO = idsAndWfiFlows.get(taskInstance.getProcessInstanceId());
                            if (wfiFlowDO != null) {
                                taskMonitorVO.setWorkNo(wfiFlowDO.getFlowNo());
                                Flow flow = FlowUtils.getFlow(wfiFlowDO.getModel());
                                Node node = flow.getNodeById(taskInstance.getName());
                                taskMonitorVO.setName(node.getName());
                            }
                            taskMonitorVO.setAssignee(idsAndNames.get(taskInstance.getAssignee()));
                            taskMonitorVOS.add(taskMonitorVO);
                        });
                        return taskMonitorVOS;
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public Long getTaskMonitorTotal() {
        // 获取当前用户所有权限
        List<String> authIds = iSecurity.getAllAuthIds();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(authIds)) {
            List<WfdVFlowDO> wfdVFlowDOS = wfdVFlowService.list(new QueryWrapper<WfdVFlowDO>().select("id").in
                    ("ADMIN_AUTH_ID",
                            authIds)
                    .le("VERSION_BEGIN_DATE", LocalDateTime.now()).and(o -> o.isNull("VERSION_END_DATE").or().ge
                            ("VERSION_END_DATE",
                                    LocalDateTime.now())));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(wfdVFlowDOS)) {
                List<WfiFlowDO> wfiFlowDOS = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().select("id").in
                        ("V_FLOW_ID",
                                wfdVFlowDOS.stream().map(WfdVFlowDO::getId).collect(Collectors.toList())));
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(wfiFlowDOS)) {
                    return queryUtil.getHistoricTaskInstanceQuery()
                            .processInstanceIdIn(wfiFlowDOS.stream().map(WfiFlowDO::getId).collect(Collectors.toList
                                    ())).taskCompletedBefore(new
                                    Date()).count();
                }
            }
        }
        return 0L;
    }

    @Override
    public R listTaskQuery(Integer current, Integer size, String flowId, int completed, String searchStr) {
        List<String> queryAuthIds = iSecurity.getAllAuthIds();
        Set<String> wfdVIds = wfdVFlowService.list(new QueryWrapper<WfdVFlowDO>()
                .eq("flow_id", flowId).in("QUERY_AUTH_ID", queryAuthIds).select("id")
        ).stream().map(WfdVFlowDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(wfdVIds)) {
            // 判断用户有没有监控权限
            List<WfiFlowDO> wfiFlowDOS = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().in("V_FLOW_ID",
                    wfdVIds).like(StringUtils.isNotBlank(searchStr), "FLOW_NO", searchStr).select("id"));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(wfiFlowDOS)) {
                // 保存流程实例信息
//                Map<String, WfiFlowDO> idsAndWfiFlows = new HashMap<>();
//                wfiFlowDOS.forEach(wfiFlowDO -> {
//                    idsAndWfiFlows.put(wfiFlowDO.getId(), wfiFlowDO);
//                });
                List<String> procIds = wfiFlowDOS.stream().map(WfiFlowDO::getId).collect(Collectors.toList());
                List<HistoricProcessInstance> processInstances = queryUtil.getHistoricProcessInstanceQuery()
                        .processInstanceIds(new HashSet<>(procIds)).list();
                List<HistoricTaskInstance> taskInstances = new ArrayList<>();
                long count = 0L;
                if (completed == 1) {
                    count = queryUtil.getHistoricTaskInstanceQuery()
                            .processInstanceIdIn(wfiFlowDOS.stream().map(WfiFlowDO::getId).collect(Collectors.toList
                                    ())).taskCompletedBefore(new
                                    Date()).count();
                    taskInstances = queryUtil.getHistoricTaskInstanceQuery()
                            .processInstanceIdIn(procIds).taskCompletedBefore(new Date())
                            .orderByHistoricTaskInstanceStartTime().desc().listPage((current - 1) * size, size);
                } else {
                    count = queryUtil.getTaskQuery()
                            .processInstanceIdIn(wfiFlowDOS.stream().map(WfiFlowDO::getId).collect(Collectors.toList
                                    ())).count();
                    List<Task> tasks = queryUtil.getTaskQuery()
                            .processInstanceIdIn(procIds)
                            .orderByTaskCreateTime().desc().listPage((current - 1) * size, size);
                    if (CollectionUtils.isNotEmpty(tasks)) {
                        for (int i = 0; i < tasks.size(); i++) {
                            Task task = tasks.get(i);
                            HistoricTaskInstance taskInstance = new HistoricTaskInstance() {
                                @Override
                                public String getDeleteReason() {
                                    return null;
                                }

                                @Override
                                public Date getStartTime() {
                                    return task.getCreateTime();
                                }

                                @Override
                                public Date getEndTime() {
                                    return null;
                                }

                                @Override
                                public Long getDurationInMillis() {
                                    return null;
                                }

                                @Override
                                public Long getWorkTimeInMillis() {
                                    return null;
                                }

                                @Override
                                public Date getClaimTime() {
                                    return null;
                                }

                                @Override
                                public Date getTime() {
                                    return null;
                                }

                                @Override
                                public String getId() {
                                    return task.getId();
                                }

                                @Override
                                public String getName() {
                                    return task.getName();
                                }

                                @Override
                                public String getDescription() {
                                    return null;
                                }

                                @Override
                                public int getPriority() {
                                    return 0;
                                }

                                @Override
                                public String getOwner() {
                                    return null;
                                }

                                @Override
                                public String getAssignee() {
                                    return task.getAssignee();
                                }

                                @Override
                                public String getProcessInstanceId() {
                                    return task.getProcessInstanceId();
                                }

                                @Override
                                public String getExecutionId() {
                                    return null;
                                }

                                @Override
                                public String getProcessDefinitionId() {
                                    return null;
                                }

                                @Override
                                public Date getCreateTime() {
                                    return task.getCreateTime();
                                }

                                @Override
                                public String getTaskDefinitionKey() {
                                    return null;
                                }

                                @Override
                                public Date getDueDate() {
                                    return null;
                                }

                                @Override
                                public String getCategory() {
                                    return null;
                                }

                                @Override
                                public String getParentTaskId() {
                                    return null;
                                }

                                @Override
                                public String getTenantId() {
                                    return null;
                                }

                                @Override
                                public String getFormKey() {
                                    return null;
                                }

                                @Override
                                public Map<String, Object> getTaskLocalVariables() {
                                    return null;
                                }

                                @Override
                                public Map<String, Object> getProcessVariables() {
                                    return null;
                                }
                            };
                            taskInstances.add(taskInstance);
                        }
                    }
                }
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(taskInstances)) {
                    // 查询办理人信息
                    List<String> userIDs = taskInstances.stream().map(HistoricTaskInstance::getAssignee).collect
                            (Collectors.toList());
                    Map<String, String> processInsIdsAndStarterIds = new HashMap<>();
                    processInstances.stream().forEach(processInstance -> {
                        userIDs.add(processInstance.getStartUserId());
                        processInsIdsAndStarterIds.put(processInstance.getId(), processInstance.getStartUserId());
                    });
                    Map<String, String> idsAndNames = new HashMap<>();
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userIDs)) {
                        List<SysUserDO> users = iUser.getUsersByIds(userIDs);
                        users.stream().forEach(user -> {
                            idsAndNames.put(user.getId(), StringUtils.isNotBlank(user.getRealName()) ? user
                                    .getRealName() : user.getUserName());
                        });
                    }
                    List<TaskQueryVO> taskQueryVOS = new ArrayList<>();
                    taskInstances.forEach(taskInstance -> {
                        TaskQueryVO taskQueryVO = new TaskQueryVO();
                        taskQueryVO.setId(taskInstance.getId());
                        taskQueryVO.setFlowInstanceId(taskInstance.getProcessInstanceId());
                        taskQueryVO.setEndTime(taskInstance.getEndTime());
                        taskQueryVO.setCreateTime(taskInstance.getCreateTime());
                        WfiFlowDO wfiFlowDO = wfiFlowService.getById(taskInstance.getProcessInstanceId());
                        if (wfiFlowDO != null) {
                            taskQueryVO.setWorkNo(wfiFlowDO.getFlowNo());
                            Flow flow = FlowUtils.getFlow(wfiFlowDO.getModel());
                            Node node = flow.getNodeById(taskInstance.getName());
                            taskQueryVO.setName(node.getName());
                        }
                        taskQueryVO.setAssignee(taskInstance.getAssignee());
                        taskQueryVO.setAssigneeRealName(idsAndNames.get(taskInstance.getAssignee()));
                        taskQueryVO.setStarterRealName(idsAndNames.get(processInsIdsAndStarterIds.get(taskInstance.getProcessInstanceId())));
                        taskQueryVOS.add(taskQueryVO);
                    });
                    return R.ok().put("tasks", taskQueryVOS).put("total", count);
                }
            }
        }
        return R.ok();
    }

    @Override
    public List<TaskQueryVO> listTaskQuery(String flowId) {
        WfdVFlowDO wfdVFlowDO = wfdVFlowService.getEffectiveByFlowId(flowId, LocalDateTime.now());
        if (wfdVFlowDO != null) {
            // 判断用户有没有监控权限
            if (StringUtils.isNotBlank(wfdVFlowDO.getQueryAuthId())
                    && iSecurity.hasAuthority(wfdVFlowDO.getQueryAuthId())) {
                List<WfiFlowDO> wfiFlowDOS = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().eq("V_FLOW_ID",
                        wfdVFlowDO
                                .getId()));
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(wfiFlowDOS)) {
                    // 保存流程实例信息
                    Map<String, WfiFlowDO> idsAndWfiFlows = new HashMap<>();
                    wfiFlowDOS.forEach(wfiFlowDO -> {
                        idsAndWfiFlows.put(wfdVFlowDO.getId(), wfiFlowDO);
                    });
                    List<String> procIds = new ArrayList<>(idsAndWfiFlows.keySet());
                    List<HistoricTaskInstance> taskInstances = queryUtil.getHistoricTaskInstanceQuery()
                            .processInstanceIdIn(procIds).taskCompletedBefore(new Date())
                            .orderByHistoricTaskInstanceStartTime().desc().list();
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(taskInstances)) {
                        // 查询办理人信息
                        List<String> userIDs = taskInstances.stream().map(HistoricTaskInstance::getAssignee).collect
                                (Collectors.toList());
                        Map<String, String> idsAndNames = new HashMap<>();
                        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userIDs)) {
                            List<SysUserDO> users = iUser.getUsersByIds(userIDs);
                            users.stream().forEach(user -> {
                                idsAndNames.put(user.getId(), StringUtils.isNotBlank(user.getRealName()) ? user
                                        .getRealName() : user.getUserName());
                            });
                        }
                        List<TaskQueryVO> taskQueryVOS = new ArrayList<>();
                        taskInstances.forEach(taskInstance -> {
                            TaskQueryVO taskQueryVO = new TaskQueryVO();
                            taskQueryVO.setId(taskInstance.getId());
                            WfiFlowDO wfiFlowDO = idsAndWfiFlows.get(taskInstance.getProcessInstanceId());
                            if (wfiFlowDO != null) {
                                taskQueryVO.setWorkNo(wfiFlowDO.getFlowNo());
                                Flow flow = FlowUtils.getFlow(wfiFlowDO.getModel());
                                Node node = flow.getNodeById(taskInstance.getName());
                                taskQueryVO.setName(node.getName());
                            }
                            taskQueryVO.setAssignee(idsAndNames.get(taskInstance.getAssignee()));
                            taskQueryVOS.add(taskQueryVO);
                        });
                        return taskQueryVOS;
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public Long getTaskQueryTotal(String flowId, int completed, String searchStr) {
        List<String> queryAuthIds = iSecurity.getAllAuthIds();
        Set<String> wfdVIds = wfdVFlowService.list(new QueryWrapper<WfdVFlowDO>()
                .in("QUERY_AUTH_ID", queryAuthIds).select("id")
        ).stream().map(WfdVFlowDO::getFlowId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(wfdVIds)) {
            // 判断用户有没有监控权限
            List<WfiFlowDO> wfiFlowDOS = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().in("V_FLOW_ID",
                    wfdVIds).like(StringUtils.isNotBlank(searchStr), "FLOW_NO", searchStr));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(wfiFlowDOS)) {
                if (completed == 1) {
                    return historyService.createHistoricTaskInstanceQuery()
                            .processInstanceIdIn(wfiFlowDOS.stream().map(WfiFlowDO::getId).collect(Collectors.toList
                                    ())).taskCompletedBefore(new
                                    Date()).count();
                } else {
                    return taskService.createTaskQuery()
                            .processInstanceIdIn(wfiFlowDOS.stream().map(WfiFlowDO::getId).collect(Collectors.toList
                                    ())).count();
                }
            }
        }

        return 0L;
    }


    /**
     * 填充被委托人信息
     *
     * @param delegateIds   被委托人主键列表
     * @param taskDelegates 委托任务
     * @return
     * @author FourLeaves
     * @date 2020/2/18 15:04
     */
    private void fillUserNames(List<String> delegateIds, List<TaskDelegateVO> taskDelegates) {
        if (CollectionUtils.isNotEmpty(delegateIds)) {
            List<SysUserDO> delegates = iUser.getUsersByIds(delegateIds);
            Map<String, String> idAndNames = new HashMap<>();
            for (SysUserDO user : delegates) {
                idAndNames.put(user.getId(), user.getRealName());
            }
            for (TaskDelegateVO taskDelegateVO : taskDelegates) {
                if (StringUtils.isNotBlank(taskDelegateVO.getDelegateId())) {
                    taskDelegateVO.setDelegate(idAndNames.get(taskDelegateVO.getDelegateId()));
                }
            }
        }
    }

    private TaskDelegateVO convertToTaskDelegate(HistoricTaskInstance taskInfo, List<String> delegateIds,
                                                 Map<String, WfdDelegateWorkDO> taskIdAndDelegateWorks, WfiFlowDO wfiFlow) {
        TaskDelegateVO taskDelegate = new TaskDelegateVO();
        taskDelegate.setId(taskInfo.getId());
        if (wfiFlow != null) {
            Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
            Node node = flow.getNodeById(taskInfo.getName());
            taskDelegate.setNodeName(node.getName());
            taskDelegate.setWorkNo(wfiFlow.getFlowNo());
        }
        taskDelegate.setFlowInstanceId(taskInfo.getProcessInstanceId());
        taskDelegate.setEndTime(taskInfo.getEndTime());

        WfdDelegateWorkDO wfdDelegateWorkDO = taskIdAndDelegateWorks.get(taskInfo.getId());
        if (wfdDelegateWorkDO != null) {
            taskDelegate.setDelegate(wfdDelegateWorkDO.getDelegateRealName());
            taskDelegate.setDelegateId(wfdDelegateWorkDO.getDelegateUserId());
            taskDelegate.setDelegateStartTime(Date.from(wfdDelegateWorkDO.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
            delegateIds.add(wfdDelegateWorkDO.getDelegateUserId());
        } else {
            return null;
        }
        return taskDelegate;
    }

    @Override
    public List<TaskDelegateVO> listTaskDelegate() {
        List<TaskDelegateVO> taskDelegates = new ArrayList<>();
        List<WfdDelegateWorkDO> delegateWorks = wfdDelegateWorkService.getDelegateWorkByUserId(securityUtils.getCurrentUserId());
        if (CollectionUtils.isNotEmpty(delegateWorks)) {
            List<HistoricTaskInstance> historicTaskInstances = queryUtil.getHistoricTaskInstanceQuery()
                    .taskNameIn(delegateWorks.stream().map(WfdDelegateWorkDO::getTaskName).collect(Collectors.toList()))
                    .processInstanceIdIn(delegateWorks.stream().map(WfdDelegateWorkDO::getProcessId).collect(Collectors.toList())).list();

            Map<String, WfdDelegateWorkDO> taskIdAndDelegateWorks = new HashMap<>();
            for (WfdDelegateWorkDO delegateWork : delegateWorks) {
                taskIdAndDelegateWorks.put(delegateWork.getTaskId(), delegateWork);
            }
            List<String> delegateIds = new ArrayList<>();
            Collection<WfiFlowDO> wfiFlows = wfiFlowService.listByIds(historicTaskInstances.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet()));
            for (HistoricTaskInstance task : historicTaskInstances) {
                WfiFlowDO wfiFlow = getWfiFlowById(wfiFlows, task.getProcessInstanceId());
                TaskDelegateVO taskDelegate = convertToTaskDelegate(task, delegateIds, taskIdAndDelegateWorks, wfiFlow);
                if (taskDelegate != null) {
                    taskDelegates.add(taskDelegate);
                }
            }
            //fillUserNames(delegateIds, taskDelegates);
        }
        return taskDelegates;
    }

    @Override
    public List<TaskDelegateVO> listTaskDelegate(Integer current, Integer size, Map<String, String> params) {
        List<TaskDelegateVO> taskDelegates = new ArrayList<>();
        Integer firstResult = ((current - 1) * size);
        Integer maxResults = size;
        List<WfdDelegateWorkDO> delegateWorks = wfdDelegateWorkService.getDelegateWorkByUserId(securityUtils.getCurrentUserId());
        if (CollectionUtils.isNotEmpty(delegateWorks)) {
            HistoricTaskInstanceQuery query = queryUtil.getHistoricTaskInstanceQuery()
                    .taskNameIn(delegateWorks.stream().map(WfdDelegateWorkDO::getTaskName).collect(Collectors.toList()));

            List<String> processInsIds = delegateWorks.stream().map(WfdDelegateWorkDO::getProcessId).collect(Collectors.toList());
            // 检索条件
            String searchStr = params.get("searchStr");
            List<String> wfiFlowIds;
            if (StringUtils.isNotBlank(searchStr)) {
                wfiFlowIds = wfiFlowService.list(new QueryWrapper<WfiFlowDO>()
                        .like("FLOW_NO", searchStr).select("id")).stream().map(WfiFlowDO::getId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(wfiFlowIds)) {
                    // 取交集
                    processInsIds.retainAll(wfiFlowIds);
                }
            }
            String flowCode = params.get("flowCode");
            query.processInstanceIdIn(processInsIds);
            if (!QueryUtil.FILTER_ALL.equals(flowCode)) {
                List<ProcessDefinition> definitions = queryUtil.getProcessDefinitionQuery().processDefinitionCategory(flowCode).list();
                if (CollectionUtils.isNotEmpty(definitions)) {
                    query.processDefinitionId(definitions.get(0).getId());
                } else {
                    return new ArrayList<>();
                }
            }
            List<HistoricTaskInstance> historicTaskInstances = query.listPage(firstResult, maxResults);

            Map<String, WfdDelegateWorkDO> taskIdAndDelegateWorks = new HashMap<>();
            for (WfdDelegateWorkDO delegateWork : delegateWorks) {
                taskIdAndDelegateWorks.put(delegateWork.getTaskId(), delegateWork);
            }
            List<String> delegateIds = new ArrayList<>();
            Collection<WfiFlowDO> wfiFlows = wfiFlowService.listByIds(historicTaskInstances.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet()));
            for (HistoricTaskInstance task : historicTaskInstances) {
                WfiFlowDO wfiFlow = getWfiFlowById(wfiFlows, task.getProcessInstanceId());
                TaskDelegateVO taskDelegate = convertToTaskDelegate(task, delegateIds, taskIdAndDelegateWorks, wfiFlow);
                if (taskDelegate != null) {
                    taskDelegates.add(taskDelegate);
                }
            }

            //fillUserNames(delegateIds, taskDelegates);
        }
        return taskDelegates;
    }

    @Override
    public boolean isFocused(Map<String, String> params) {
        String taskId = params.get("taskId");
        String formId = params.get("formId");
        String flowCode = params.get("flowCode");
        String flowInstanceId;
        if (StringUtils.isBlank(taskId)) {
            WfiFlowDO wfiFlowDO = getWfiFlowByBusinessKey(flowCode, formId);
            if (wfiFlowDO != null) {
                flowInstanceId = wfiFlowDO.getId();
            } else {
                return false;
            }
        } else {
            HistoricTaskInstance taskInstance = queryUtil.getHistoricTaskInstance(taskId);
            if (taskInstance != null) {
                flowInstanceId = taskInstance.getProcessInstanceId();
            } else {
                return false;
            }
        }
        return wfiFocusedWorkService.count(new QueryWrapper<WfiFocusedWorkDO>()
                .eq("flow_instance_id", flowInstanceId).eq("user_id", securityUtils.getCurrentUserId())) == 1;
    }

    private class ProcessInstanceWfiFlow {
        private ProcessInstance processInstance;
        private WfiFlowDO wfiFlow;
        private Flow flow;

        public ProcessInstanceWfiFlow(ProcessInstance processInstance, WfiFlowDO wfiFlow) {
            this.processInstance = processInstance;
            this.wfiFlow = wfiFlow;
            if (wfiFlow != null) {
                flow = FlowUtils.getFlow(wfiFlow.getModel());
            }
        }
    }

    @Override
    public R listTaskFocused(Map<String, String> params) {
        int current = Integer.parseInt(params.get("current"));
        int size = Integer.parseInt(params.get("size"));
        String searchStr = params.get("searchStr");
        String flowCode = params.get("flowCode");
        String isRead = params.get("isRead");
        /**
         * 流程类型
         */
        String flowType = params.get("flowType");
        Page<WfiFocusedWorkDO> page = new Page<>(current, size);
        IPage list = wfiFocusedWorkService.page(page, new QueryWrapper<WfiFocusedWorkDO>()
                .like(StringUtils.isNotBlank(searchStr), "WORK_NO", searchStr)
                .eq(Objects.equals("no", isRead), "Latest_Read_Flag", 1)
                .eq(StringUtils.isNotBlank(flowCode) && !QueryUtil.FILTER_ALL.equals(flowCode) && Objects.equals(flowType, "categoryNode"), "wfd_category_id", flowCode)
                .eq(StringUtils.isNotBlank(flowCode) && !QueryUtil.FILTER_ALL.equals(flowCode) && !Objects.equals(flowType, "categoryNode"), "wfd_id", flowCode)
                .eq("app_id", securityUtils.getAppName()).eq("user_id", securityUtils.getCurrentUserId())
                .orderByDesc("create_time"));
        List<WfiFocusedWorkDO> focusedWorks = list.getRecords();
        if (CollectionUtils.isNotEmpty(focusedWorks)) {
            Set<String> userIds = new HashSet<>();
            // 发起人
            userIds.addAll(focusedWorks.stream().map(WfiFocusedWorkDO::getStarterId).collect(Collectors.toList()));
            List<SysUserDO> userDOS = iUser.getUsersByIds(new ArrayList<>(userIds));
            Map<String, String> idAndRealNames = new HashMap<>();
            userDOS.stream().forEach(user -> {
                idAndRealNames.put(user.getId(), user.getRealName());
            });
            Set<String> flowIds = focusedWorks.stream().map(WfiFocusedWorkDO::getFlowInstanceId).collect(Collectors.toSet());
            List<ProcessInstance> processInstances = queryUtil.getProcessInstanceQuery().processInstanceIds(flowIds).list();
            Collection<WfiFlowDO> wfiFlows = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().in("id",
                    flowIds).select("id", "model", "flow_no", "flow_id", "business_key"));
            // fillWfiFlowModels(wfiFlows);
            List<HistoricTaskInstance> historicTaskInstances = queryUtil.getHistoricTaskInstanceQuery().processInstanceIdIn(new ArrayList<>(flowIds)).list();
            WfiFlowDO wfiFlow;
            ProcessInstance processInstance;
            for (WfiFocusedWorkDO focusedWork : focusedWorks) {
                wfiFlow = wfiFlows.stream().filter(o -> StringUtils.equals(o.getId(), focusedWork.getFlowInstanceId())).findFirst().orElse(null);
                processInstance = processInstances.stream().filter(o -> StringUtils.equals(o.getId(), focusedWork.getFlowInstanceId())).findFirst().orElse(null);
                if (StringUtils.isNotBlank(focusedWork.getStarterId())) {
                    focusedWork.setStarterRealName(idAndRealNames.get(focusedWork.getStarterId()));
                }
                if (processInstance == null) {
                    // 没有正在运行的实例，则说明已办结或者已删除
                    if (wfiFlow == null) {
                        focusedWork.setState("end");
                        LOGGER.info("WfiFlow missing: " + focusedWork.getFlowInstanceId());
                    } else {
                        focusedWork.setState("end");
                    }
                } else {
                    focusedWork.setState("normal");
                    if (wfiFlow != null) {
                        List<HistoricTaskInstance> tasks = historicTaskInstances.stream()
                                .filter(o -> StringUtils.equals(o.getProcessInstanceId(), focusedWork.getFlowInstanceId()))
                                .sorted(Comparator.comparing(TaskInfo::getCreateTime)).collect(Collectors.toList());
                        // 获取最新创建的任务
                        if (tasks.size() > 0) {
                            try {
                                Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
                                Node node = null;
                                try {
                                    node = flow.getNodeById(tasks.get(tasks.size() - 1).getName());
                                } catch (NodeNotFoundByIdException e) {
                                    wfiFlow = wfiFlowService.getById(wfiFlow.getId());
                                    flow = FlowUtils.getFlow(wfiFlow.getModel());
                                    node = flow.getNodeById(tasks.get(tasks.size() - 1).getName());
                                }

                                focusedWork.setCurrentActName(node.getName());
                            } catch (BpmException ex) {
                                ex.printStackTrace();
                                LOGGER.error(ex.getMessage());
                            }
                        }
                    }
                }
            }
            return R.ok().put("list", focusedWorks).put("total", list.getTotal());
        }
        return R.ok();
    }

    @Override
    public R getTaskDelegateInfo(Integer current, Integer size, Map<String, String> params) {
        String searchStr = params.get("searchStr");
        String flowCode = params.get("flowCode");
        /**
         * 流程类型
         */
        String flowType = params.get("flowType");
        Page<WfdDelegateWorkDO> page = new Page<>(current, size);
        IPage list = wfdDelegateWorkService.page(page, new QueryWrapper<WfdDelegateWorkDO>()
                .like(StringUtils.isNotBlank(searchStr), "FLOW_NO", searchStr)
                .eq(StringUtils.isNotBlank(flowCode) && !QueryUtil.FILTER_ALL.equals(flowCode) && Objects.equals(flowType, "categoryNode"), "wfd_category_id", flowCode)
                .eq(StringUtils.isNotBlank(flowCode) && !QueryUtil.FILTER_ALL.equals(flowCode) && !Objects.equals(flowType, "categoryNode"), "wfd_id", flowCode)
                .eq("app_id", securityUtils.getAppName()).eq("user_id", securityUtils.getCurrentUserId())
                .orderByDesc("create_time"));
        List<WfdDelegateWorkDO> delegateWorks = list.getRecords();
        if (CollectionUtils.isNotEmpty(delegateWorks)) {
            List<String> processInsIds = delegateWorks.stream().map(WfdDelegateWorkDO::getProcessId).collect(Collectors.toList());
            HistoricTaskInstanceQuery query = queryUtil.getHistoricTaskInstanceQuery()
                    .taskNameIn(delegateWorks.stream().map(WfdDelegateWorkDO::getTaskName).collect(Collectors.toList())).processInstanceIdIn(processInsIds);
            List<TaskDelegateVO> taskDelegates = new ArrayList<>(delegateWorks.size());
            List<HistoricTaskInstance> allHistoricTaskInstances = query.list();
            delegateWorks.stream().forEach(delegateWork -> {
                TaskDelegateVO taskDelegate = new TaskDelegateVO();
                taskDelegate.setId(delegateWork.getTaskId());
                taskDelegate.setNodeName(delegateWork.getNodeName());
                taskDelegate.setWorkNo(delegateWork.getFlowNo());
                taskDelegate.setFlowInstanceId(delegateWork.getProcessId());
                HistoricTaskInstance historicTaskInstance = null;
                try {
                    historicTaskInstance = allHistoricTaskInstances.stream().filter(task -> Objects.equals(task.getId(), delegateWork.getTaskId())).findFirst().get();
                } catch (Exception e) {

                }
                taskDelegate.setEndTime(historicTaskInstance == null ? null : historicTaskInstance.getEndTime());
                taskDelegate.setDelegate(delegateWork.getDelegateRealName());
                taskDelegate.setDelegateId(delegateWork.getDelegateUserId());
                taskDelegate.setDeleUserId(delegateWork.getUserId());
                taskDelegate.setStarterId(queryUtil
                        .getHistoricProcessInstance(delegateWork.getProcessId()).getStartUserId());
                taskDelegates.add(taskDelegate);

            });
            //wfdDelegateWorkService.fillDelegateWork();
            return R.ok().put("tasks", taskDelegates).put("total", list.getTotal());
        }
        return R.ok();
//        List<TaskDelegateVO> taskDelegates = new ArrayList<>();
//        Integer firstResult = ((current - 1) * size);
//        Integer maxResults = size;
//        List<WfdDelegateWorkDO> delegateWorks = wfdDelegateWorkService.getDelegateWorkByUserId(securityUtils.getCurrentUserId());
//        if (CollectionUtils.isNotEmpty(delegateWorks)) {
//            HistoricTaskInstanceQuery query = queryUtils.getHistoricTaskInstanceQuery()
//                    .taskNameIn(delegateWorks.stream().map(WfdDelegateWorkDO::getTaskName).collect(Collectors.toList()));
//            List<String> processInsIds = delegateWorks.stream().map(WfdDelegateWorkDO::getProcessId).collect(Collectors.toList());
//            // 检索条件
//            String searchStr = params.get("searchStr");
//            List<String> wfiFlowIds;
//            if (StringUtils.isNotBlank(searchStr)) {
//                wfiFlowIds = wfiFlowService.list(new QueryWrapper<WfiFlowDO>()
//                        .like("FLOW_NO", searchStr).select("id")).stream().map(WfiFlowDO::getId).collect(Collectors.toList());
//                if (CollectionUtils.isNotEmpty(wfiFlowIds)) {
//                    // 取交集
//                    processInsIds.retainAll(wfiFlowIds);
//                }
//            }
//            String flowCode = params.get("flowCode");
//            /**
//             * 流程类型
//             */
//            String flowType = params.get("flowType");
//
//            if (StringUtils.isNotBlank(flowCode)) {
//                if (!QueryUtils.FILTER_ALL.equals(flowCode)) {
//                    List<String> wfdIds = new ArrayList<>();
//                    if (Objects.equals(flowType, "categoryNode")) {
//                        wfdIds = wfdFlowService.list(new QueryWrapper<WfdFlowDO>().select("id").eq("CATEGORY_ID", flowCode)).stream().map(WfdFlowDO::getId).collect(Collectors.toList());
//                    } else {
//                        wfdIds.add(flowCode);
//                    }
//                    if (CollectionUtils.isNotEmpty(wfdIds)) {
//                        List<String> wfiIds = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().select("id").in("FLOW_ID", wfdIds)).stream().map(WfiFlowDO::getId).collect(Collectors.toList());
//                        if (CollectionUtils.isNotEmpty(wfiIds)) {
//                            processInsIds.retainAll(wfiIds);
//                        } else {
//                            return R.ok();
//                        }
//                    } else {
//                        return R.ok();
//                    }
//                }
//            }
//
//            if (CollectionUtils.isEmpty(processInsIds)) {
//                return R.ok();
//            }
//            query.processInstanceIdIn(processInsIds);
//            Map<String, WfdDelegateWorkDO> taskIdAndDelegateWorks = new HashMap<>();
//            for (WfdDelegateWorkDO delegateWork : delegateWorks) {
//                taskIdAndDelegateWorks.put(delegateWork.getTaskId(), delegateWork);
//            }
//
//            // 全部 用于计数
//            List<HistoricTaskInstance> allHistoricTaskInstances = query.list();
//
//            List<String> delegateIds = new ArrayList<>();
//            Collection<WfiFlowDO> wfiFlows = wfiFlowService.listByIds(allHistoricTaskInstances.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet()));
//            for (HistoricTaskInstance task : allHistoricTaskInstances) {
//                WfiFlowDO wfiFlow = getWfiFlowById(wfiFlows, task.getProcessInstanceId());
//                TaskDelegateVO taskDelegate = convertToTaskDelegate(task, delegateIds, taskIdAndDelegateWorks, wfiFlow);
//                if (taskDelegate != null) {
//                    taskDelegates.add(taskDelegate);
//                }
//            }
//
//            int count = taskDelegates.size();
//
//            List<TaskDelegateVO> newTaskDelegates = new ArrayList<>();
//            if (count > 0) {
//                int lastIndex = maxResults + firstResult;
//                newTaskDelegates = taskDelegates.subList(firstResult, lastIndex >= taskDelegates.size() ? taskDelegates.size() : lastIndex);
//            }
//            return R.ok().put("tasks", newTaskDelegates).put("total", count);
//        }
//        return R.ok();
    }

    @Override
    public Long getTaskDelegateTotal(Map<String, String> params) {
        return (long) wfdDelegateWorkService.count(new QueryWrapper<WfdDelegateWorkDO>()
                .eq("app_id", securityUtils.getAppName()).eq("user_id", securityUtils.getCurrentUserId()));
    }

    @Override
    public NodeInfo getNodeInfo(String flowCode, String nodeId) {
        WfdVFlowDO wfdVFlow = wfdVFlowService.getEffectiveByCode(flowCode, LocalDateTime.now());
        if (wfdVFlow == null) {
            throw new WfdVFlowNotFoundByCodeException(flowCode);
        }
        Flow flow = FlowUtils.getFlow(wfdVFlow.getModel());
        Node node;
        if (StringUtils.isEmpty(nodeId)) {
            // 如果nodeId为空，则取第一个允许到达的人工节点
            node = FlowUtils.getFirstFlowOutManualNode(flow);
        } else {
            // 如果nodeId不为空,则取对应节点
            node = flow.getNodeById(nodeId);
        }
        return new NodeInfoImpl(node);
    }

    private void combineFlowInfo(List<TaskMineVO> taskMines) {
        Set<String> processInstanceSet = taskMines.stream().map(TaskMineVO::getFlowInstanceId).collect(Collectors
                .toSet());
        if (processInstanceSet.size() > 0) {
            List<HistoricProcessInstance> historicProcessInstances = queryUtil.getHistoricProcessInstanceQuery().processInstanceIds(processInstanceSet).list();
            if (historicProcessInstances.size() > 0) {
                List<SysUserDO> sysUsers = iUser.getUsersByIds(historicProcessInstances.stream().map
                        (HistoricProcessInstance::getStartUserId).collect(Collectors.toList()));
                for (TaskMineVO taskMine : taskMines) {
                    for (HistoricProcessInstance processInstance : historicProcessInstances) {
                        if (processInstance.getId().equals(taskMine.getFlowInstanceId())) {
                            for (SysUserDO sysUser :
                                    sysUsers) {
                                if (sysUser.getId().equals(processInstance.getStartUserId())) {
                                    taskMine.setFlowStarter(sysUser.getRealName());
                                    break;
                                }
                            }
                            taskMine.setFlowStartTime(processInstance.getStartTime());
                            break;
                        }
                    }
                }
            }
        }
    }

    private NodeInfo getNodeInfo(WfiFlowDO wfiFlow, String taskId) {
        TaskInstance taskInstance = getTaskInstanceById(taskId);
        return getNodeInfo(wfiFlow, taskInstance);
    }

    private NodeInfo getNodeInfo(WfiFlowDO wfiFlow, TaskInstance taskInstance) {
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        return getNodeInfo(wfiFlow, taskInstance, flow);
    }

    private NodeInfo getNodeInfo(WfiFlowDO wfiFlow, TaskInstance taskInstance, Flow flow) {
        Node node = flow.getNodeById(taskInstance.getNodeId());
        return new NodeInfoImpl(node, wfiFlow, taskInstance);
    }

    private NodeInfo getNodeInfo(WfiFlowDO wfiFlow, HistoricTaskInstance historicTaskInstance, Flow flow) {
        return getNodeInfo(wfiFlow, new com.csicit.ace.bpm.activiti.impl.historic.TaskInstanceImpl
                (historicTaskInstance, wfiFlow), flow);
    }

    private NodeInfo getNodeInfo(WfiFlowDO wfiFlow, HistoricTaskInstance historicTaskInstance) {
        return getNodeInfo(wfiFlow, new com.csicit.ace.bpm.activiti.impl.historic.TaskInstanceImpl
                (historicTaskInstance, wfiFlow));
    }

    @Override
    public NodeInfo getNodeInfoByTaskIdWithoutAuth(String taskId) {
        TaskInstance taskInstance = getTaskInstanceById(taskId);
        WfiFlowDO wfiFlow = wfiFlowService.getById(taskInstance.getFlowInstanceId());
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByIdException(taskInstance.getFlowInstanceId());
        }
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(taskInstance.getNodeId());
        return new NodeInfoImpl(node, wfiFlow, taskInstance, false);
    }

    @Override
    public NodeInfo getNodeInfoByTaskId(String taskId) {
        TaskInstance taskInstance = getTaskInstanceById(taskId);
        WfiFlowDO wfiFlow = wfiFlowService.getById(taskInstance.getFlowInstanceId());
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByIdException(taskInstance.getFlowInstanceId());
        }
        return getNodeInfo(wfiFlow, taskInstance);
    }

    @Override
    public NodeInfo getLatestNodeInfoByBusinessKey(String code, String businessKey) {
        WfiFlowDO wfiFlow = wfiFlowService.getOne(new QueryWrapper<WfiFlowDO>().eq("FLOW_CODE", code).eq
                ("BUSINESS_KEY", businessKey));
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByCodeException(code, businessKey);
        }
        List<HistoricTaskInstance> historicTaskInstances = queryUtil.listHistoricTaskInstances(wfiFlow.getId());
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(historicTaskInstances)) {
            List<HistoricTaskInstance> pendingTasks = historicTaskInstances.stream().filter(o -> o.getEndTime() ==
                    null).collect(Collectors.toList());
            if (pendingTasks.size() > 0) {
                return getNodeInfo(wfiFlow, pendingTasks.get(0));
            } else {
                Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
                return new NodeInfoImpl(flow.getNodes().stream().filter(node -> Objects.equals(node.getNodeType(),
                        "end")).findFirst().get());
                // return getNodeInfo(wfiFlow, historicTaskInstances.get(0));
            }
        } else {
            throw new BpmException(LocaleUtils.getNoAccessToGetNodeInfo(code, businessKey));
        }
    }

    @Override
    public NodeInfo getNodeInfoByBusinessKey(String code, String businessKey) {
        WfiFlowDO wfiFlow = queryUtil.getWfiFlowByBusinessKey(code, businessKey);
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByCodeException(code, businessKey);
        }
        // 先查询是否存在办理中的task
        // 优先查找本人是否有正在办理的任务
        SysUserDO currentUser = securityUtils.getCurrentUser();
        String currentUserId = currentUser.getId();
        List<HistoricTaskInstance> historicTaskInstances = queryUtil.listHistoricTaskInstances(wfiFlow.getId());
        List<HistoricTaskInstance> myTasks = historicTaskInstances.stream().filter(o -> Objects.equals(currentUserId, o.getAssignee())).collect(Collectors.toList());
        myTasks.sort(Comparator.comparing(HistoricTaskInstance::getStartTime));
        if (myTasks.size() > 0) {
            // 如果有当前用户办理的任务
            List<HistoricTaskInstance> pendingTasks = myTasks.stream().filter(o -> o.getEndTime() == null).collect
                    (Collectors.toList());
            if (pendingTasks.size() > 0) {
                // 如果有未办结的任务
                return getNodeInfo(wfiFlow, pendingTasks.get(0));
            } else {
                // 如果没有未办结的任务
                return getNodeInfo(wfiFlow, myTasks.get(0));
            }
        } else {
            // 如果没有当前用户办理的任务
            if (historicTaskInstances.size() > 0) {
                // 如果当前实例有任务，判断用户是否有监控权限/查询权限，如果有则返回最后一个任务
                Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
                if (hasAdminAuth(flow, currentUser) || hasQueryAuth(flow, currentUser)) {
                    List<HistoricTaskInstance> pendingTasks = historicTaskInstances.stream().filter(o -> o.getEndTime
                            () == null).collect(Collectors.toList());
                    if (pendingTasks.size() > 0) {
                        return getNodeInfo(wfiFlow, pendingTasks.get(0));
                    } else {
                        return getNodeInfo(wfiFlow, historicTaskInstances.get(0));
                    }
                } else {
                    throw new BpmException(LocaleUtils.getNoAccessToGetNodeInfo(code, businessKey));
                }
            } else {
                throw new BpmException(LocaleUtils.getTaskNotFoundByFlowInstanceId(wfiFlow.getId()));
            }
        }
    }


    @Override
    public List<ActivityVO> listActivities(String id) {
        List<ActivityVO> activityVOS = new ArrayList<>(16);
        if (StringUtils.isNotBlank(id)) {
            WfiFlowDO wfiFlow = wfiFlowService.getById(id);
            Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
            List<HistoricActivityInstance> list = queryUtil.getHistoricActivityInstanceQuery(id)
                    .orderByHistoricActivityInstanceStartTime().asc().list();

            List<HistoricTaskInstance> taskInstances = queryUtil.listHistoricTaskInstances(id);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
                Map<String, Object> wfiDeliverIds = queryUtil.listTaskVariableValues(taskInstances.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet()), TaskVariableName.WFI_DELIVER_ID, false);
                for (int i = 0; i < list.size(); i++) {
                    HistoricActivityInstance historicActivityInstance = list.get(i);
                    ActivityVO activityVO = new ActivityVO();
                    activityVO.setSn(i + 1);
                    activityVO.setEndTime(historicActivityInstance.getEndTime());
                    activityVO.setStartTime(historicActivityInstance.getStartTime());
                    activityVO.setNodeId(historicActivityInstance.getActivityName());
                    activityVO.setTaskId(historicActivityInstance.getTaskId());
                    // 转交人
                    HistoricTaskInstance taskInstance = taskInstances.stream().filter(taskInstanceT -> Objects.equals
                            (taskInstanceT.getId(), historicActivityInstance.getTaskId())).findAny().orElse
                            (null);
                    if (taskInstance != null) {
                        if (wfiDeliverIds.containsKey(taskInstance.getId())) {
                            String deliverInfoId = (String) wfiDeliverIds.get(taskInstance.getId());
                            if (StringUtils.isNotBlank(deliverInfoId)) {
                                WfiDeliverDO wfiDeliverDO = wfiDeliverService.getById(deliverInfoId);
                                if (wfiDeliverDO != null) {
                                    DeliverInfo deliverInfo = JsonUtils.castObject(wfiDeliverDO.getDeliverInfo(), DeliverInfo.class);
                                    // 驳回到的节点ID
                                    if (OperationType.Reject.isEquals(deliverInfo.getOperationType())) {
                                        for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
                                            activityVO.addRejectToNodeId(deliverNode.getNodeId());
                                            Node node = flow.getNodeById(deliverNode.getNodeId());
                                            activityVO.addRejectToNodeName(node.getName());
                                        }
                                    }
                                }
                            }
                        }
                        activityVOS.add(activityVO);
                    }
                }
            }
        }
        return activityVOS;
    }

    @Override
    public List<ActivityVO> listActivitiesByBusinessKey(String flowCode, String businessKey) {
        if (StringUtils.isNotBlank(flowCode) && StringUtils.isNotBlank(businessKey)) {
            WfiFlowDO wfiFlowDO = wfiFlowService.getByCode(flowCode, businessKey);
            if (wfiFlowDO != null) {
                return listActivities(wfiFlowDO.getId());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<ActivityVO> listActivitiesByTaskId(String taskId) {
        if (StringUtils.isNotBlank(taskId)) {
            Task task = queryUtil.getTaskById(taskId);
            if (task != null) {
                return listActivities(task.getProcessInstanceId());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<TaskInstance> listParentTasks(Flow flow, String taskId) {
        List<HistoricTaskInstance> historicTaskInstances = queryUtil.listParentTasks(flow, taskId);
        List<TaskInstance> parentTasks = new ArrayList<>();
        Collection<WfiFlowDO> wfiFlows = wfiFlowService.listByIds(historicTaskInstances.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet()));
        Map<String, Integer> nodeIdIndexs = new HashMap<>(16);
        for (HistoricTaskInstance task : historicTaskInstances) {
            Node node = flow.getNodeById(task.getName());
            UserType userType = getUserType(node, task.getId(), task.getAssignee());
            if (UserType.Host.equals(userType)) {
                if (nodeIdIndexs.containsKey(task.getName())) {
                    WfiFlowDO wfiFlow = getWfiFlowById(wfiFlows, task.getProcessInstanceId());
                    parentTasks.set(nodeIdIndexs.get(task.getName()), new com.csicit.ace.bpm.activiti.impl.historic.TaskInstanceImpl(task, wfiFlow));
                } else {
                    WfiFlowDO wfiFlow = getWfiFlowById(wfiFlows, task.getProcessInstanceId());
                    parentTasks.add(new com.csicit.ace.bpm.activiti.impl.historic.TaskInstanceImpl(task, wfiFlow));
                    nodeIdIndexs.put(task.getName(), parentTasks.size() - 1);
                }
            }
        }
        return parentTasks;
    }

    private Flow getFlowByFlowId(String flowId) {
        WfiFlowDO wfiFlow = getWfiFlowById(flowId);
        return FlowUtils.getFlow(wfiFlow.getModel());
    }

    private HistoricTaskInstance getNodeHostHistoricTaskInstance(String flowId, String nodeCode) {
        Flow flow = getFlowByFlowId(flowId);
        Node node = flow.getNodeByCode(nodeCode);
        List<HistoricTaskInstance> historicTaskInstances = queryUtil.listHistoricTaskInstancesByNodeId(flowId, node
                .getId());
        List<HistoricTaskInstance> pendingTaskInstances = historicTaskInstances.stream().filter(o -> o.getEndTime()
                == null).collect(Collectors.toList());
        if (pendingTaskInstances.size() > 0) {
            // 如果存在正在办理的任务，则主办人以正在办理的任务为准
            for (HistoricTaskInstance task : pendingTaskInstances) {
                if (UserType.Host.equals(getUserType(node, task.getId(), task.getAssignee()))) {
                    return task;
                }
            }
            historicTaskInstances.removeAll(pendingTaskInstances);
        }
        // 如果正在办理的任务中没有主办人办理的任务，以最后办结的任务经办人作为主办人
        historicTaskInstances.sort((o1, o2) -> ObjectUtils.compare(o2.getEndTime(), o1.getEndTime()));
        for (HistoricTaskInstance task : historicTaskInstances) {
            if (UserType.Host.equals(getUserType(node, task.getId(), task.getAssignee()))) {
                return task;
            }
        }
        return null;
    }

    @Override
    public String getNodeHostId(String flowId, String nodeCode) {
        HistoricTaskInstance hostTask = getNodeHostHistoricTaskInstance(flowId, nodeCode);
        return hostTask == null ? "" : hostTask.getAssignee();
    }

    @Override
    public SysUserDO getNodeHost(String flowId, String nodeCode) {
        HistoricTaskInstance hostTask = getNodeHostHistoricTaskInstance(flowId, nodeCode);
        return hostTask == null ? null : iUser.getUserById(hostTask.getAssignee());
    }

    @Override
    public String getNodeHostOpinion(String flowId, String nodeCode) {
        HistoricTaskInstance hostTask = getNodeHostHistoricTaskInstance(flowId, nodeCode);
        if (hostTask != null) {
            if (hostTask.getEndTime() != null) {
                String wfiDeliverId = queryUtil.getWfiDeliverId(hostTask);
                DeliverInfo deliverInfo = queryUtil.getDeliverInfo(wfiDeliverId);
                return deliverInfo.getOpinion();
            }
        }
        return "";
    }

    @Override
    public List<Node> getRejectFromNode(String flowId) {
        Flow flow = getFlowByFlowId(flowId);
        List<Node> nodes = new ArrayList<>();
        List<Task> tasks = queryUtil.listTasks(flowId);
        for (Task task : tasks) {
            String wfiDeliverIdFrom = queryUtil.getWfiDeliverIdFrom(task.getId());
            if (!StringUtils.isEmpty(wfiDeliverIdFrom)) {
                WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverById(wfiDeliverIdFrom);
                DeliverInfo deliverInfo = wfiDeliver.getDeliverInfoClass();
                if (OperationType.Reject.isEquals(deliverInfo.getOperationType())) {
                    HistoricTaskInstance historicTaskInstance = queryUtil.getHistoricTaskInstance(deliverInfo
                            .getTaskId());
                    nodes.add(flow.getNodeById(historicTaskInstance.getName()));
                }
            }
        }
        return nodes;
    }

    @Override
    public UserType getUserType(Node node, String taskId, String currentUserId) {
        String wfiDeliverIdFrom = (String) getTaskVariable(taskId, TaskVariableName.WFI_DELIVER_ID_FROM);
        WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverById(wfiDeliverIdFrom);
        DeliverInfo deliverInfo = wfiDeliver.getDeliverInfoClass();
        for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
            if (deliverNode.getNodeId().equals(node.getId())) {
                return getUserType(node, currentUserId, deliverNode);
            }
        }
        return getUserType(node, currentUserId, queryUtil.getDeliverNodeFrom(node, taskId));
    }

    @Override
    public UserType getUserType(Node node, String currentUserId, DeliverNode deliverNode) {
        List<DeliverUser> deliverUsers = deliverNode.getDeliverUsers().stream().filter(o -> Objects.equals(currentUserId, o.getUserId())).collect(Collectors.toList());
        if (deliverUsers.size() > 0) {
            if (HostMode.FirstClaim.isEquals(node.getHostMode())) {
                if (deliverUsers.stream().anyMatch(o -> o.getClaimTime() != null)) {
                    return UserType.Host;
                } else {
                    return UserType.Assistant;
                }
            } else if (HostMode.Specified.isEquals(node.getHostMode())) {
                if (deliverUsers.stream().anyMatch(o -> UserType.Host.isEquals(o.getUserType()))) {
                    return UserType.Host;
                } else {
                    return UserType.Assistant;
                }
            } else if (HostMode.AllowDeliver.isEquals(node.getHostMode()) || HostMode.Everybody.isEquals(node
                    .getHostMode())) {
                if (deliverNode.getDeliverUsers().stream().anyMatch(o -> o.getClaimTime() != null)) {
                    if (deliverUsers.stream().anyMatch(o -> o.getClaimTime() != null)) {
                        return UserType.Host;
                    } else {
                        return UserType.Assistant;
                    }
                } else {
                    return UserType.Host;
                }
            }
        }
        return UserType.None;
    }

    @Override
    public Object getFormValue(String tableName, String columnName, String formIdName, String businessKey) {
        return wfiFlowService.getFormValue(tableName, columnName, formIdName, businessKey);
    }

    @Override
    public Map<String, Object> listTaskVariableValues(List<TaskInstance> taskInstances, TaskVariableName taskVariableName) {
        Set<String> taskIds = taskInstances.stream().map(TaskInstance::getId).collect(Collectors.toSet());
        return queryUtil.listTaskVariableValues(taskIds, taskVariableName);
    }

    @Override
    public Object getTaskVariable(String taskId, TaskVariableName taskVariableName) {
        return queryUtil.getTaskVariable(taskId, taskVariableName);
    }

    @Override
    public WfiFlowDO getWfiFlowByTaskId(String taskId) {
        HistoricTaskInstance task = queryUtil.getHistoricTaskInstance(taskId);
        return getWfiFlowById(task.getProcessInstanceId());
    }

    @Override
    public List<WfdFlowDO> getFlowsByQueryAuth() {
        List<String> authIds = iSecurity.getAllAuthIds();
        if (CollectionUtils.isNotEmpty(authIds)) {
            Set<String> wfdIds = wfdVFlowService.list(new QueryWrapper<WfdVFlowDO>()
                    .in("QUERY_AUTH_ID", authIds).select("flow_id")
            ).stream().map(WfdVFlowDO::getFlowId).distinct().collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(wfdIds)) {
                return new ArrayList<>(wfdFlowService.listByIds(wfdIds));
            }
        }
        return null;
    }

    @Override
    public List<WfdFlowDO> getFlowsByMonitorAuth() {
        List<String> authIds = iSecurity.getAllAuthIds();
        if (CollectionUtils.isNotEmpty(authIds)) {
            Set<String> wfdIds = wfdVFlowService.list(new QueryWrapper<WfdVFlowDO>()
                    .in("ADMIN_AUTH_ID", authIds).select("flow_id")
            ).stream().map(WfdVFlowDO::getFlowId).distinct().collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(wfdIds)) {
                return new ArrayList<>(wfdFlowService.listByIds(wfdIds));
            }
        }
        return null;
    }

    @Override
    public void appendNodeInfo(String flowId, List<WfiCommentDO> wfiComments) {
        List<WfiCommentDO> taskComments = wfiComments.stream().filter(o -> StringUtils.isNotEmpty(o.getTaskId())).collect(Collectors.toList());
        if (taskComments.size() > 0) {
            WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(flowId);
            Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
            List<HistoricTaskInstance> tasks = queryUtil.getHistoricTaskInstanceQuery(flowId).list();
            for (WfiCommentDO wfiComment : taskComments) {
                Optional<HistoricTaskInstance> task = tasks.stream().filter(o -> wfiComment.getTaskId().equals(o.getId())).findFirst();
                if (task.isPresent()) {
                    Node node = flow.getNodeById(task.get().getName());
                    wfiComment.setNode(new NodeVo(node));
                } else {
                    throw new TaskNotFoundByIdException(wfiComment.getTaskId());
                }
            }
        }
    }

    @Override
    public String getNodeIdByTaskId(String taskId) {
        HistoricTaskInstance task = queryUtil.getHistoricTaskInstance(taskId);
        return task.getName();
    }

    @Override
    public OperationType getOperationType(Boolean finished, UserType userType, Node node, String taskId, String owner, String assignee, WfiDeliverTasks wfiDeliverTasks) {
        // 如果是无关人员
        if (UserType.None.equals(userType)) {
            // 如果是无关人员，则不能进行任何操作
            return OperationType.None;
        } else {
            if (finished) {
                // 如果工作已办结
                return OperationType.None;
            } else {
                if (UserType.Host.equals(userType)) {
                    if (HostMode.FirstClaim.isEquals(node.getHostMode())) {
                        if (queryUtil.allowDeliverHost(node, taskId, owner, assignee, wfiDeliverTasks)) {
                            return OperationType.Deliver;
                        } else {
                            return OperationType.None;
                        }
                    } else {
                        return OperationType.Deliver;
                    }
                } else if (UserType.Assistant.equals(userType)) {
                    return OperationType.Complete;
                } else {
                    return OperationType.None;
                }
            }
        }
    }

    @Override
    public OperationType getOperationType(String flowId, String taskId) {
        if (!StringUtils.isEmpty(taskId)) {
            WfiTaskPendingDO wfiTaskPending = wfiTaskPendingService.getByTaskId(flowId, taskId, securityUtils.getCurrentUserId());
            if (wfiTaskPending != null) {
                if (UserType.Host.isEquals(wfiTaskPending.getUserType())) {
                    return OperationType.Deliver;
                } else if (UserType.Assistant.isEquals(wfiTaskPending.getUserType())) {
                    return OperationType.Complete;
                }
            }
        }
        return OperationType.None;
    }

    @Override
    public UserType getUserType(String flowId, String taskId, Boolean isFinished, String userId, Node node, DeliverNode deliverNodeFrom) {
        if (isFinished) {
            // 如果已办结
            return getUserType(node, userId, deliverNodeFrom);
        } else {
            // 如果未办结
            WfiTaskPendingDO wfiTaskPending = wfiTaskPendingService.getByTaskId(flowId, taskId, userId);
            if (wfiTaskPending == null) {
                return UserType.None;
            }
            return UserType.getByValue(wfiTaskPending.getUserType());
        }
    }

    @Override
    public Boolean existFlowInstance(String flowCode, String businessKey) {
        return wfiFlowService.count(new QueryWrapper<WfiFlowDO>().eq("FLOW_CODE", flowCode).eq("BUSINESS_KEY", businessKey)) > 0;
    }

    @Override
    public TaskRejectTo getTaskRejectToByRejectToFirst(WfiFlowDO wfiFlow, Flow flow, Node node) {
        TaskRejectTo taskRejectTo = new TaskRejectTo();
        node = FlowUtils.getFirstManualNode(flow);
        taskRejectTo.setNodeName(node.getName());
        List<TaskInstance> taskInstances = listCompletedTasksByFlowInstanceId(wfiFlow.getId());
        if (taskInstances.size() > 0) {
            Map<String, Object> deliverInfoIdFormAll = listTaskVariableValues(taskInstances, TaskVariableName.WFI_DELIVER_ID_FROM);
            // 第一个创建的任务即是第一步
            taskInstances.sort(Comparator.comparing(TaskInstance::getStartTime));
            TaskInstance firstTask = taskInstances.get(0);
            // 第一步节点id
            String firstNodeId = firstTask.getNodeId();
            String deliverInfoIdFrom = (String) deliverInfoIdFormAll.get(firstTask.getId());
            DeliverInfo deliverInfo = queryUtil.getDeliverInfoFrom(deliverInfoIdFrom);
            DeliverNode deliverNode = deliverInfo.getDeliverNodeByNodeId(firstNodeId);
            Node firstNode = flow.getNodeById(firstNodeId);
            for (TaskInstance tempTask : taskInstances) {
                if (StringUtils.equals(tempTask.getNodeId(), firstNodeId)) {
                    if (StringUtils.equals((String) deliverInfoIdFormAll.get(tempTask.getId()), deliverInfoIdFrom)) {
                        UserType userType = getUserType(firstNode, tempTask.getAssignee(), deliverNode);
                        if (UserType.Host.equals(userType)) {
                            taskRejectTo.setTaskId(tempTask.getId());
                            return taskRejectTo;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Collection<List<TaskRejectTo>> listTaskRejectToByRejectToSpecified(WfiFlowDO wfiFlow, Flow flow, Node node, String taskId) {
        if (CollectionUtils.isEmpty(node.getRejectToStep())) {
            throw new RejectToStepNotSpecifiedException();
        }
        List<TaskInstance> taskInstances = listParentTasks(flow, taskId);
        List<String> descLines = queryUtil.getProcessNodeLinesDesc(flow, node.getId());
        Map<Integer, List<TaskRejectTo>> switchTaskRejectTos = new HashMap<>();
        Map<Integer, String> taskRejectToIds = new HashMap<>();
        int i = 1;
        for (String line : descLines) {
            String[] nodeIds = line.split("->");
            List<TaskRejectTo> taskRejectTosT = new ArrayList<>();
            StringJoiner joiner = new StringJoiner("");
            for (String nodeId : nodeIds) {
                if (!node.getRejectToStep().contains(nodeId)) {
                    continue;
                }
                TaskRejectTo taskRejectTo = new TaskRejectTo();
                for (TaskInstance t : taskInstances) {
                    if (Objects.equals(t.getNodeId(), nodeId)) {
                        taskRejectTo.setTaskId(t.getId());
                        taskRejectTo.setNodeName(t.getNodeName());
                    }
                }
                if (StringUtils.isNotBlank(taskRejectTo.getTaskId())) {
                    joiner.add(taskRejectTo.getTaskId());
                    taskRejectTosT.add(taskRejectTo);
                }
            }
            taskRejectToIds.put(i, joiner.toString());
            switchTaskRejectTos.put(i, taskRejectTosT);
            i++;
        }
        //分支去重
        for (Integer key : taskRejectToIds.keySet()) {
            for (Integer keyT = 1; keyT < i && ObjectUtils.notEqual(key, keyT); keyT++) {
                if (Objects.equals(taskRejectToIds.get(key), taskRejectToIds.get(keyT))) {
                    switchTaskRejectTos.remove(key);
                }
            }
        }
        // 增加过滤 防止出现 1、A 2、A-B 这种
        Map<Integer, String> nodeIdLines = new HashMap<>();
        for (Integer index : switchTaskRejectTos.keySet()) {
            List<TaskRejectTo> taskRejectTos = switchTaskRejectTos.get(index);
            String line = "";
            for (TaskRejectTo taskRejectTo : taskRejectTos) {
                line += taskRejectTo.getTaskId();
            }
            nodeIdLines.put(index, line);
        }
        for (Integer index : nodeIdLines.keySet()) {
            for (Integer index2 : nodeIdLines.keySet()) {
                if (ObjectUtils.notEqual(index2, index)) {
                    if (nodeIdLines.get(index2).startsWith(nodeIdLines.get(index))) {
                        switchTaskRejectTos.remove(index);
                    }
                }
            }
        }
        return switchTaskRejectTos.values();
    }

    @Override
    public List<TaskRejectTo> listTaskRejectToByRejectToLast(WfiFlowDO wfiFlow, Flow flow, Node node) {
        // 驳回到上一步
        List<String> descLines = queryUtil.getProcessNodeLinesDesc(wfiFlow.getId(), node.getId());
        // 获取上一步的节点ID
        List<String> lastNodeIds = new ArrayList<>();
        for (String line : descLines) {
            String[] nodeIds = line.split("->");
            lastNodeIds.add(nodeIds[nodeIds.length - 2]);
        }
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(lastNodeIds)) {
            List<TaskRejectTo> taskRejectTosT = new ArrayList<>();
            List<HistoricTaskInstance> taskInstances = getTasksByNodeIds(wfiFlow.getId(), lastNodeIds);
            Set<String> nodeIds = new HashSet<>();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(taskInstances)) {
                for (HistoricTaskInstance t : taskInstances) {
                    String nodeId = t.getName();
                    if (!nodeIds.contains(nodeId)) {
                        TaskRejectTo taskRejectTo = new TaskRejectTo();
                        taskRejectTo.setTaskId(t.getId());
                        taskRejectTo.setNodeName(flow.getNodeById(nodeId).getName());
                        taskRejectTosT.add(taskRejectTo);
                        nodeIds.add(nodeId);
                    }
                }
                return taskRejectTosT;
            }
        }
        return null;
    }

    @Override
    public List<PresetInfo> listAvailablePresetInfosByTaskId(String taskId) {
        Task task = queryUtil.getTaskById(taskId);
        WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(task.getProcessInstanceId());
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(task.getName());
        List<PresetInfo> presetInfos = new ArrayList<>();
        Integer flowVersion = wfiVFlowService.getFlowVersionByFlowId(wfiFlow.getId());
        Boolean allowModifyPresetRouteByDescendant = true;
        if (IntegerUtils.isTrue(node.getPresetedNode())) {
            // 如果当前节点是预设节点，则需要判断预设路径的节点是否允许后续节点修改预设路径
            // 先找到预设流转路径的节点
            Node presetedRouteNode = getPresetedRouteNode(node);
            if (presetedRouteNode == null) {
                throw new PresetedRouteNodeNotFoundException(wfiFlow.getId(), node);
            }
            if (IntegerUtils.isFalse(presetedRouteNode.getAllowModifyPresetRouteByDescendant())) {
                allowModifyPresetRouteByDescendant = false;
            }
        }
        SessionAttribute.initElSession(securityUtils.getSession(), wfiFlow.getId(), flow, wfiFlow.getBusinessKey());
        resolvePresetRoute(wfiFlow.getId(), taskId, flowVersion, presetInfos, node, new ArrayList<>(), allowModifyPresetRouteByDescendant);
        // 标记是否是已预设的
        // 首先检查当前任务是否已预设流程
        WfiRoutePresetDO wfiRoutePreset = wfiRoutePresetService.getByTaskId(taskId);
        if (wfiRoutePreset == null) {
            // 如果当前节点没有流转路径，判断当前节点是不是预设的流转步骤
            if (IntegerUtils.isTrue(node.getPresetedNode())) {
                // 如果当前节点是预设的流转步骤，则向上找预设信息
                wfiRoutePreset = getLatestWfiRoutePreset(wfiFlow, node);
            }
        }
        if (wfiRoutePreset != null) {
            PresetInfo presetInfo = FlowUtils.getPresetInfo(wfiRoutePreset.getPresetInfo());
            // 开始计算哪一个路径是已经被预设的路径
            for (PresetInfo cPresetInfo : presetInfos) {
                // 从presetInfo中找到与cPresetInfo中相同的节点后，两者后续的预设节点必须完全匹配
                List<PresetRoute> tPresetRoutes = new ArrayList<>(presetInfo.getPresetRoutes());
                for (int i = 0; i < tPresetRoutes.size(); ) {
                    PresetRoute tPresetRoute = tPresetRoutes.get(i);
                    if (cPresetInfo.getPresetRoutes().stream().anyMatch(r -> StringUtils.equals(r.getNodeId(), tPresetRoute.getNodeId()))) {
                        break;
                    } else {
                        // 如果当前节点在cPresetInfo中不存在，则直接删除
                        tPresetRoutes.remove(i);
                    }
                }
                // 经过上面方法处理后，tPresetRoutes剩余的节点，必须与cPresetInfo的节点完全匹配
                if (cPresetInfo.getPresetRoutes().size() == tPresetRoutes.size()) {
                    // 首先数量必须完全一致
                    if (cPresetInfo.getPresetRoutes().stream().allMatch(p -> tPresetRoutes.stream().anyMatch(pr -> StringUtils.equals(pr.getNodeId(), p.getNodeId()) && StringUtils.equals(pr.getFlowInLinkId(), p.getFlowInLinkId())))) {
                        cPresetInfo.setPreseted(IntegerUtils.TRUE_VALUE);
                        for (PresetRoute cPresetRoute : cPresetInfo.getPresetRoutes()) {
                            for (PresetRoute presetRoute : presetInfo.getPresetRoutes()) {
                                if (StringUtils.equals(cPresetRoute.getNodeId(), presetRoute.getNodeId())) {
                                    cPresetRoute.setPresetUsers(presetRoute.getPresetUsers());
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return presetInfos;
    }

    @Override
    public List<WfiNodeVO> listWfiNodesByFlowId(String wfiFlowId) {
        WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(wfiFlowId);
        return listWfiNodesByWfiFlow(wfiFlow);
    }

    @Override
    public List<WfiNodeVO> listWfiNodesByBusinessKey(String flowCode, String businessKey) {
        WfiFlowDO wfiFlow = queryUtil.getWfiFlowByBusinessKey(flowCode, businessKey);
        return listWfiNodesByWfiFlow(wfiFlow);
    }

    @Override
    public boolean allowPresetRoute(WfiFlowDO wfiFlow, Node node, String taskId) {
        boolean allowPreset = false;
        // 如果允许预设流程，判断是否已是被预设的节点，如果已是被预设的节点，则需要判断是否允许修改
        if (IntegerUtils.isTrue(node.getAllowPresetRoute())) {
            allowPreset = true;
            if (IntegerUtils.isTrue(node.getPresetedNode())) {
                // 如果是被预设的节点,获取最新的流程预设信息，如果是自己预设的，则允许预设，否则必须允许后续步骤修改才能预设
                WfiRoutePresetDO routePreset = getLatestWfiRoutePreset(wfiFlow, node);
                if (!StringUtils.equals(routePreset.getTaskId(), taskId)) {
                    // 如果不是当前任务将当前节点设置为预设节点的，则获取预设节点信息，并判断是否允许后续节点修改流程预设信息
                    String nodeIdPreset = getNodeIdByTaskId(routePreset.getTaskId());
                    Node nodePreset = node.getFlow().getNodeById(nodeIdPreset);
                    if (IntegerUtils.isFalse(nodePreset.getAllowModifyPresetRouteByDescendant())) {
                        allowPreset = false;
                    }
                }
            }
        }
        return allowPreset;
    }

    @Override
    public boolean flowPreseted(String flowId) {
        return wfiRoutePresetService.countByFlowId(flowId) > 0;
    }

    private List<WfiNodeVO> listWfiNodesByWfiFlow(WfiFlowDO wfiFlow) {
        List<WfiDeliverDO> wfiDelivers = wfiDeliverService.listByFlowId(wfiFlow.getId());
        if (wfiRoutePresetService.count(new QueryWrapper<WfiRoutePresetDO>().eq("flow_id", wfiFlow.getId())) == 0) {
            throw new WfiRoutePresetNotFoundByFlowIdException(wfiFlow.getId());
        }
//        List<WfiRoutePresetDO> wfiRoutePresets = wfiRoutePresetService.listByFlowId(wfiFlow.getId());
        List<HistoricTaskInstance> tasks = queryUtil.listHistoricTaskInstances(wfiFlow.getId());
//        List<WfiTaskPendingDO> wfiTaskPendings = wfiTaskPendingService.listByFlowId(wfiFlow.getId());
        Map<String, Object> wfiDeliverIdFromMap = queryUtil.listTaskVariableValues(tasks, TaskVariableName.WFI_DELIVER_ID_FROM);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        SessionAttribute.initElSession(securityUtils.getSession(), wfiFlow.getId(), flow, wfiFlow.getBusinessKey());
        Integer secretLevel = FlowUtils.getSecretLevel(flow, wfdFlowElService);
        List<WfiNodeVO> wfiNodes = new ArrayList<>();
        WfiNodeVO wfiNode;
//        List<WfiNodeUserVO> wfiNodeUsers;
//        WfiNodeUserVO wfiNodeUser;
        if (wfiDelivers.size() > 0) {
            // 如果已存在办理信息，则表示已发起流程，不需要遍历所有开始节点，否则需要遍历所有开始节点
            // 创建流程实例的办理信息
            WfiDeliverDO wfiDeliverFrom = wfiDelivers.get(0);
            DeliverInfo deliverInfoFrom = FlowUtils.getDeliverInfo(wfiDeliverFrom);
            Node node;
            for (DeliverNode deliverNode : deliverInfoFrom.getDeliverNodes()) {
                wfiNode = new WfiNodeVO();
                node = flow.getNodeById(deliverNode.getNodeId());
                resolveWfiNode(wfiNode, node);
                resolveWfiNodeUser(wfiNode, deliverNode);
                wfiNodes.add(wfiNode);
                resolveFlowOutNodes(tasks, wfiDelivers, wfiDeliverIdFromMap, wfiNodes, node, secretLevel);
            }
        } else {
            // 首先找出开始节点，然后从开始节点往下流转，直到结束节点
            for (Node node : flow.getNodes()) {
                if (NodeType.Start.isEquals(node.getNodeType())) {
                    resolveFlowOutNodes(tasks, wfiDelivers, wfiDeliverIdFromMap, wfiNodes, node, secretLevel);
                }
            }
        }
        return wfiNodes;
    }

    private void resolveFlowOutNodes(List<HistoricTaskInstance> tasks, List<WfiDeliverDO> wfiDelivers, Map<String, Object> wfiDeliverIdFromMap, List<WfiNodeVO> wfiNodes, Node node, Integer secretLevel) {
        WfiNodeVO wfiNode;
        // 如果有流出分支创建了任务，则只走创建了任务的分支
        List<Link> flowOutLinks = new ArrayList<>();
        for (HistoricTaskInstance task : tasks) {
            if (flowOutLinks.stream().anyMatch(l -> StringUtils.equals(task.getName(), l.getToNodeId()))) {
                continue;
            }
            for (Link link : node.getFlowOutLinks()) {
                if (StringUtils.equals(task.getName(), link.getToNodeId())) {
                    flowOutLinks.add(link);
                }
            }
        }
        if (flowOutLinks.size() == 0) {
            // 如果有流出分支是预设的流转分支，则只走预设的流转分支
            flowOutLinks = node.getFlowOutLinks().stream().filter(o -> IntegerUtils.isTrue(o.getPresetedLink())).collect(Collectors.toList());
            if (flowOutLinks.size() == 0) {
                flowOutLinks = node.getFlowOutLinks();
            }
        }
        for (Link link : flowOutLinks) {
            if (wfiNodes.stream().anyMatch(o -> StringUtils.equals(o.getNodeId(), link.getToNodeId()))) {
                // 如果已添加过，则不继续添加
                continue;
            }
            if (NodeType.End.isEquals(link.getToNode().getNodeType())) {
                wfiNode = new WfiNodeVO();
                resolveWfiNode(wfiNode, link.getToNode());
                wfiNodes.add(wfiNode);
            } else if (NodeType.Start.isEquals(link.getToNode().getNodeType())) {
                continue;
            } else if (NodeType.Manual.isEquals(link.getToNode().getNodeType())) {
                wfiNode = new WfiNodeVO();
                resolveWfiNode(wfiNode, link.getToNode());
                // 需要分为两种情况处理，如果是没生成任务，则根据规则计算，否则根据来自转交信息判断办理人
                // 当前节点的任务列表
                List<HistoricTaskInstance> cTasks = tasks.stream().filter(o -> StringUtils.equals(o.getName(), link.getToNodeId())).collect(Collectors.toList());
                if (cTasks.size() > 0) {
                    // 创建的最后一个任务
                    HistoricTaskInstance lastTask = cTasks.get(cTasks.size() - 1);
                    WfiDeliverDO wfiDeliverFrom = wfiDelivers.stream().filter(o -> Objects.equals(o.getId(), wfiDeliverIdFromMap.get(lastTask.getId()))).findAny().orElse(null);
                    if (wfiDeliverFrom != null) {
                        DeliverInfo deliverInfoFrom = FlowUtils.getDeliverInfo(wfiDeliverFrom);
                        DeliverNode deliverNodeFrom = deliverInfoFrom.getDeliverNodeByNodeId(link.getToNodeId());
                        resolveWfiNodeUser(wfiNode, deliverNodeFrom);
                    }
                } else {
                    resolveWfiNodeUser(wfiNode, link.getToNode(), secretLevel);
                }
                wfiNodes.add(wfiNode);
            } else if (NodeType.Free.isEquals(link.getToNode().getNodeType())) {
                DeliverNode deliverNode = null;
                WfiDeliverDO wfiDeliverFrom;
                DeliverInfo deliverInfoFrom;
                boolean taskExists;
                for (NodeFreeStep nodeFreeStep : link.getToNode().getNodeFreeSteps()) {
                    wfiNode = new WfiNodeVO();
                    resolveWfiNode(wfiNode, nodeFreeStep);
                    taskExists = false;
                    // 分为两种情况处理，如果是没生成任务，则根据规则计算，否则根据来自转交信息判断办理人
                    for (HistoricTaskInstance task : tasks) {
                        if (StringUtils.equals(link.getToNodeId(), task.getName())) {
                            // 如果是当前节点的任务
                            wfiDeliverFrom = wfiDelivers.stream().filter(o -> Objects.equals(o.getId(), wfiDeliverIdFromMap.get(task.getId()))).findAny().orElse(null);
                            if (wfiDeliverFrom != null) {
                                deliverInfoFrom = FlowUtils.getDeliverInfo(wfiDeliverFrom);
                                deliverNode = deliverInfoFrom.getDeliverNodeByNodeId(link.getToNodeId());
                                if (StringUtils.equals(deliverNode.getNodeFreeStepId(), nodeFreeStep.getId())) {
                                    taskExists = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (taskExists) {
                        resolveWfiNodeUser(wfiNode, deliverNode);
                    } else {
                        resolveWfiNodeUser(wfiNode, nodeFreeStep);
                    }
                    wfiNodes.add(wfiNode);
                }
            }
            resolveFlowOutNodes(tasks, wfiDelivers, wfiDeliverIdFromMap, wfiNodes, link.getToNode(), secretLevel);
        }
    }

    private void resolveWfiNode(WfiNodeVO wfiNode, Node node) {
        wfiNode.setNodeId(node.getId());
        wfiNode.setNodeCode(node.getCode());
        wfiNode.setNodeName(node.getName());
    }

    private void resolveWfiNode(WfiNodeVO wfiNode, NodeFreeStep nodeFreeStep) {
        wfiNode.setNodeFreeStepId(nodeFreeStep.getId());
        resolveWfiNode(wfiNode, nodeFreeStep.getNode());
    }

    private void resolveWfiNodeUser(WfiNodeVO wfiNode, DeliverUser deliverUser) {
        WfiNodeUserVO wfiNodeUser;
        wfiNodeUser = new WfiNodeUserVO();
        wfiNodeUser.setSortIndex(deliverUser.getSortIndex());
        wfiNodeUser.setUserId(deliverUser.getUserId());
        wfiNodeUser.setRealName(deliverUser.getRealName());
        wfiNode.getNodeUsers().add(wfiNodeUser);
    }

    private void resolveWfiNodeUser(WfiNodeVO wfiNode, DeliverNode deliverNode) {
        wfiNode.setNodeUsers(new ArrayList<>());
        for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
            if (deliverUser.getDelegateMap().size() > 0) {
                // 委托给了别人，则继续
                continue;
            }
            resolveWfiNodeUser(wfiNode, deliverUser);
        }
    }

    private void resolveWfiNodeUser(WfiNodeVO wfiNode, NodeFreeStep nodeFreeStep) {
        wfiNode.setNodeUsers(new ArrayList<>());
        for (DeliverUser deliverUser : nodeFreeStep.getDeliverUsers()) {
            if (deliverUser.getDelegateMap().size() > 0) {
                // 委托给了别人，则继续
                continue;
            }
            resolveWfiNodeUser(wfiNode, deliverUser);
        }
    }

    private void resolveWfiNodeUser(WfiNodeVO wfiNode, Node node, Integer secretLevel) {
        WfiNodeUserVO wfiNodeUser;
        wfiNode.setNodeUsers(new ArrayList<>());
        // 如果是预设的流程节点，则从流程预设中预设的信息中取办理人员信息，如果流程预设里没有指定办理人员信息，则根据获取流程节点的办理人
        if (IntegerUtils.isTrue(node.getPresetedNode()) && node.getNodePresetUsers().size() > 0) {
            for (NodePresetUser presetUser : node.getNodePresetUsers()) {
                wfiNodeUser = new WfiNodeUserVO();
                wfiNodeUser.setSortIndex(presetUser.getSortIndex());
                wfiNodeUser.setUserId(presetUser.getUserId());
                wfiNodeUser.setRealName(presetUser.getRealName());
                wfiNode.getNodeUsers().add(wfiNodeUser);
            }
        } else {
            List<StepUser> stepUsers = FlowUtils.listStepUsersByNode(wfdCollectionUtils, node, false, secretLevel);
            for (StepUser stepUser : stepUsers) {
                wfiNodeUser = new WfiNodeUserVO();
                wfiNodeUser.setSortIndex(stepUser.getSortIndex());
                wfiNodeUser.setUserId(stepUser.getUserId());
                wfiNodeUser.setRealName(stepUser.getRealName());
                wfiNode.getNodeUsers().add(wfiNodeUser);
            }
        }
    }

    @Override
    public WfiRoutePresetDO getLatestWfiRoutePreset(WfiFlowDO wfiFlow, Node node) {
        List<HistoricTaskInstance> pTasks;
        List<String> pTaskIds;
        List<WfiRoutePresetDO> pWfiRoutePresets;
        for (Link flowInLink : node.getFlowInLinks()) {
            if (IntegerUtils.isTrue(flowInLink.getPresetedLink())) {
                // 如果当前流入分支是预设的分支
                pTasks = queryUtil.listHistoricTaskInstancesByNodeId(wfiFlow.getId(), flowInLink.getFromNodeId());
                if (pTasks.size() > 0) {
                    pTaskIds = pTasks.stream().map(HistoricTaskInstance::getId).collect(Collectors.toList());
                    pWfiRoutePresets = wfiRoutePresetService.list(new QueryWrapper<WfiRoutePresetDO>().in("task_id", pTaskIds).orderByDesc("preset_time"));
                    if (pWfiRoutePresets.size() > 0) {
                        return pWfiRoutePresets.get(0);
                    }
                }
            }
        }
        for (Link flowInLink : node.getFlowInLinks()) {
            if (IntegerUtils.isTrue(flowInLink.getPresetedLink())) {
                return getLatestWfiRoutePreset(wfiFlow, flowInLink.getFromNode());
            }
        }
        throw new WfiRoutePresetNotFoundByPresetedNodeException(wfiFlow, node);
    }

    /**
     * 获取预设流程步骤的节点
     *
     * @param node 预设路径中的节点
     * @return 预设流程步骤的节点
     * @author JonnyJiang
     * @date 2020/12/18 0:03
     */

    private Node getPresetedRouteNode(Node node) {
        for (Link link : node.getFlowInLinks()) {
            if (IntegerUtils.isTrue(link.getPresetedLink())) {
                // 如果当前流入分支是预设的流转分支
                if (IntegerUtils.isTrue(link.getFromNode().getPresetedRoute())) {
                    // 如果当前流入节点预设了流转分支
                    return link.getFromNode();
                } else if (IntegerUtils.isTrue(link.getFromNode().getPresetedNode())) {
                    // 如果当前流入节点是预设的流转节点
                    return getPresetedRouteNode(link.getFromNode());
                }
            }
        }
        return null;
    }

    /**
     * 处理所有后续可能路径
     *
     * @param wfiFlowId                          流程实例id
     * @param taskId                             任务id
     * @param flowVersion                        流程实例版本
     * @param presetInfos                        可能路径列表
     * @param node                               节点
     * @param presetRoutes                       当前节点来自路径列表
     * @param allowModifyPresetRouteByDescendant 是否允许后续节点修改流转路径
     * @author JonnyJiang
     * @date 2020/9/23 23:38
     */

    private void resolvePresetRoute(String wfiFlowId, String taskId, Integer flowVersion, List<PresetInfo> presetInfos, Node node, List<PresetRoute> presetRoutes, Boolean allowModifyPresetRouteByDescendant) {
        if (NodeType.End.isEquals(node.getNodeType())) {
            PresetInfo presetInfo = new PresetInfo();
            presetInfo.setFlowId(wfiFlowId);
            presetInfo.setTaskId(taskId);
            presetInfo.setFlowVersion(flowVersion);
            presetInfo.setPresetRoutes(presetRoutes);
            presetInfos.add(presetInfo);
        } else {
            RuleUtils ruleUtils = SpringContextUtils.getBean(RuleUtils.class);
            for (Link link : node.getFlowOutLinks()) {
                // 首先判断当前节点是否已出现过，如果已出现过，则不继续添加
                if (presetRoutes.stream().anyMatch(o -> StringUtils.equals(o.getNodeId(), link.getToNodeId()))) {
                    continue;
                }
                Map<String, Integer> workResultMap = FlowUtils.getWorkResultMap(link.getFromNode(), new ArrayList<>(), new ArrayList<>());
                if (FlowUtils.linkAvailable(ruleUtils, link, link.getFlow(), workResultMap)) {
                    if (!IntegerUtils.isTrue(link.getPresetedLink())) {
                        // 如果当前分支不是预设的流转分支
                        if (!allowModifyPresetRouteByDescendant) {
                            // 如果不允许修改预设的流转分支
                            continue;
                        }
                    }
                    List<PresetRoute> routes = new ArrayList<>(presetRoutes);
                    PresetRoute presetRoute = new PresetRoute();
                    presetRoute.setSortIndex(routes.size());
                    presetRoute.setFlowInLinkId(link.getId());
                    presetRoute.setNodeId(link.getToNodeId());
                    presetRoute.setForceSequence(link.getToNode().getForceSequence());
                    routes.add(presetRoute);
                    resolvePresetRoute(wfiFlowId, taskId, flowVersion, presetInfos, link.getToNode(), routes, allowModifyPresetRouteByDescendant);
                }
            }
        }
    }

    @Override
    public R addReviewFile(String formId) {
        return ifile.addReview(formId);
    }
    @Override
    public R setReview(String formId) {
        return ifile.setReview(formId);
    }
}