package com.csicit.ace.bpm.activiti.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.DbUpdateTask;
import com.csicit.ace.bpm.activiti.BpmConfiguration;
import com.csicit.ace.bpm.activiti.TaskVariableName;
import com.csicit.ace.bpm.activiti.WfiDeliverTasks;
import com.csicit.ace.bpm.activiti.impl.v7v1v81.History;
import com.csicit.ace.bpm.enums.*;
import com.csicit.ace.bpm.exception.*;
import com.csicit.ace.bpm.pojo.domain.*;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.DeliverNode;
import com.csicit.ace.bpm.pojo.vo.DeliverUser;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Link;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.service.*;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.interfaces.service.ISecurity;
import com.csicit.ace.interfaces.service.IUser;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JonnyJiang
 * @date 2019/12/6 17:10
 */
@Component
public class QueryUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryUtil.class);
    /**
     * 不进行筛选
     */
    public static final String FILTER_ALL = "ALL";
    @Autowired
    private WfdVFlowService wfdVFlowService;
    @Autowired
    WfdFlowService wfdFlowService;
    @Autowired
    private WfiFlowService wfiFlowService;
    @Autowired
    private WfiDeliverService wfiDeliverService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private WfiBackupService wfiBackupService;
    @Autowired
    private WfiTaskPendingService wfiTaskPendingService;
    @Autowired
    private SpringProcessEngineConfiguration springProcessEngineConfiguration;
    @Autowired
    private ISecurity iSecurity;
    @Autowired
    private IUser iUser;
    @Autowired
    private WfiRoutePresetService wfiRoutePresetService;

    /**
     * 根据任务主键获取流程定义主键
     *
     * @param taskId
     * @return
     * @author FourLeaves
     * @date 2020/4/10 9:18
     */
    public String getWfdIdByTaskId(String taskId) {
        HistoricTaskInstance taskInstance = getHistoricTaskInstanceQuery()
                .taskId(taskId).singleResult();
        if (taskInstance != null) {
            String processInstacneId = taskInstance.getProcessInstanceId();
            if (StringUtils.isNotBlank(processInstacneId)) {
                WfiFlowDO wfiFlowDO = wfiFlowService.getById(processInstacneId);
                if (wfiFlowDO != null) {
                    return wfiFlowDO.getFlowId();
                }
            }
        }
        return null;

    }


    /**
     * 根据流程定义id获取生效的流程 版本
     *
     * @param flowId 流程标识
     * @return com.csicit.ace.bpm.pojo.domain.WfdVFlowDO
     * @author JonnyJiang
     * @date 2019/9/23 14:02
     */

    public WfdVFlowDO getEffectiveWfdVFlowByFlowId(String flowId) {
        WfdVFlowDO wfdVFlow = wfdVFlowService.getEffectiveByFlowId(flowId, LocalDateTime.now());
        if (wfdVFlow == null) {
            throw new BpmException(LocaleUtils.getEffectiveWfdVFlowNotFoundByFlowId(flowId));
        }
        return wfdVFlow;
    }

    public ProcessInstanceQuery getProcessInstanceQuery() {
        return runtimeService.createProcessInstanceQuery().processInstanceTenantId(securityUtils.getAppName());
    }

    public ProcessInstanceQuery getProcessInstanceQuery(String processInstanceId) {
        return getProcessInstanceQuery().processInstanceId(processInstanceId);
    }

    public ProcessInstance getProcessInstance(String processInstanceId) {
        return getProcessInstanceQuery(processInstanceId).singleResult();
    }

    public HistoricProcessInstanceQuery getHistoricProcessInstanceQuery() {
        return historyService.createHistoricProcessInstanceQuery().processInstanceTenantId(securityUtils.getAppName());
    }

    public HistoricProcessInstanceQuery getHistoricProcessInstanceQuery(String processInstanceId) {
        return getHistoricProcessInstanceQuery().processInstanceId(processInstanceId);
    }

    public HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
        return getHistoricProcessInstanceQuery(processInstanceId).singleResult();
    }

    public HistoricActivityInstanceQuery getHistoricActivityInstanceQuery() {
        return historyService.createHistoricActivityInstanceQuery().activityTenantId(securityUtils.getAppName());
    }

    public HistoricActivityInstanceQuery getHistoricActivityInstanceQuery(String processInstanceId) {
        return getHistoricActivityInstanceQuery().processInstanceId(processInstanceId);
    }

    public HistoricVariableInstanceQuery getHistoricVariableInstanceQuery() {
        return historyService.createHistoricVariableInstanceQuery();
    }

    public List<HistoricTaskInstance> listHistoricTaskInstances(String processInstanceId) {
        return getHistoricTaskInstanceQuery(processInstanceId).orderByTaskCreateTime().asc().list();
    }

    public List<HistoricTaskInstance> listHistoricTaskInstances(String processInstanceId, String wfiDeliverIdFrom, String nodeId) {
        List<HistoricTaskInstance> tasks = new ArrayList<>();
        if (StringUtils.isNotEmpty(wfiDeliverIdFrom)) {
            List<HistoricTaskInstance> nodeTasks = getHistoricTaskInstanceQuery(processInstanceId).taskName(nodeId).list();
            if (nodeTasks.size() > 0) {
                Map<String, Object> deliverIdFromMap = listTaskVariableValues(nodeTasks.stream().map(HistoricTaskInstance::getId).collect(Collectors.toSet()), TaskVariableName.WFI_DELIVER_ID_FROM);
                for (HistoricTaskInstance nodeTask : nodeTasks) {
                    if (wfiDeliverIdFrom.equals(deliverIdFromMap.get(nodeTask.getId()))) {
                        tasks.add(nodeTask);
                    }
                }
            }
        }
        return tasks;
    }

    public TaskQuery getTaskQuery() {
        return taskService.createTaskQuery().taskTenantId(securityUtils.getAppName());
    }

    public TaskQuery getTaskQuery(String processInstanceId) {
        return getTaskQuery().processInstanceId(processInstanceId);
    }

    public List<Task> listTasks(String processInstanceId) {
        return getTaskQuery(processInstanceId).list();
    }

    public HistoricTaskInstance getHistoricTaskInstance(String id) {
        HistoricTaskInstance historicTaskInstance = getHistoricTaskInstanceQuery().taskId(id).singleResult();
        if (historicTaskInstance == null) {
            throw new BpmException(LocaleUtils.getHistoricTaskInstanceNotFound(id));
        }
        return historicTaskInstance;
    }

    public HistoricTaskInstanceQuery getListTaskMineCompletedByFlowCodeQuery(String flowCode) {
        String currentUserId = securityUtils.getCurrentUserId();
        HistoricTaskInstanceQuery query = getHistoricTaskInstanceQuery().taskAssignee(currentUserId).taskCompletedBefore(new Date());
        if (!FILTER_ALL.equals(flowCode)) {
            query.processDefinitionName(flowCode);
        }
        return query.orderByHistoricTaskInstanceEndTime().desc();
    }

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public HistoricTaskInstanceQuery getListTaskMineByParamsQuery(Map<String, String> params) {
        /**
         * 流程标识
         */
        String flowCode = params.get("flowCode");
        /**
         * 流程类型
         */
        String flowType = params.get("flowType");
        /**
         * 检索内容
         */
        String searchStr = params.get("searchStr");
        /**
         * 流程发起日期起始
         */
        String startDate = params.get("startDate");
        /**
         * 流程发起日期截止
         */
        String endDate = params.get("endDate");
        String currentUserId = securityUtils.getCurrentUserId();
        /**
         * 根据文号找出定义的ID
         */
        List<String> wfiFlowIds = new ArrayList<>();
        if (StringUtils.isNotBlank(searchStr)) {
            wfiFlowIds = wfiFlowService.list(new QueryWrapper<WfiFlowDO>()
                    .like("FLOW_NO", searchStr).select("id")).stream().map(WfiFlowDO::getId).collect(Collectors.toList());
        }
        HistoricTaskInstanceQuery query = getHistoricTaskInstanceQuery().taskAssignee(currentUserId).taskCompletedBefore(new Date())
                .orderByHistoricTaskInstanceEndTime().desc();
        /**
         * 显示所有流程
         */
        if (StringUtils.isNotBlank(flowCode) && !QueryUtil.FILTER_ALL.equals(flowCode)) {
            List<String> wfdIds = new ArrayList<>();
            if (Objects.equals(flowType, "categoryNode")) {
                wfdIds = wfdFlowService.list(new QueryWrapper<WfdFlowDO>().select("id").eq("CATEGORY_ID", flowCode)).stream().map(WfdFlowDO::getId).collect(Collectors.toList());
            } else {
                wfdIds.add(flowCode);
            }
            if (CollectionUtils.isNotEmpty(wfdIds)) {
                List<String> wfiIds = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().select("id").in("FLOW_ID", wfdIds)).stream().map(WfiFlowDO::getId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(wfdIds)) {
                    if (StringUtils.isNotBlank(searchStr) && CollectionUtils.isNotEmpty(wfiFlowIds)) {
                        wfiFlowIds.retainAll(wfiIds);
                    } else if (StringUtils.isBlank(searchStr)) {
                        wfiFlowIds.addAll(wfiIds);
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
//            else {
//
////                List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().processDefinitionCategory(flowCode).list();
////                if (CollectionUtils.isNotEmpty(definitions)) {
////                    query.processDefinitionId(definitions.get(0).getId());
////                } else {
////                    return null;
////                }
//            }
        }
        try {
            Calendar c = Calendar.getInstance();
            if (StringUtils.isNotBlank(startDate)) {
                query.taskCompletedAfter(simpleDateFormat.parse(startDate));
            }
            if (StringUtils.isNotBlank(endDate)) {
                c.setTime(simpleDateFormat.parse(endDate));
                c.add(Calendar.DAY_OF_MONTH, 1);
                query.taskCompletedBefore(c.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotBlank(searchStr) || CollectionUtils.isNotEmpty(wfiFlowIds)) {
            if (CollectionUtils.isEmpty(wfiFlowIds)) {
                return null;
            }
            query.processInstanceIdIn(wfiFlowIds);
        }
        return query;
    }

    private List<String> listWfiFlowIdsByParams(String flowCode, String flowType, String searchStr, String currentUserId) {
        List<String> wfiFlowIds = new ArrayList<>();
        if (StringUtils.isNotBlank(searchStr)) {
            wfiFlowIds = wfiFlowService.list(new QueryWrapper<WfiFlowDO>()
                    .like("FLOW_NO", searchStr).select("id")).stream().map(WfiFlowDO::getId).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(flowCode) && !QueryUtil.FILTER_ALL.equals(flowCode)) {
            List<String> wfdIds = new ArrayList<>();
            if (Objects.equals(flowType, "categoryNode")) {
                wfdIds = wfdFlowService.list(new QueryWrapper<WfdFlowDO>().select("id").eq("CATEGORY_ID", flowCode)).stream().map(WfdFlowDO::getId).collect(Collectors.toList());
            } else {
                wfdIds.add(flowCode);
            }
            if (CollectionUtils.isNotEmpty(wfdIds)) {
                List<String> wfiIds = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().select("id").in("FLOW_ID", wfdIds)).stream().map(WfiFlowDO::getId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(wfiIds)) {
                    if (StringUtils.isNotBlank(searchStr) && CollectionUtils.isNotEmpty(wfiFlowIds)) {
                        wfiFlowIds.retainAll(wfiIds);
                    } else if (StringUtils.isBlank(searchStr)) {
                        wfiFlowIds.addAll(wfiIds);
                    }
                } else {
                    return wfiFlowIds;
                }
            } else {
                return wfiFlowIds;
            }
        }
        return wfiFlowIds;
    }

    public TaskQuery getListTaskMineByParams(Map<String, String> params) {
        /**
         * 流程标识
         */
        String flowCode = params.get("flowCode");
        /**
         * 流程类型
         */
        String flowType = params.get("flowType");
        /**
         * 检索内容
         */
        String searchStr = params.get("searchStr");
        String currentUserId = securityUtils.getCurrentUserId();
        TaskQuery query = getTaskQuery().taskAssignee(currentUserId).orderByTaskCreateTime().desc();
        /**
         * 根据文号找出定义的ID
         */
        List<String> wfiFlowIds = listWfiFlowIdsByParams(flowCode, flowType, searchStr, currentUserId);
        if (StringUtils.isNotBlank(searchStr) || CollectionUtils.isNotEmpty(wfiFlowIds)) {
            if (CollectionUtils.isEmpty(wfiFlowIds)) {
                return null;
            }
            query.processInstanceIdIn(wfiFlowIds);
        }
        return query;
    }

    public QueryWrapper<WfiTaskPendingDO> getWfiTaskPendingQueryWrapperByParams(Map<String, String> params) {

        /**
         * 流程标识
         */
        String flowCode = params.get("flowCode");
        /**
         * 流程类型
         */
        String flowType = params.get("flowType");
        /**
         * 检索内容
         */
        String searchStr = params.get("searchStr");
        String currentUserId = securityUtils.getCurrentUserId();
        /**
         * 根据文号找出定义的ID
         */
        List<String> wfiFlowIds = listWfiFlowIdsByParams(flowCode, flowType, searchStr, currentUserId);
        QueryWrapper<WfiTaskPendingDO> query = new QueryWrapper<WfiTaskPendingDO>().eq("user_id", currentUserId);
        if (StringUtils.isNotBlank(searchStr) || CollectionUtils.isNotEmpty(wfiFlowIds)) {
            if (CollectionUtils.isEmpty(wfiFlowIds)) {
                return null;
            }
            query.in("flow_id", wfiFlowIds);
        }
        return query.orderByDesc("create_time");
    }

    /**
     * 已委托的任务
     *
     * @return
     * @author FourLeaves
     * @date 2020/2/18 14:49
     */
    public HistoricTaskInstanceQuery getListTaskDelegatedQuery() {
        String currentUserId = securityUtils.getCurrentUserId();
        return getHistoricTaskInstanceQuery()
                .taskOwner(currentUserId);
        //.taskCompletedBefore(new Date())
        //.orderByHistoricTaskInstanceEndTime()
        //.desc();
    }

    public TaskQuery getListTaskMineByFlowCode(String flowCode) {
        String currentUserId = securityUtils.getCurrentUserId();
        TaskQuery query = getTaskQuery().taskAssignee(currentUserId);
        if (!FILTER_ALL.equals(flowCode)) {
            query.processDefinitionName(flowCode);
        }
        return query.orderByTaskCreateTime().desc();
    }

    public QueryWrapper<WfiTaskPendingDO> getListTaskMineQueryByFlowCode(String flowCode) {
        String currentUserId = securityUtils.getCurrentUserId();
        QueryWrapper<WfiTaskPendingDO> queryWrapper = new QueryWrapper<WfiTaskPendingDO>().eq("app_id", securityUtils.getAppName()).eq("user_id", currentUserId);
        if (!FILTER_ALL.equals(flowCode)) {
            queryWrapper.eq("flow_code", flowCode);
        }
        return queryWrapper.orderByDesc("create_time");
    }

    public Task getTaskById(String taskId) {
        Task task = getTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new TaskNotFoundByIdException(taskId);
        }
        return task;
    }

    /**
     * 获取流程实例
     *
     * @param id 流程实例id
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/12/10 17:54
     */

    public WfiFlowDO getWfiFlowById(String id) {
        WfiFlowDO wfiFlow = wfiFlowService.getById(id);
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByIdException(id);
        }
        return wfiFlow;
    }

    /**
     * 获取流程实例
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/12/31 10:23
     */

    public WfiFlowDO getWfiFlowByBusinessKey(String flowCode, String businessKey) {
        WfiFlowDO wfiFlow = wfiFlowService.getByCode(flowCode, businessKey);
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByCodeException(flowCode, businessKey);
        }
        return wfiFlow;
    }

    /**
     * 获取上级节点名称列表
     *
     * @param node 当前节点
     * @return 节点名称列表
     * @author JonnyJiang
     * @date 2019/12/10 19:29
     */

    public void listParentNodeIds(List<String> parentNodeIds, Node node) {
        for (Link link : node.getFlowInLinks()) {
            if (NodeType.Manual.isEquals(link.getFromNode().getNodeType()) || NodeType.Manual.isEquals(link.getFromNode().getNodeType())) {
                if (!parentNodeIds.contains(link.getFromNodeId())) {
                    parentNodeIds.add(link.getFromNodeId());
                    listParentNodeIds(parentNodeIds, link.getFromNode());
                }
            } else if (NodeType.Subflow.isEquals(link.getFromNode().getNodeType())) {
                // TODO: 2020/11/23 暂不支持
                // 此处需要添加子流程的节点id，暂不支持
                for (Link l : node.getFlowInLinks()) {
                    listParentNodeIds(parentNodeIds, l.getFromNode());
                }
            }
        }
    }


    /**
     * 根据最新节点   逆序  寻找所有分支
     * nodeId1->nodeId2->nodeId2->nodeId
     *
     * @param wfiFlowId
     * @param nodeId
     * @return
     * @author FourLeaves
     * @date 2019/12/20 11:48
     */
    public List<String> getProcessNodeLinesDesc(String wfiFlowId, String nodeId) {
        WfiFlowDO wfiFlow = getWfiFlowById(wfiFlowId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        return getProcessNodeLinesDesc(flow, nodeId);
    }

    /**
     * 根据最新节点   逆序  寻找所有分支
     * nodeId1->nodeId2->nodeId2->nodeId
     *
     * @param flow
     * @param nodeId
     * @return
     * @author FourLeaves
     * @date 2019/12/20 11:48
     */
    public List<String> getProcessNodeLinesDesc(Flow flow, String nodeId) {
        List<Link> links = flow.getLinks();
        Set<String> set = new HashSet<>();
        set.add(nodeId);
        addProcessNodeLinesDesc(nodeId, links, set);
        List<String> list = new ArrayList<>();
        for (String line : set) {
            if (set.stream().filter(lineT -> lineT.endsWith(line)).collect(Collectors.toList()).size() == 1) {
                list.add(line);
            }
        }
        return list;
    }


    /**
     * 递归获取line 倒序
     *
     * @param toNodeId
     * @param links
     * @param set
     * @return
     * @author FourLeaves
     * @date 2019/12/20 11:03
     */
    public void addProcessNodeLinesDesc(String toNodeId, List<Link> links, Set<String> set) {
        List<String> lines = set.stream().filter(line -> line.startsWith(toNodeId)).collect(Collectors.toList());
        for (Link link : links) {
            if (Objects.equals(toNodeId, link.getToNodeId())) {
                String formNodeId = link.getFromNodeId();
                for (String line : lines) {
                    set.add(formNodeId + "->" + line);
                }
                addProcessNodeLinesDesc(formNodeId, links, set);
            }
        }
    }

    /**
     * 获取由工作流各个节点分支构成的一长串字符串，例如：
     * nodeid1->nodeid2->nodeid3
     *
     * @param wfiFlowId
     * @return
     * @author FourLeaves
     * @date 2019/12/20 10:24
     */
    public List<String> getProcessNodeLines(String wfiFlowId) {
        WfiFlowDO wfiFlow = getWfiFlowById(wfiFlowId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        return getProcessNodeLines(flow);
    }

    /**
     * 获取由工作流各个节点分支构成的一长串字符串，例如：
     * nodeid1->nodeid2->nodeid3
     *
     * @param flow
     * @return
     * @author FourLeaves
     * @date 2019/12/20 10:24
     */
    public List<String> getProcessNodeLines(Flow flow) {
        List<Link> links = flow.getLinks();
        List<String> list = new ArrayList<>();
        List<Node> firstNodes = FlowUtils.getFirstManualNodes(flow);
        for (Node firstNode : firstNodes) {
            Set<String> set = new HashSet<>();
            set.add(firstNode.getId());
            addProcessNodeLines(firstNode.getId(), links, set);
            for (String line : set) {
                if (set.stream().filter(lineT -> lineT.startsWith(line)).collect(Collectors.toList()).size() == 1) {
                    list.add(line);
                }
            }
        }
        return list;
    }

    /**
     * 递归获取line
     *
     * @param fromNodeId
     * @param links
     * @param set
     * @return
     * @author FourLeaves
     * @date 2019/12/20 11:03
     */
    public void addProcessNodeLines(String fromNodeId, List<Link> links, Set<String> set) {
        // List<String> fromNodeIds = new ArrayList<>();
        resolveProcessNodeLines(fromNodeId, links, set);
    }

    private void resolveProcessNodeLines(String fromNodeId, List<Link> links, Set<String> set) {
//        if (!fromNodeIds.contains(fromNodeId)) {
//            fromNodeIds.add(fromNodeId);
        List<String> lines = set.stream().filter(line -> line.endsWith(fromNodeId)).collect(Collectors.toList());
        for (Link link : links) {
            if (Objects.equals(fromNodeId, link.getFromNodeId())) {
                String toNodeId = link.getToNodeId();
                for (String line : lines) {
                    set.add(line + "->" + toNodeId);
                }
                resolveProcessNodeLines(toNodeId, links, set);
            }
        }
        //}
    }

    /**
     * 获取下级节点id列表
     *
     * @param node 当前节点
     * @return 节点id列表
     * @author JonnyJiang
     * @date 2019/12/10 19:40
     */

    public List<String> listChildNodeIds(Node node) {
        List<String> childNodeIds = new ArrayList<>();
        for (Link link : node.getFlowOutLinks()) {
            if (NodeType.Manual.isEquals(link.getToNode().getNodeType()) || NodeType.Free.isEquals(link.getToNode().getNodeType())) {
                childNodeIds.add(link.getToNodeId());
                childNodeIds.addAll(listChildNodeIds(link.getToNode()));
            } else if (NodeType.Subflow.isEquals(link.getToNode().getNodeType())) {
                // 此处需要添加子流程的节点id，暂不支持
                for (Link l : node.getFlowOutLinks()) {
                    childNodeIds.addAll(listChildNodeIds(l.getToNode()));
                }
            }
        }
        return childNodeIds;
    }

    /**
     * 获取下级节点列表
     *
     * @param node
     * @return java.util.List<com.csicit.ace.bpm.pojo.vo.wfd.Node>
     * @author JonnyJiang
     * @date 2019/12/10 20:13
     */

    public List<Node> listChildNodes(Node node) {
        List<Node> childNodes = new ArrayList<>();
        resolveChildNodes(childNodes, node);
        return childNodes;
    }

    /**
     * 获取下级节点列表
     *
     * @param childNodes 下级节点列表
     * @param node       当前节点
     */
    private void resolveChildNodes(List<Node> childNodes, Node node) {
        for (Link link : node.getFlowOutLinks()) {
            if (!childNodes.contains(link.getToNode())) {
                if (NodeType.Manual.isEquals(link.getToNode().getNodeType()) || NodeType.Free.isEquals(link.getToNode().getNodeType())) {
                    childNodes.add(link.getToNode());
                    resolveChildNodes(childNodes, link.getToNode());
                } else if (NodeType.Subflow.isEquals(link.getToNode().getNodeType())) {
                    // 此处需要添加子流程的节点id，暂不支持
                    for (Link l : node.getFlowOutLinks()) {
                        resolveChildNodes(childNodes, l.getToNode());
                    }
                }
            }
        }
    }

    /**
     * 获取上级任务列表
     *
     * @param taskId 任务id
     * @return 上级任务列表
     * @author JonnyJiang
     * @date 2019/12/10 17:54
     */

    public List<HistoricTaskInstance> listParentTasks(String taskId) {
        HistoricTaskInstance historicTaskInstance = getHistoricTaskInstance(taskId);
        WfiFlowDO wfiFlow = getWfiFlowById(historicTaskInstance.getProcessInstanceId());
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        return listParentTasks(flow, historicTaskInstance);
//        Node node = flow.getNodeById(historicTaskInstance.getName());
//        List<String> parentNodeIds = new ArrayList<>();
//        listParentNodeIds(parentNodeIds, node);
//        if (parentNodeIds.size() > 0) {
//            return getHistoricTaskInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).taskNameIn(parentNodeIds).orderByTaskCreateTime().asc().list();
//        } else {
//            return new ArrayList<>();
//        }
    }

    public List<HistoricTaskInstance> listParentTasks(Flow flow, String taskId) {
        HistoricTaskInstance historicTaskInstance = getHistoricTaskInstance(taskId);
        return listParentTasks(flow, historicTaskInstance);
    }


    public List<HistoricTaskInstance> listParentTasks(Flow flow, HistoricTaskInstance historicTaskInstance) {
        Node node = flow.getNodeById(historicTaskInstance.getName());
        List<String> parentNodeIds = new ArrayList<>();
        listParentNodeIds(parentNodeIds, node);
        if (parentNodeIds.size() > 0) {
            return getHistoricTaskInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).taskNameIn(parentNodeIds).orderByTaskCreateTime().asc().list();
        } else {
            return new ArrayList<>();
        }
    }

    public BpmnModel getBpmnModel(String processDefinitionId) {
        return repositoryService.getBpmnModel(processDefinitionId);
    }

    public WfiDeliverDO getWfiDeliverFrom(String taskId) {
        return getWfiDeliverById(getWfiDeliverIdFrom(taskId));
    }

    public WfiDeliverDO getWfiDeliverById(Collection<WfiDeliverDO> wfiDelivers, String id) {
        for (WfiDeliverDO wfiDeliver : wfiDelivers) {
            if (StringUtils.equals(wfiDeliver.getId(), id)) {
                wfiDeliver.setDeliverInfoClass(FlowUtils.getDeliverInfo(wfiDeliver));
                return wfiDeliver;
            }
        }
        throw new BpmException(LocaleUtils.getWfiDeliverNotFoundById(id));
    }

    public WfiDeliverDO getWfiDeliverById(String id) {
        WfiDeliverDO wfiDeliver = wfiDeliverService.getById(id);
        if (wfiDeliver == null) {
            throw new BpmException(LocaleUtils.getWfiDeliverNotFoundById(id));
        }
        wfiDeliver.setDeliverInfoClass(FlowUtils.getDeliverInfo(wfiDeliver));
        return wfiDeliver;
    }

    public DeliverInfo getDeliverInfoFrom(TaskInfo task) {
        WfiDeliverDO wfiDeliver = getWfiDeliverById(getWfiDeliverIdFrom(task.getId()));
        return wfiDeliver.getDeliverInfoClass();
    }

    public DeliverInfo getDeliverInfo(String wfiDeliverId) {
        WfiDeliverDO wfiDeliver = getWfiDeliverById(wfiDeliverId);
        return wfiDeliver.getDeliverInfoClass();
    }

    public DeliverInfo getDeliverInfoFrom(String wfiDeliverIdFrom) {
        return getDeliverInfo(wfiDeliverIdFrom);
    }

    public String getWfiDeliverIdFrom(String taskId) {
        return (String) getTaskVariable(taskId, TaskVariableName.WFI_DELIVER_ID_FROM);
    }

    public List<HistoricTaskInstance> listHistoricTaskInstancesByNodeId(String processInstanceId, String nodeId) {
        return getHistoricTaskInstanceQuery(processInstanceId, nodeId).list();
    }

    public String getWfiDeliverId(HistoricTaskInstance historicTaskInstance) {
        return (String) getTaskVariable(historicTaskInstance.getId(), TaskVariableName.WFI_DELIVER_ID);
    }

    public DeliverInfo getDeliverInfo(HistoricTaskInstance historicTaskInstance) {
        WfiDeliverDO wfiDeliver = getWfiDeliverById(getWfiDeliverId(historicTaskInstance));
        return wfiDeliver.getDeliverInfoClass();
    }

    public HistoricTaskInstanceQuery getHistoricTaskInstanceQuery() {
        return historyService.createHistoricTaskInstanceQuery().taskTenantId(securityUtils.getAppName());
    }

    public HistoricTaskInstanceQuery getHistoricTaskInstanceQuery(String processInstanceId) {
        return getHistoricTaskInstanceQuery().processInstanceId(processInstanceId);
    }

    public HistoricTaskInstanceQuery getHistoricTaskInstanceQuery(String processInstanceId, String nodeId) {
        return getHistoricTaskInstanceQuery(processInstanceId).taskName(nodeId);
    }

    public ProcessDefinitionQuery getProcessDefinitionQuery() {
        return repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(securityUtils.getAppName());
    }

    public ExecutionQuery getExecutionQuery() {
        return runtimeService.createExecutionQuery().executionTenantId(securityUtils.getAppName());
    }

    /**
     * 获取来自转交节点信息
     *
     * @param node   节点
     * @param taskId 任务id
     * @return 来自转交节点信息
     * @author JonnyJiang
     * @date 2019/12/30 10:59
     */

    public DeliverNode getDeliverNodeFrom(Node node, String taskId) {
        WfiDeliverDO wfiDeliver = getWfiDeliverById(getWfiDeliverIdFrom(taskId));
        DeliverInfo deliverInfo = wfiDeliver.getDeliverInfoClass();
        return getDeliverNodeFrom(deliverInfo, node.getId());
    }

    /**
     * 获取来自转交节点信息
     *
     * @param deliverInfoFrom 来自转交信息
     * @param nodeId          节点id
     * @return 来自转交节点信息
     * @author JonnyJiang
     * @date 2019/12/31 11:25
     */

    public DeliverNode getDeliverNodeFrom(DeliverInfo deliverInfoFrom, String nodeId) {
        for (DeliverNode deliverNode : deliverInfoFrom.getDeliverNodes()) {
            if (deliverNode.getNodeId().equals(nodeId)) {
                return deliverNode;
            }
        }
        throw new BpmException(LocaleUtils.getDeliverNodeNotFoundByNodeId(nodeId));
    }

    /**
     * 获取流程引擎中的流程定义
     *
     * @param flowId       流程定义id
     * @param deploymentId 发布id
     * @param flowCode     流程标识
     * @return org.activiti.engine.repository.ProcessDefinition
     * @author JonnyJiang
     * @date 2020/1/13 8:50
     */

    public ProcessDefinition getProcessDefinitionByKey(String flowId, String deploymentId, String flowCode) {
        ProcessDefinition processDefinition = getProcessDefinitionQuery().processDefinitionKey(flowId).processDefinitionName(flowCode).deploymentId(deploymentId).singleResult();
        if (processDefinition == null) {
            throw new BpmException(LocaleUtils.getProcessDefinitionNotFoundByKey(flowId));
        }
        return processDefinition;
    }

    /**
     * 获取流程发布信息
     *
     * @param wfdVFlowId 流程定义发布id
     * @param flowName   流程名称
     * @return org.activiti.engine.repository.Deployment
     * @author JonnyJiang
     * @date 2020/1/17 8:56
     */

    public Deployment getDeployment(String wfdVFlowId, String flowName) {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().deploymentKey(wfdVFlowId).deploymentName(flowName).orderByDeploymenTime().desc().list();
        if (deployments.size() == 0) {
            throw new BpmException(LocaleUtils.getDeploymentNotFound(wfdVFlowId, flowName));
        }
        return deployments.get(0);
    }

    public WfdVFlowDO getWfdVFlowById(String id) {
        WfdVFlowDO wfdVFlow = wfdVFlowService.getById(id);
        if (wfdVFlow == null) {
            throw new WfdVFlowNotFoundByIdException(id);
        }
        return wfdVFlow;
    }

    public WfiBackupDO getWfiBackupByTaskId(String flowInstanceId, String taskId) {
        WfiBackupDO wfiBackup = wfiBackupService.getOne(new QueryWrapper<WfiBackupDO>().eq("FLOW_ID", flowInstanceId).eq("TASK_ID", taskId).eq("APP_ID", securityUtils.getAppName()));
        if (wfiBackup == null) {
            throw new BpmException(LocaleUtils.getWfiBackupNotFoundByTaskId(flowInstanceId, taskId));
        }
        return wfiBackup;
    }
//
//    public static Map<String, Object> convertToMap(Object obj) {
//        try {
//            Map<String, Object> map = new HashMap<>();
//            Class<?> clazz = obj.getClass();
//            for (Field field : clazz.getDeclaredFields()) {
//                field.setAccessible(true);
//                map.put(field.getName(), field.get(obj));
//            }
//            return map;
//        } catch (Exception ex) {
//            LOGGER.debug(ex.getMessage());
//            throw new BpmException(ex.getMessage());
//        }
//    }

    public static Map<String, Object> convertToMap(WfiFlowDO wfiFlow) {
        return History.resolveWfiFlow(wfiFlow.getId(), wfiFlow.getVFlowId(), wfiFlow.getModel(), wfiFlow.getBusinessKey(), wfiFlow.getFlowNo(), wfiFlow.getFlowId(), wfiFlow.getFlowCode(), wfiFlow.getAppId());
    }

    private static Object convertToMap(WfiVFlowDO wfiVFlow) {
        return History.resolveWfiVFlow(wfiVFlow.getId(), wfiVFlow.getFlowId(), wfiVFlow.getModel(), wfiVFlow.getBpmn(), wfiVFlow.getFlowVersion(), wfiVFlow.getVersionEndDate());
    }

    public static Map<String, Object> convertToMap(WfiDeliverDO wfiDeliver) {
        return History.resolveWfiDeliver(wfiDeliver.getId(), wfiDeliver.getDeliverInfo(), wfiDeliver.getFlowId(), wfiDeliver.getUserId(), wfiDeliver.getDeliverTime());
    }

    public static Map<String, Object> convertToMap(WfiCommentDO wfiComment) {
        return History.resolveWfiComment(wfiComment);
    }

    public Map<String, Object> getBackupData(String flowInstanceId) {
        Map<String, Object> map = new HashMap<>(16);
        if (BpmManager.ENGINE_VERSION.equals(History.ENGINE_VERSION)) {

//        WfiFlowDO wfiFlow = queryUtils.getWfiFlowById(flowInstanceId);
//        WfdVFlowDO wfdVFlow = queryUtils.getWfdVFlowById(wfiFlow.getVFlowId());

//        Map<String, Object> deployment = wfiBackupService.getActReDeploymentByKey(wfdVFlow.getId(), wfdVFlow.getName());
//        if (deployment == null) {
//            throw new BpmException(LocaleUtils.getDeploymentNotFound(wfdVFlow.getId(), wfdVFlow.getName()));
//        }
//        map.put(ACT_RE_DEPLOYMENT, deployment);
//        String deploymentId = (String) deployment.get("ID_");
//        Map<String, Object> procdef = wfiBackupService.getActReProcdef(deploymentId, wfdVFlow.getFlowId());
//        if (procdef == null) {
//            throw new BpmException(LocaleUtils.getReProcdefNotFound(deploymentId, wfdVFlow.getId()));
//        }
//        map.put(ACT_RE_PROCDEF, procdef);

            // ACT_RU_EXECUTION
            List<Map<String, Object>> ruExecutions = wfiBackupService.listActRuExecutionByRootProcInstId(flowInstanceId);
            map.put(TableName.ACT_RU_EXECUTION, ruExecutions);
            // ACT_RU_IDENTITYLINK
            List<Map<String, Object>> ruIdentityLinks = wfiBackupService.listActRuIdentityLinksByRootProcInstId(flowInstanceId);
            map.put(TableName.ACT_RU_IDENTITYLINK, ruIdentityLinks);
            // ACT_RU_TASK
            List<Map<String, Object>> ruTasks = wfiBackupService.listActRuTaskByRootProcInstId(flowInstanceId);
            if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
                com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveStActRuTasks(ruTasks);
            }
            map.put(TableName.ACT_RU_TASK, ruTasks);
            // ActRuVariables
            List<Map<String, Object>> ruVariables = wfiBackupService.listActRuVariableByRootProcInstId(flowInstanceId);
            if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
                com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveStActRuVariables(ruVariables);
            }
            map.put(TableName.ACT_RU_VARIABLE, ruVariables);
            // ACT_HI_ACTINST
            List<Map<String, Object>> hiActinsts = wfiBackupService.listActHiActinstByRootProcInstId(flowInstanceId);
            if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
                com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveStActHiActinsts(hiActinsts);
            }
            map.put(TableName.ACT_HI_ACTINST, hiActinsts);
            // ACT_HI_COMMENT
            List<Map<String, Object>> hiComments;
            if (ProcessEngineConfigurationImpl.DATABASE_TYPE_MYSQL.equals(BpmConfiguration.getActualDatabaseType())) {
                hiComments = wfiBackupService.listMySqlActHiCommentByRootProcInstId(flowInstanceId);
            } else if (ProcessEngineConfigurationImpl.DATABASE_TYPE_ORACLE.equals(BpmConfiguration.getActualDatabaseType())) {
                hiComments = wfiBackupService.listOracleActHiCommentByRootProcInstId(flowInstanceId);
            } else if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
                hiComments = wfiBackupService.listStActHiCommentByRootProcInstId(flowInstanceId);
            } else {
                hiComments = wfiBackupService.listActHiCommentByRootProcInstId(flowInstanceId);
            }
            map.put(TableName.ACT_HI_COMMENT, hiComments);
            // ACT_HI_DETAIL
            List<Map<String, Object>> hiDetails = wfiBackupService.listActHiDetailsByRootProcInstId(flowInstanceId);
            if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
                com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveStActHiDetails(hiDetails);
            }
            map.put(TableName.ACT_HI_DETAIL, hiDetails);
            // ACT_HI_IDENTITYLINK
            List<Map<String, Object>> hiIdentitylinks = wfiBackupService.listActHiIdentityLinkByRootProcInstId(flowInstanceId);
            map.put(TableName.ACT_HI_IDENTITYLINK, hiIdentitylinks);
            // ACT_HI_PROCINST
            List<Map<String, Object>> hiProcinsts = wfiBackupService.listActHiProcinstByRootProcInstId(flowInstanceId);
            if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
                com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveStActHiProcinsts(hiProcinsts);
            }
            map.put(TableName.ACT_HI_PROCINST, hiProcinsts);
            // ACT_HI_TASKINST
            List<Map<String, Object>> hiTaskinsts = wfiBackupService.listActHiTaskinstByRootProcInstId(flowInstanceId);
            if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
                com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveStActHiTaskinsts(hiTaskinsts);
            }
            map.put(TableName.ACT_HI_TASKINST, hiTaskinsts);
            // ACT_HI_VARINST
            List<Map<String, Object>> hiVarinsts = wfiBackupService.listActHiVarinstByRootProcInstId(flowInstanceId);
            if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
                com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveStActHiVarinsts(hiVarinsts);
            }
            map.put(TableName.ACT_HI_VARINST, hiVarinsts);
            // ACT_GE_BYTEARRAY
            List<Map<String, Object>> geByteArrays;
            if (ProcessEngineConfigurationImpl.DATABASE_TYPE_MYSQL.equals(BpmConfiguration.getActualDatabaseType())) {
                geByteArrays = wfiBackupService.listMySqlActGeByteArrayByRootProcInstId(flowInstanceId);
            } else if (ProcessEngineConfigurationImpl.DATABASE_TYPE_ORACLE.equals(BpmConfiguration.getActualDatabaseType())) {
                geByteArrays = wfiBackupService.listOracleActGeByteArrayByRootProcInstId(flowInstanceId);
            } else if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
                geByteArrays = wfiBackupService.listStActGeByteArrayByRootProcInstId(flowInstanceId);
            } else {
                geByteArrays = wfiBackupService.listActGeByteArrayByRootProcInstId(flowInstanceId);
            }
            map.put(TableName.ACT_GE_BYTEARRAY, geByteArrays);
            // WFI_FLOW
            List<WfiFlowDO> wfiFlows = wfiBackupService.listWfiFlow(flowInstanceId);
            map.put(TableName.WFI_FLOW, wfiFlows.stream().map(QueryUtil::convertToMap).collect(Collectors.toList()));
            // WFI_V_FLOW
            List<WfiVFlowDO> wfiVFlows = wfiBackupService.listWfiVFlow(flowInstanceId);
            map.put(TableName.WFI_V_FLOW, wfiVFlows.stream().map(QueryUtil::convertToMap).collect(Collectors.toList()));
            // WFI_DELIVER
            List<WfiDeliverDO> wfiDelivers = wfiBackupService.listWfiDeliver(flowInstanceId);
            map.put(TableName.WFI_DELIVER, wfiDelivers.stream().map(QueryUtil::convertToMap).collect(Collectors.toList()));
            // WFI_COMMENT
            List<WfiCommentDO> wfiComments = wfiBackupService.listWfiComment(flowInstanceId);
            map.put(TableName.WFI_COMMENT, wfiComments.stream().map(QueryUtil::convertToMap).collect(Collectors.toList()));
        } else {
            throw new BpmException(LocaleUtils.getUnsupportedEngineVersion(BpmManager.ENGINE_VERSION));
        }

//        map.forEach((k, v) -> {
//            ArrayList arrayList = (ArrayList) v;
//            System.out.println(k + "------------");
//            arrayList.forEach(o -> {
//                Map<String, Object> hashMap = (HashMap<String, Object>) o;
//                hashMap.forEach((ki, vi) -> {
//                    if (vi != null) {
//                        System.out.println(ki + vi.getClass() + "|" + vi.getClass().getTypeName());
//                    }
//                });
//            });
//        });

        return map;
    }

    public Object getTaskVariable(String taskId, TaskVariableName taskVariableName) {
        List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery().taskId(taskId).variableName(taskVariableName.getName()).list();
        if (variables.size() > 0) {
            return variables.get(0).getValue();
        }
        throw new BpmException(LocaleUtils.getTaskVariableNotFound(taskId, taskVariableName));
    }

    public Map<String, Object> listTaskVariableValues(List<HistoricTaskInstance> tasks, TaskVariableName taskVariableName) {
        return listTaskVariableValues(tasks.stream().map(TaskInfo::getId).collect(Collectors.toSet()), taskVariableName, true);
    }

    public Map<String, Object> listTaskVariableValues(Set<String> taskIds, TaskVariableName taskVariableName) {
        return listTaskVariableValues(taskIds, taskVariableName, true);
    }

    public Map<String, Object> listTaskVariableValues(Set<String> taskIds, TaskVariableName taskVariableName, Boolean forceExists) {
        Map<String, Object> map = new Hashtable<>();
        List<HistoricVariableInstance> variables;
        if (taskIds != null && taskIds.size() > 0) {
            variables = getHistoricVariableInstanceQuery().taskIds(taskIds).variableName(taskVariableName.getName()).list();
        } else {
            variables = new ArrayList<>();
        }
        Optional<HistoricVariableInstance> variable;
        for (String taskId : taskIds) {
            variable = variables.stream().filter(o -> taskId.equals(o.getTaskId())).findFirst();
            if (variable.isPresent()) {
                map.put(taskId, variable.get().getValue());
                variables.remove(variable.get());
            } else {
                if (forceExists) {
                    throw new BpmException(LocaleUtils.getTaskVariableNotFound(taskId, taskVariableName));
                }
            }
        }
        return map;
    }

    public ProcessDefinition getLatestProcessDefinition(String vFlowId, String code) {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(securityUtils.getAppName()).processDefinitionKey(vFlowId).processDefinitionName(code).latestVersion().list();
        if (processDefinitions.size() > 0) {
            return processDefinitions.get(0);
        }
        return null;
    }

    /**
     * 节点是否办结
     *
     * @param flowInstanceId   流程实例ID
     * @param wfiDeliverIdFrom 来自转交信息id
     * @param nodeId           节点id
     * @return 是否办结
     * @author JonnyJiang
     * @date 2020/6/2 15:04
     */

    public boolean nodeIsFinished(String flowInstanceId, String wfiDeliverIdFrom, String nodeId) {
        return listUnfinishedTasks(flowInstanceId, wfiDeliverIdFrom, nodeId).size() == 0;
    }

    /**
     * 获取未办结任务列表
     *
     * @param flowInstanceId   流程实例id
     * @param wfiDeliverIdFrom 来自转交信息id
     * @param nodeId           节点id
     * @return 未办结任务列表
     * @author JonnyJiang
     * @date 2020/6/3 8:53
     */

    public List<Task> listUnfinishedTasks(String flowInstanceId, String wfiDeliverIdFrom, String nodeId) {
        List<Task> tasks = new ArrayList<>();
        if (StringUtils.isNotEmpty(wfiDeliverIdFrom)) {
            List<Task> unfinishedTasks = getTaskQuery(flowInstanceId).taskName(nodeId).list();
            if (unfinishedTasks.size() > 0) {
                Map<String, Object> deliverIdFromMap = listTaskVariableValues(unfinishedTasks.stream().map(Task::getId).collect(Collectors.toSet()), TaskVariableName.WFI_DELIVER_ID_FROM);
                for (Task unfinishedTask : unfinishedTasks) {
                    if (wfiDeliverIdFrom.equals(deliverIdFromMap.get(unfinishedTask.getId()))) {
                        tasks.add(unfinishedTask);
                    }
                }
            }
        }
        return tasks;
    }

    public SpringProcessEngineConfiguration getSpringProcessEngineConfiguration() {
        return springProcessEngineConfiguration;
    }

    public ExpressionManager getExpressionManager() {
        return getSpringProcessEngineConfiguration().getExpressionManager();
    }

    /**
     * 是否有流程管理权限
     *
     * @param flowId 流程定义id
     * @param userId 用户id
     * @return 是否有流程管理权限
     * @author JonnyJiang
     * @date 2020/6/3 16:30
     */

    public boolean hasAdminAuth(String flowId, String userId) {
        WfdVFlowDO wfdVFlow = getEffectiveWfdVFlowByFlowId(flowId);
        return iSecurity.hasAuthorityWithUserId(userId, wfdVFlow.getAdminAuthId());
    }

    /**
     * 是否有流程查询权限
     *
     * @param flowId 流程定义id
     * @param userId 用户id
     * @return 是否有流程查询权限
     * @author JonnyJiang
     * @date 2020/6/3 16:33
     */

    public boolean hasQueryAuth(String flowId, String userId) {
        WfdVFlowDO wfdVFlow = getEffectiveWfdVFlowByFlowId(flowId);
        if (iSecurity.hasAuthorityWithUserId(userId, wfdVFlow.getQueryAuthId())) {
            return true;
        }
        return iSecurity.hasAuthorityWithUserId(userId, wfdVFlow.getAdminAuthId());
    }

    public List<SysUserDO> listUsersByIds(List<String> ids) {
        if (ids == null) {
            return new ArrayList<>();
        } else {
            List<SysUserDO> users = iUser.getUsersByIds(ids);
            // 检查账号是否存在
            if (users.size() != ids.size()) {
                // 如果有账号不存在的
                for (String userId : ids) {
                    if (users.stream().noneMatch(o -> o.getId().equals(userId))) {
                        throw new SysUserNotFoundByIdException(userId);
                    }
                }
            }
            return users;
        }
    }

    public void checkWaitForPassUsers(Node node, String taskId, String owner, String assignee, List<HistoricTaskInstance> unfinishedTasksDeliver) {
        // 如果存在前序签署人，判断前序签署人是否已办理
        if (node.getWaitForPassUsers().size() > 0) {
            // 任务归属人
            String currentUserId;
            if (StringUtils.isEmpty(owner)) {
                // 如果owner为空，则表示任务没有委托办理
                currentUserId = assignee;
            } else {
                // 如果owner不为空，则标识任务已委托办理
                currentUserId = owner;
            }
            List<String> waitForPassUsers = new ArrayList<>();
            Integer index = node.getWaitForPassUsers().indexOf(currentUserId);
            if (index > -1) {
                // 如果当前用户也在前序办理人列表中，将当前用户之前的办理人加入当前任务的前序办理人列表中
                for (int i = 0; i < index; i++) {
                    waitForPassUsers.add(node.getWaitForPassUsers().get(i));
                }
            } else {
                waitForPassUsers.addAll(node.getWaitForPassUsers());
            }
            if (waitForPassUsers.size() > 0) {
                // 未办结的前序签署人id列表
                List<String> unfinishedWaitForPassUserIds = new ArrayList<>();
                String unfinishedUserId;
                for (HistoricTaskInstance unfinishedTask : unfinishedTasksDeliver) {
                    if (StringUtils.isEmpty(unfinishedTask.getOwner())) {
                        unfinishedUserId = unfinishedTask.getAssignee();
                    } else {
                        unfinishedUserId = unfinishedTask.getOwner();
                    }
                    if (waitForPassUsers.contains(unfinishedUserId)) {
                        unfinishedWaitForPassUserIds.add(unfinishedUserId);
                    }
                }
                if (unfinishedWaitForPassUserIds.size() > 0) {
                    List<SysUserDO> unfinishedWaitForPassUsers = listUsersByIds(unfinishedWaitForPassUserIds);
                    // 如果未办理人员账号都存在
                    throw new WaitForPassUsersUnFinishedException(taskId, unfinishedWaitForPassUsers);
                }
            }
        }
    }

    public WfiTaskPendingDO getWfiTaskPendingByTaskId(String flowId, String taskId, String userId) {
        WfiTaskPendingDO wfiTaskPending = wfiTaskPendingService.getByTaskId(flowId, taskId, userId);
        if (wfiTaskPending == null) {
            throw new WfiTaskPendingNotFoundException(flowId, taskId, userId);
        }
        return wfiTaskPending;
    }

    public Map<String, WfiDeliverTasks> getWfiDeliverMap(Map<String, Object> wfiDeliverIdFromMap, List<HistoricTaskInstance> tasks) {
        Map<String, WfiDeliverTasks> wfiDeliverMap = new HashMap<>();
        String wfiDeliverId;
        WfiDeliverTasks wfiDeliverTasksTemp;
        for (HistoricTaskInstance task : tasks) {
            wfiDeliverId = (String) wfiDeliverIdFromMap.get(task.getId());
            if (wfiDeliverMap.containsKey(wfiDeliverId)) {
                wfiDeliverTasksTemp = wfiDeliverMap.get(wfiDeliverId);
            } else {
                wfiDeliverTasksTemp = new WfiDeliverTasks(wfiDeliverId);
                wfiDeliverMap.put(wfiDeliverId, wfiDeliverTasksTemp);
            }
            wfiDeliverTasksTemp.add(task);
        }
        return wfiDeliverMap;
    }

    /**
     * 判断是否允许转交，主要是办理人权限的校验
     *
     * @param userType       用户身份
     * @param node           节点
     * @param flowInstanceId 流程实例id
     * @param taskId         任务id
     * @param owner          所有人
     * @param assignee       办理人
     * @author JonnyJiang
     * @date 2020/6/24 16:55
     */

    public void checkAllowDeliver(UserType userType, Node node, String flowInstanceId, String taskId, String owner, String assignee, WfiDeliverDO wfiDeliverFrom) {
        if (UserType.None.equals(userType)) {
            throw new NoAccessToCompleteTaskException(taskId);
        } else {
            // 任务所有任务
            List<HistoricTaskInstance> tasks = listHistoricTaskInstances(flowInstanceId);
            // 获取任务来自转交信息
            Map<String, Object> wfiDeliverIdFromMap = listTaskVariableValues(tasks, TaskVariableName.WFI_DELIVER_ID_FROM);
            // 每次转交产生的任务图
            Map<String, WfiDeliverTasks> wfiDeliverMap = getWfiDeliverMap(wfiDeliverIdFromMap, tasks);
            WfiDeliverTasks wfiDeliverTasks = wfiDeliverMap.get(wfiDeliverFrom.getId());
            if (UserType.Host.equals(userType)) {
                checkAllowDeliverHost(node, taskId, owner, assignee, wfiDeliverTasks);
                checkPresetRoute(node, taskId);
            } else if (UserType.Assistant.equals(userType)) {
                // 如果是协办人
                checkAllowCompleteAssistant(node, taskId, owner, assignee, wfiDeliverTasks);
            }
        }
    }

    public void checkPresetRoute(Node node, String taskId) {
        if (IntegerUtils.isTrue(node.getAllowPresetRoute())) {
            // 如果当前节点允许预设后续流转步骤
            if (IntegerUtils.isTrue(node.getMustPresetRoute())) {
                // 如果当前节点必须预设后续流转步骤
                if (wfiRoutePresetService.count(new QueryWrapper<WfiRoutePresetDO>().eq("task_id", taskId)) == 0) {
                    throw new MustPresetRouteException(node.getCode(), node.getName(), taskId);
                }
            }
        }
    }

    public boolean allowDeliverHost(Node node, String taskId, String owner, String assignee, WfiDeliverTasks wfiDeliverTasks) {
        try {
            checkAllowDeliverHost(node, taskId, owner, assignee, wfiDeliverTasks);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void checkAllowDeliverHost(Node node, String taskId, String owner, String assignee, WfiDeliverTasks wfiDeliverTasks) {
        // 与task相同节点的未办结任务
        List<HistoricTaskInstance> unfinishedTasks = wfiDeliverTasks.getUnfinishedTasks().stream().filter(o -> StringUtils.equals(node.getId(), o.getName())).collect(Collectors.toList());
        checkWaitForPassUsers(node, taskId, owner, assignee, unfinishedTasks);
        // 如果是主办人，检查当前结转条件
        if (AllowPassMode.None.isEquals(node.getAllowPassMode())) {

        } else if (AllowPassMode.AllSign.isEquals(node.getAllowPassMode())) {
            // 所有经办人，都已签署
            if (unfinishedTasks.size() == 1) {
                if (!unfinishedTasks.get(0).getId().equals(taskId)) {
                    // 如果未办结的任务，不是当前任务
                    throw new BpmException(LocaleUtils.getTaskAlreadyCompleted(taskId));
                }
            } else if (unfinishedTasks.size() > 1) {
                // 如果未办结的任务大于1
                throw new WaitingForAssistException(node.getCode(), node.getName(), taskId);
            } else {
                // 如果没有正在办理的任务
                throw new BpmException(LocaleUtils.getTaskAlreadyCompleted(taskId));
            }
        } else if (AllowPassMode.SpecifiedUserAllSign.isEquals(node.getAllowPassMode())) {
            // 判断指定人员是否已全部办理完毕
            checkWaitForPassUsers(node, taskId, owner, assignee, unfinishedTasks);
        } else if (AllowPassMode.ExceedCount.isEquals(node.getAllowPassMode())) {
            long finishedCnt = wfiDeliverTasks.getFinishedTasks().stream().filter(o -> StringUtils.equals(o.getName(), node.getId()) && !o.getId().equals(taskId)).count();
            if (finishedCnt + 1 < node.getWaitForPassCount()) {
                // 已办结数量+1如果不满足指定办结数量
                throw new PassCountNotMatchException(finishedCnt, node.getWaitForPassCount(), taskId);
            }
        } else if (AllowPassMode.ExceedPercent.isEquals(node.getAllowPassMode())) {
            List<HistoricTaskInstance> finishedTasks = wfiDeliverTasks.getFinishedTasks().stream().filter(o -> StringUtils.equals(o.getName(), node.getId())).collect(Collectors.toList());
            Long finishedCnt = finishedTasks.stream().filter(o -> !o.getId().equals(taskId)).count();
            Integer totalCnt = finishedTasks.size() + unfinishedTasks.size();
            if (totalCnt.equals(0)) {
                throw new TaskNotFoundByIdException(taskId);
            } else {
                Long finishedPercent = (finishedCnt + 1) * 100 / totalCnt;
                if (finishedPercent < node.getWaitForPassPercent()) {
                    // 已办结数量+1后，办结人数如果不满足指定比例
                    throw new PassPercentNotMatchException(node.getCode(), node.getName(), finishedPercent, node.getWaitForPassPercent(), taskId);
                }
            }
        }
    }

    private void checkAllowCompleteAssistant(Node node, String taskId, String owner, String assignee, WfiDeliverTasks wfiDeliverTasks) {
        if (HostMode.FirstClaim.isEquals(node.getHostMode())) {
            // 如果是先接收主办
            DeliverInfo deliverInfo = getDeliverInfoFrom(wfiDeliverTasks.getWfiDeliverId());
            DeliverNode deliverNode = deliverInfo.getDeliverNodeByNodeId(node.getId());
            String userId = StringUtils.isEmpty(owner) ? assignee : owner;
            String claimerId = FlowUtils.getClaimerId(node, deliverNode, userId);
            if (StringUtils.isEmpty(claimerId)) {
                // 如果没有主办人，如果除当前用户用户外，所有协办人都已办结，当前用户必须先接收，再转交
                List<HistoricTaskInstance> unfinishedTasks;
                if (MultiInstanceMode.Single.isEquals(node.getMultiInstanceMode())) {
                    unfinishedTasks = new ArrayList<>(wfiDeliverTasks.getUnfinishedTasks());
                } else {
                    unfinishedTasks = new ArrayList<>();
                    DeliverUser deliverUser = deliverNode.getDeliverUserByUserId(userId);
                    if (MultiInstanceMode.Department.isEquals(node.getMultiInstanceMode())) {
                        String unfinishedUserId;
                        DeliverUser unfinishedDeliverUser;
                        for (HistoricTaskInstance unfinishedTask : wfiDeliverTasks.getUnfinishedTasks()) {
                            unfinishedUserId = StringUtils.isEmpty(unfinishedTask.getOwner()) ? unfinishedTask.getAssignee() : unfinishedTask.getOwner();
                            unfinishedDeliverUser = deliverNode.getDeliverUserByUserId(unfinishedUserId);
                            if (StringUtils.equals(deliverUser.getDepartmentId(), unfinishedDeliverUser.getDepartmentId())) {
                                unfinishedTasks.add(unfinishedTask);
                            }
                        }
                    } else if (MultiInstanceMode.Organization.isEquals(node.getMultiInstanceMode())) {
                        String unfinishedUserId;
                        DeliverUser unfinishedDeliverUser;
                        for (HistoricTaskInstance unfinishedTask : wfiDeliverTasks.getUnfinishedTasks()) {
                            unfinishedUserId = StringUtils.isEmpty(unfinishedTask.getOwner()) ? unfinishedTask.getAssignee() : unfinishedTask.getOwner();
                            unfinishedDeliverUser = deliverNode.getDeliverUserByUserId(unfinishedUserId);
                            if (StringUtils.equals(deliverUser.getOrganizationId(), unfinishedDeliverUser.getOrganizationId())) {
                                unfinishedTasks.add(unfinishedTask);
                            }
                        }
                    } else if (MultiInstanceMode.Group.isEquals(node.getMultiInstanceMode())) {
                        String unfinishedUserId;
                        DeliverUser unfinishedDeliverUser;
                        for (HistoricTaskInstance unfinishedTask : wfiDeliverTasks.getUnfinishedTasks()) {
                            unfinishedUserId = StringUtils.isEmpty(unfinishedTask.getOwner()) ? unfinishedTask.getAssignee() : unfinishedTask.getOwner();
                            unfinishedDeliverUser = deliverNode.getDeliverUserByUserId(unfinishedUserId);
                            if (StringUtils.equals(deliverUser.getGroupId(), unfinishedDeliverUser.getGroupId())) {
                                unfinishedTasks.add(unfinishedTask);
                            }
                        }
                    } else {
                        throw new UnsupportedMultiInstanceModeException(node);
                    }
                }
                if (unfinishedTasks.size() == 1) {
                    // 如果仅有一个任务未办结
                    if (unfinishedTasks.get(0).getId().equals(taskId)) {
                        // 如果仅有当前任务未办结，并且用户身份为协办人
                        throw new MustClaimThenDeliverException(taskId);
                    }
                }
            } else {
                // 如果已有主办人
                if (IntegerUtils.isFalse(node.getEnableAssistAhd())) {
                    // 如果主办人接收后不允许继续办理
                    throw new EnableAssistAhdException(taskId);
                }
            }
        }
    }
}