package com.csicit.ace.bpm.activiti.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.bpm.*;
import com.csicit.ace.bpm.FlowInstance;
import com.csicit.ace.bpm.activiti.*;
import com.csicit.ace.bpm.activiti.cmd.InviteCmd;
import com.csicit.ace.bpm.activiti.cmd.MultiInstanceCmd;
import com.csicit.ace.bpm.activiti.nodes.AbstractNode;
import com.csicit.ace.bpm.activiti.nodes.ManualNode;
import com.csicit.ace.bpm.activiti.utils.BpmnUtil;
import com.csicit.ace.bpm.activiti.utils.QueryUtil;
import com.csicit.ace.bpm.activiti.utils.TaskPendingUtil;
import com.csicit.ace.bpm.collection.BaseCollection;
import com.csicit.ace.bpm.delegate.*;
import com.csicit.ace.bpm.delegate.args.*;
import com.csicit.ace.bpm.el.WfdFlowElService;
import com.csicit.ace.bpm.enums.*;
import com.csicit.ace.bpm.exception.*;
import com.csicit.ace.bpm.pojo.domain.*;
import com.csicit.ace.bpm.pojo.vo.*;
import com.csicit.ace.bpm.pojo.vo.wfd.Event;
import com.csicit.ace.bpm.pojo.vo.wfd.*;
import com.csicit.ace.bpm.service.*;
import com.csicit.ace.bpm.utils.*;
import com.csicit.ace.common.config.ZuulRouteConfig;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.AceCollectionUtils;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.interfaces.service.IAuditLog;
import com.csicit.ace.interfaces.service.IMessage;
import com.csicit.ace.interfaces.service.IUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author JonnyJiang
 * @date 2019/7/10 18:00
 */
@Component("bpmManager")
public class BpmManagerImpl implements BpmManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(BpmManagerImpl.class);
    private static final int FIRST_FLOW_VERSION = 1;
    private static final String STOP_AFTER_REJECT_END_EVENT_NAME = "STOP_AFTER_REJECT_END_EVENT_NAME";
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private WfdFlowService wfdFlowService;
    @Autowired
    private WfdVFlowService wfdVFlowService;
    @Autowired
    private WfiFlowService wfiFlowService;
    @Autowired
    private WfdCollectionUtils wfdCollectionUtils;
    @Autowired
    private RuleUtils ruleUtils;
    @Autowired
    private WfiDeliverService wfiDeliverService;
    @Autowired
    private WfdGlobalVariantService wfdGlobalVariantService;
    @Autowired
    private IAuditLog sysAuditLogService;
    @Autowired
    private QueryUtil queryUtil;
    @Autowired
    private BpmAdapter bpmAdapter;
    @Autowired
    private WfiBackupService wfiBackupService;
    @Autowired
    private WfdDelegateWorkService wfdDelegateWorkService;
    @Autowired
    private WfdDelegateUserService wfdDelegateUserService;
    @Autowired
    private IUser iUser;
    @Autowired
    private IMessage iMessage;
    @Autowired
    private WfiCommentService wfiCommentService;
    @Autowired
    private WfiFocusedWorkService wfiFocusedWorkService;
    @Autowired
    private WfiUserTaskStateService wfiUserTaskStateService;
    @Autowired
    private WfiTaskPendingService wfiTaskPendingService;
    @Autowired
    private TaskPendingUtil taskPendingUtil;
    @Autowired
    private ZuulRouteConfig zuulRouteConfig;
    @Autowired
    private WfdFlowElService wfdFlowElService;
    @Autowired
    private UrgeTaskMessageService urgeTaskMessageService;
    @Autowired
    private SysReviewFileService reviewFileService;

    @Override
    public FlowInstance createFlowInstanceById(String flowId, String businessKey) {
        return createFlowInstanceById(flowId, businessKey, new HashMap<>(16));
    }

    @Override
    public FlowInstance createFlowInstanceById(String flowId, String businessKey, Map<String, Object> variables) {
        return createFlowInstanceById(flowId, businessKey, securityUtils.getCurrentUser(), variables);
    }

    @Override
    public FlowInstance createFlowInstanceById(String flowId, String businessKey, SysUserDO starter) {
        return createFlowInstanceById(flowId, businessKey, starter, new HashMap<>(16));
    }

    @Override
    public FlowInstance createFlowInstanceById(String flowId, String businessKey, SysUserDO starter, Map<String, Object> variables) {
        WfdVFlowDO wfdVFlow = queryUtil.getEffectiveWfdVFlowByFlowId(flowId);
        return createFlowInstance(wfdVFlow, businessKey, starter, variables);
    }

    @Override
    public FlowInstance createFlowInstanceByCode(String code, String businessKey) {
        return createFlowInstanceByCode(code, businessKey, new HashMap<>(16));
    }

    @Override
    public FlowInstance createFlowInstanceByCode(String code, String businessKey, Map<String, Object> variables) {
        return createFlowInstanceByCode(code, businessKey, securityUtils.getCurrentUser(), variables);
    }

    @Override
    public FlowInstance createFlowInstanceByCode(String code, String businessKey, SysUserDO starter) {
        return createFlowInstanceByCode(code, businessKey, starter, new HashMap<>(16));
    }

    @Override
    public FlowInstance createFlowInstanceByCode(String code, String businessKey, SysUserDO starter, Map<String, Object> variables) {
        WfdVFlowDO wfdVFlow = bpmAdapter.getEffectiveWfdVFlowByCode(code);
        return createFlowInstance(wfdVFlow, businessKey, starter, variables);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param wfdVFlow ????????????
     * @return ????????????
     * @author JonnyJiang
     * @date 2019/9/23 20:41
     */

    private ProcessDefinition deployToEngine(WfdVFlowDO wfdVFlow) {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder.addString(wfdVFlow.getCode() + ".bpmn", wfdVFlow.getBpmn()).key(wfdVFlow.getId()).name(wfdVFlow.getName()).category(wfdVFlow.getCategoryId()).tenantId(securityUtils.getAppName()).deploy();
        ProcessDefinition processDefinition = queryUtil.getProcessDefinitionByKey(wfdVFlow.getFlowId(), deployment.getId(), wfdVFlow.getCode());
        wfdVFlow.setUsed(1);
        wfdVFlow.setProcessDefinitionId(processDefinition.getId());
        wfdVFlowService.updateUsedById(wfdVFlow.getId(), 1, processDefinition.getId());
        return processDefinition;
    }

    private void onFlowInstanceCreating(WfdVFlowDO wfdVFlow, Flow flow, String businessKey, SysUserDO starter, Map<String, Object> variables) {
        for (Event event : flow.getEvents()) {
            if (FlowEventType.Creating.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getFlowListenerInstance(FlowEventType.Creating, event.getClassName());
                if (listener instanceof FlowCreatingListener) {
                    ((FlowCreatingListener) listener).notify(new FlowCreatingEventArgs(wfdVFlow, flow, businessKey, starter, variables));
                }
            }
        }
    }

    private FlowInstance createFlowInstance(WfdVFlowDO wfdVFlow, String businessKey, SysUserDO
            starter, Map<String, Object> variables) {
        // ???????????????????????????
        if (wfiFlowService.count(new QueryWrapper<WfiFlowDO>().eq("BUSINESS_KEY", businessKey).eq("FLOW_CODE", wfdVFlow.getCode()).eq("APP_ID", securityUtils.getAppName())) > 0) {
            throw new BpmException(LocaleUtils.getBusinessKeyExist(businessKey, wfdVFlow.getCode()));
        }
        Flow flow = FlowUtils.getFlow(wfdVFlow.getModel());
        Node startNode;
        // ?????????????????????????????????
        Optional<Node> nodeOp = flow.getNodes().stream().filter(o -> NodeType.Start.isEquals(o.getNodeType()) && IntegerUtils.isTrue(o.getAllowManualStart())).findFirst();
        if (nodeOp.isPresent()) {
            startNode = nodeOp.get();
        } else {
            throw new BpmException(LocaleUtils.getManualStartNodeNotFound(flow.getCode()));
        }
        // ?????????????????????????????????????????????
        if (bpmAdapter.hasInitAuth(starter.getId(), wfdVFlow.getInitAuthId())) {
            // ?????????????????????????????????????????????????????????????????????
            // ????????????????????????????????????
            ProcessDefinition processDefinition = queryUtil.getLatestProcessDefinition(wfdVFlow.getFlowId(), wfdVFlow.getCode());
            if (processDefinition == null) {
                // ????????????????????????
                processDefinition = deployToEngine(wfdVFlow);
            } else {
                // ????????????????????????
                if (wfdVFlow.getProcessDefinitionId() == null && IntegerUtils.isTrue(wfdVFlow.getUsed())) {
                    wfdVFlow.setProcessDefinitionId(processDefinition.getId());
                    wfdVFlowService.update(new WfdVFlowDO(), new UpdateWrapper<WfdVFlowDO>().set("PROCESS_DEFINITION_ID", wfdVFlow.getProcessDefinitionId()).eq("ID", wfdVFlow.getId()));
                } else if (ObjectUtils.notEqual(wfdVFlow.getProcessDefinitionId(), processDefinition.getId())) {
                    // ???????????????????????????????????????????????????????????????????????????????????????
                    processDefinition = deployToEngine(wfdVFlow);
                }
            }
            // ????????????appName
            if (variables == null) {
                variables = new HashMap<>(16);
            }
            variables.put(ProcessVariableName.VFlowId.getName(), wfdVFlow.getId());
            List<DeliverNode> deliverNodes = getFlowOutNodes(startNode, starter.getId(), businessKey);
            if (deliverNodes.size() == 0) {
                throw new BpmException(LocaleUtils.getFlowOutLinkNotFound(flow.getId()));
            }
            DeliverInfo deliverInfo = new DeliverInfo();
            deliverInfo.setDeliverNodes(deliverNodes);
            HttpSession httpSession = securityUtils.getSession();
            SessionAttribute.initElSession(httpSession, null, flow, businessKey);
            FlowUtils.initDeliverNodes(flow, deliverInfo, businessKey);
            WfiDeliverDO wfiDeliver = getWfiDeliver(null, deliverInfo, starter.getId());
            variables.put(TaskVariableName.WFI_DELIVER_ID.getName(), wfiDeliver.getId());
            httpSession.setAttribute(SessionAttribute.TASK_WFD_DELIVER, wfiDeliver);
            httpSession.setAttribute(SessionAttribute.TASK_DELIVER_INFO, deliverInfo);
            variables.put(ProcessVariableName.IS_MAIN.getName(), true);
            Authentication.setAuthenticatedUserId(starter.getId());
            onFlowInstanceCreating(wfdVFlow, flow, businessKey, starter, variables);
            BpmnModel bpmnModel = queryUtil.getBpmnModel(processDefinition.getId());
            BpmnUtil.initUserTasks(flow, bpmnModel, deliverInfo, variables);
            initVariables(flow, deliverInfo, variables);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(flow.getId(), businessKey, variables, securityUtils.getAppName());
            resolveMultiInstance(deliverInfo, flow, processInstance.getId(), wfiDeliver.getId(), bpmnModel, variables);
            refreshTaskPending(processInstance.getId(), flow);
            onFlowInstanceCreated(wfdVFlow, flow, businessKey, starter, variables, processInstance);

            // ?????????????????????
            Map<String, Object> params = new HashMap<>();
            params.put("flowInstanceId", processInstance.getId());
            params.put("isFocus", "yes");
            Task task = queryUtil.getTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(securityUtils.getCurrentUserId()).singleResult();
            if (task != null) {
                params.put("taskId", task.getId());
                focusWork(params);
            }
            LOGGER.debug("processInstance created:" + processInstance);
            return new FlowInstanceImpl(processInstance);
        } else {
            throw new NoAccessToCreateFlowException(flow);
        }
    }

    private void onFlowInstanceCreated(WfdVFlowDO wfdVFlow, Flow flow, String businessKey, SysUserDO
            starter, Map<String, Object> variables, ProcessInstance processInstance) {
        for (Event event : flow.getEvents()) {
            if (FlowEventType.Created.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getFlowListenerInstance(FlowEventType.Created, event.getClassName());
                if (listener instanceof FlowCreatedListener) {
                    ((FlowCreatedListener) listener).notify(new FlowCreatedEventArgs(wfdVFlow, flow, businessKey, starter, variables, new FlowInstanceImpl(processInstance)));
                }
            }
        }
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowByBusinessKey(flow.getCode(), businessKey);
        syncSettings(flow, wfiFlow);
        sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.primary, ""), LocaleUtils.getFlowOperationCreate(), LocaleUtils.getFlowOperationCreateLog(flow.getCode(), wfiFlow.getId()));
    }

    private DeliverNode getDeliverNode(Link link, String flowStarterId, String flowInstanceId) {
        DeliverNode deliverNode = new DeliverNode();
        deliverNode.setNodeId(link.getToNodeId());
        Node toNode = link.getToNode();
        deliverNode.setNodeName(toNode.getName());
        if (IntegerUtils.isTrue(toNode.getEnableManualSelParticipant())) {
            // ????????????????????????
            List<SysUserDO> users = getNodeUsers(toNode, flowStarterId, flowInstanceId);
            if (IntegerUtils.isTrue(toNode.getEnableHostOnly())) {
                // ??????????????????????????????????????????????????????
                String hostId = getHostId(toNode);
                for (SysUserDO sysUser : users) {
                    if (sysUser.getId().equals(hostId)) {
                        DeliverUser deliverUser = new DeliverUser();
                        deliverUser.setUserId(sysUser.getId());
                        deliverUser.setRealName(sysUser.getRealName());
                        deliverUser.setUserType(UserType.Host.getValue());
                        deliverNode.addDeliverUser(deliverUser);
                        break;
                    }
                }
                validateDeliverUsers(toNode, deliverNode);
                return deliverNode;
            }
        }
        // ??????????????????????????????????????????????????????????????????
        List<SysUserDO> users = getNodeUsers(toNode, flowStarterId, flowInstanceId);
        String hostId = getHostId(toNode);
        for (SysUserDO sysUser : users) {
            DeliverUser deliverUser = new DeliverUser();
            deliverUser.setUserId(sysUser.getId());
            deliverUser.setRealName(sysUser.getRealName());
            if (sysUser.getId().equals(hostId)) {
                deliverUser.setUserType(UserType.Host.getValue());
            } else {
                deliverUser.setUserType(UserType.Assistant.getValue());
            }
            deliverNode.addDeliverUser(deliverUser);
        }
        validateDeliverUsers(toNode, deliverNode);
        return deliverNode;
    }

    private void validateDeliverUsers(Node toNode, DeliverNode deliverNode) {
        if (NodeType.Manual.isEquals(toNode.getNodeType()) || NodeType.Free.isEquals(toNode.getNodeType())) {
            if (deliverNode.getDeliverUsers().size() == 0) {
                throw new BpmException(LocaleUtils.getDeliverUsersNotFound(toNode.getName(), toNode.getCode()));
            }
        }
    }

    private List<SysUserDO> getNodeUsers(Node node, String flowStarterId, String flowInstanceId) {
        Map<String, Object> variables = new HashMap<>(16);
        variables.put(BaseCollection.FLOW_STARTER_ID, flowStarterId);
        variables.put(BaseCollection.FLOW_INSTANCE_ID, flowInstanceId);
        List<SysUserDO> users = wfdCollectionUtils.getUsers(node, variables);
        if (users == null) {
            throw new BpmException(LocaleUtils.getNodeUsersNotFound(node.getName(), node.getCode()));
        } else {
            users.sort((u1, u2) -> ObjectUtils.compare(u1.getSortIndex(), u2.getSortIndex()));
            return users;
        }
    }

    private String getHostId(Node node) {
        String hostId = "";
        if (HostMode.Specified.isEquals(node.getHostMode())) {
            if (StringUtils.isNotEmpty(node.getHostId())) {
                hostId = node.getHostId();
            }
        }
        return hostId;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param node          ????????????
     * @param flowStarterId ?????????ID
     * @param businessKey   ????????????
     * @return java.util.List<com.csicit.ace.bpm.pojo.vo.DeliverNode>
     * @author JonnyJiang
     * @date 2019/11/8 14:00
     */

    private List<DeliverNode> getFlowOutNodes(Node node, String flowStarterId, String businessKey) {
        return getFlowOutNodes(node, flowStarterId, null, businessKey);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param node ????????????
     * @return java.util.List<com.csicit.ace.bpm.pojo.vo.DeliverNode>
     * @author JonnyJiang
     * @date 2019/11/1 11:48
     */

    private List<DeliverNode> getFlowOutNodes(Node node, String flowStarterId, String flowInstanceId, String businessKey) {
        List<DeliverNode> deliverNodes = new ArrayList<>();
        // ??????????????????????????????
        if (node.getFlowOutLinks().size() == 0) {
            throw new NodeOutLinkNotFoundException(node.getCode());
        } else if (node.getFlowOutLinks().size() == 1) {
            // ??????????????????????????????????????????????????????????????????????????????????????????
            Link link = node.getFlowOutLinks().get(0);
            deliverNodes.add(getDeliverNode(link, flowStarterId, flowInstanceId));
        } else {
            // ????????????????????????
            String defaultResult = null;
            if (node.getWorkResultOptions() != null) {
                if (node.getWorkResultOptions().length > 0) {
                    defaultResult = node.getWorkResultOptions()[0];
                }
            }
            for (Link link : node.getFlowOutLinks()) {
                if (ConditionMode.None.isEquals(link.getConditionMode())) {
                    deliverNodes.add(getDeliverNode(link, flowStarterId, flowInstanceId));
                } else if (ConditionMode.WorkResult.isEquals(link.getConditionMode())) {
                    if (link.getResult() != null && defaultResult != null) {
                        if (link.getResult().equals(defaultResult)) {
                            deliverNodes.add(getDeliverNode(link, flowStarterId, flowInstanceId));
                        }
                    }
                } else if (ConditionMode.Rule.isEquals(link.getConditionMode())) {
                    // ????????????????????????
                    SessionAttribute.initElSession(securityUtils.getSession(), flowInstanceId, link.getFlow(), businessKey);
                    Boolean val = ruleUtils.calculateRule(node.getFlow(), link.getRuleExpression());
                    if (val) {
                        deliverNodes.add(getDeliverNode(link, flowStarterId, flowInstanceId));
                    }
                }
            }
        }
        return deliverNodes;
    }

    @Override
    public void deliverWork(TaskInstance task) {
        deliverWork(task.getId());
    }

    @Override
    public void deliverWork(TaskInstance task, SysUserDO currentUser) {
        deliverWork(task.getId(), currentUser);
    }

    @Override
    public void deliverWork(String taskId) {
        deliverWork(taskId, securityUtils.getCurrentUser());
    }

    @Override
    public void deliverWork(String taskId, SysUserDO currentUser) {
        deliverWork(taskId, new HashMap<>(16), currentUser);
    }

    @Override
    public void deliverWork(TaskInstance task, Map<String, Object> variables) {
        deliverWork(task.getId(), variables, securityUtils.getCurrentUser());
    }

    @Override
    public void deliverWork(TaskInstance task, Map<String, Object> variables, SysUserDO currentUser) {
        deliverWork(task.getId(), variables, currentUser);
    }

    @Override
    public void deliverWork(String taskId, Map<String, Object> variables) {
        deliverWork(taskId, variables, securityUtils.getCurrentUser());
    }

    @Override
    public void deliverWork(String taskId, Map<String, Object> variables, SysUserDO currentUser) {
        Task task = queryUtil.getTaskById(taskId);
        deliverWork(task, variables, currentUser, bpmAdapter.getFlowInstance(task.getProcessInstanceId()));
    }

    private void deliverWork(Task task, Map<String, Object> variables, SysUserDO currentUser, FlowInstance
            flowInstance) {
        // ?????????????????????????????????
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(task.getProcessInstanceId());
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(task.getName());
        if (FlowOutMode.Mode2.isEquals(node.getFlowOutMode())) {
            // ?????????????????????????????????????????????
            throw new BpmException(LocaleUtils.getUnsupporttedAutoDeliver(node.getCode()));
        }
        // ??????????????????????????????????????????????????????
        List<DeliverNode> deliverNodes = getFlowOutNodes(node, flowInstance.getStarterId(), flowInstance.getId());
        DeliverInfo deliverInfo = new DeliverInfo();
        deliverInfo.setTaskId(task.getId());
        deliverInfo.setDeliverNodes(deliverNodes);
        deliverWork(task.getProcessInstanceId(), task, variables, deliverInfo, currentUser);
    }

    @Override
    public void deliverWork(DeliverInfo deliverInfo) {
        deliverWork(new HashMap<>(16), deliverInfo);
    }

    private void deliverWork(Map<String, Object> variables, DeliverInfo deliverInfo) {
        Task task = queryUtil.getTaskById(deliverInfo.getTaskId());
        deliverWork(task.getProcessInstanceId(), task, variables, deliverInfo, securityUtils.getCurrentUser());
    }

    private void deliverWork(String flowInstanceId, Task task, Map<String, Object> variables, DeliverInfo
            deliverInfo, SysUserDO currentUser) {
        Authentication.setAuthenticatedUserId(currentUser.getId());
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(flowInstanceId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(task.getName());
        if (RemarkMode.Required.isEquals(node.getRemarkMode())) {
            if (StringUtils.isBlank(deliverInfo.getOpinion())) {
                throw new BpmException(LocaleUtils.getOpinionIsNull());
            }
        } else if (RemarkMode.Forbidden.isEquals(node.getRemarkMode())) {
            if (StringUtils.isNotBlank(deliverInfo.getOpinion())) {
                deliverInfo.setOpinion("");
            }
        }
        checkDeliverInfo(deliverInfo, flow, task, node);
        // ??????????????????
        WfiTaskPendingDO wfiTaskPending = queryUtil.getWfiTaskPendingByTaskId(wfiFlow.getId(), task.getId(), task.getAssignee());
        UserType userType = UserType.getByValue(wfiTaskPending.getUserType());
        checkAllowDeliver(userType, node, task);
        deliverWork(wfiFlow, flow, node, task, variables, deliverInfo, currentUser, userType);
    }

    /**
     * ??????????????????
     *
     * @param deliverInfo ????????????
     * @param flow        ????????????
     * @param task        ??????
     * @param sourceNode  ????????????
     * @author JonnyJiang
     * @date 2020/11/25 9:39
     */

    private void checkDeliverInfo(DeliverInfo deliverInfo, Flow flow, TaskInfo task, Node sourceNode) {
        if (NodeType.Free.isEquals(sourceNode.getNodeType())) {
            WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverFrom(task.getId());
            DeliverInfo deliverInfoFrom = wfiDeliver.getDeliverInfoClass();
            DeliverNode deliverNodeFrom = deliverInfoFrom.getDeliverNodeByNodeId(sourceNode.getId());
            if (StringUtils.isEmpty(deliverNodeFrom.getNodeFreeStepId())) {
                throw new NodeFreeStepIdIsNullException(task.getId(), wfiDeliver.getId());
            }
            Node targetNode;
            for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
                targetNode = flow.getNodeById(deliverNode.getNodeId());
                checkPreset(sourceNode, targetNode);
                checkTargetNodeAfterFreeNode(task, flow, sourceNode, targetNode, deliverNodeFrom, deliverNode);
            }
        } else {
            // ????????????????????????????????????
            Node targetNode;
            for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
                targetNode = flow.getNodeById(deliverNode.getNodeId());
                checkPreset(sourceNode, targetNode);
                checkTargetNode(task, flow, targetNode, deliverNode);
            }
        }
    }

    private void checkPreset(Node sourceNode, Node targetNode) {
        if (IntegerUtils.isTrue(targetNode.getPresetedNode())) {
            // ???????????????????????????????????????????????????????????????
            return;
        } else if (IntegerUtils.isTrue(sourceNode.getPresetedNode())) {
            // ??????????????????????????????????????????????????????????????????????????????
            throw new TargetNodeMustBePresetedNodeException(targetNode.getId(), targetNode.getName(), targetNode.getCode());
        } else if (IntegerUtils.isTrue(sourceNode.getAllowPresetRoute())) {
            if (IntegerUtils.isTrue(sourceNode.getPresetedRoute())) {
                // ?????????????????????????????????????????????????????????????????????????????????
                throw new TargetNodeMustBePresetedNodeException(targetNode.getId(), targetNode.getName(), targetNode.getCode());
            }
        }
    }

    private void checkTargetNode(TaskInfo task, Flow flow, Node targetNode, DeliverNode deliverNode) {
        checkDeliverUsers(task.getProcessInstanceId(), flow, targetNode, deliverNode);
        if (NodeType.Free.isEquals(targetNode.getNodeType())) {
            // ??????????????????????????????????????????????????????????????????????????????????????????
            InitNodeFreeStep(task, targetNode, deliverNode);
        }
    }

    private void checkTargetNodeAfterFreeNode(TaskInfo task, Flow flow, Node sourceNode, Node targetNode, DeliverNode deliverNodeFrom, DeliverNode deliverNode) {
        if (StringUtils.equals(deliverNode.getNodeId(), sourceNode.getId())) {
            // ???????????????????????????????????????????????????????????????
            /**
             * ??????????????????????????????????????????????????????????????????????????????????????????
             * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
             */
            NodeFreeStep nextStep = sourceNode.getNextNodeFreeStepById(deliverNodeFrom.getNodeFreeStepId());
            if (nextStep == null) {
                throw new NextNodeFreeStepNotFoundException(task.getProcessInstanceId(), sourceNode.getId(), deliverNodeFrom.getNodeFreeStepId());
            }
            if (ObjectUtils.notEqual(deliverNode.getNodeFreeStepId(), nextStep.getId())) {
                throw new NodeFreeStepIsNotNextStepException(task.getProcessInstanceId(), sourceNode.getId(), deliverNode.getNodeFreeStepId(), nextStep.getId());
            }
            deliverNode.setNodeFreeStepId(nextStep.getId());
        } else {
            // ???????????????????????????????????????????????????
            if (NodeType.Free.isEquals(targetNode.getNodeType())) {
                InitNodeFreeStep(task, targetNode, deliverNode);
            }
        }
        checkDeliverUsers(task.getProcessInstanceId(), flow, targetNode, deliverNode);
    }

    private void InitNodeFreeStep(TaskInfo task, Node targetNode, DeliverNode deliverNode) {
        NodeFreeStep firstStep = targetNode.getFirstNodeFreeStep();
        if (firstStep == null) {
            throw new NodeFreeStepNotSetException(task.getProcessInstanceId(), targetNode.getId(), targetNode.getName());
        } else {
            deliverNode.setNodeFreeStepId(firstStep.getId());
        }
    }

    private void checkDeliverUsers(String wfiFlowId, Flow flow, Node targetNode, DeliverNode deliverNode) {
        long hostCnt = deliverNode.getDeliverUsers().stream().filter(o -> UserType.Host.isEquals(o.getUserType())).count();
        if (HostMode.Specified.isEquals(targetNode.getHostMode())) {
            // ????????????1????????????
            if (hostCnt != 1) {
                throw new BpmException(LocaleUtils.getHostCntNotMatch(targetNode.getCode(), targetNode.getName(), hostCnt, 1));
            }
        } else {
            // ?????????????????????
            if (hostCnt > 0) {
                throw new BpmException(LocaleUtils.getHostCntNotMatch(targetNode.getCode(), targetNode.getName(), hostCnt, 0));
            }
        }
        // ????????????????????????
        List<DeliverUser> deliverUsers = deliverNode.getDeliverUsers();
        if (deliverUsers.size() != deliverNode.getDeliverUsers().stream().map(DeliverUser::getUserId).distinct().count()) {
            throw new DeliverUserDuplicateException(targetNode, deliverUsers);
        }
        if (AllowPassMode.None.isEquals(targetNode.getAllowPassMode()) || AllowPassMode.SpecifiedUserAllSign.isEquals(targetNode.getAllowPassMode())) {
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            List<String> userIds = targetNode.getWaitForPassUsers().stream().filter(o -> deliverUsers.stream().noneMatch(u -> StringUtils.equals(o, u.getUserId()))).collect(Collectors.toList());
            if (userIds.size() > 0) {
                // ????????????????????????????????????????????????????????????
                List<SysUserDO> sysUsers = iUser.getUsersByIds(userIds);
                if (Objects.equals(sysUsers.size(), userIds.size())) {
                    // ????????????????????????????????????
                    throw new DeliverUsersNotContainsWaitForPassUserException(wfiFlowId, targetNode, String.join(",", sysUsers.stream().map(SysUserDO::getRealName).collect(Collectors.toList())));
                } else {
                    for (String userId : userIds) {
                        if (sysUsers.stream().noneMatch(o -> StringUtils.equals(userId, o.getId()))) {
                            throw new SysUserNotFoundByIdException(userId);
                        }
                    }
                }
            }
        } else if (AllowPassMode.ExceedCount.isEquals(targetNode.getAllowPassMode())) {
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (deliverUsers.size() < targetNode.getWaitForPassCount()) {
                throw new WaitForPassCountNotEnoughException(wfiFlowId, targetNode, deliverUsers.size());
            }
        }
    }

    @Override
    public void checkAllowDeliver(UserType userType, Node node, TaskInstance task) {
        WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverFrom(task.getId());
        queryUtil.checkAllowDeliver(userType, node, task.getFlowInstanceId(), task.getId(), task.getOwner(), task.getAssignee(), wfiDeliver);
    }

    private void checkAllowDeliver(UserType userType, Node node, Task task) {
        WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverFrom(task.getId());
        queryUtil.checkAllowDeliver(userType, node, task.getProcessInstanceId(), task.getId(), task.getOwner(), task.getAssignee(), wfiDeliver);
    }

    /**
     * ????????????????????????
     *
     * @param flowInstanceId ????????????id
     * @param taskId         ??????id
     * @param userType       ????????????
     * @param currentUser    ????????????
     * @param opinion        ????????????
     * @author JonnyJiang
     * @date 2020/5/25 20:09
     */

    private void addComment(String flowInstanceId, String taskId, UserType userType, SysUserDO currentUser, String opinion) {
        WfiCommentDO wfiComment = new WfiCommentDO();
        wfiComment.setCommentUserId(currentUser.getId());
        wfiComment.setCommentUser(currentUser.getRealName());
        wfiComment.setFlowId(flowInstanceId);
        wfiComment.setCommentTime(LocalDateTime.now());
        wfiComment.setTaskId(taskId);
        wfiComment.setCommentText(opinion);
        wfiComment.setAppId(securityUtils.getAppName());
        wfiComment.setUserType(userType.getValue());
        wfiCommentService.save(wfiComment);
//        taskService.addComment(task.getId(), task.getProcessInstanceId(), message);
    }

    private WfiDeliverDO getWfiDeliver(String flowId, DeliverInfo deliverInfo, String userId) {
        WfiDeliverDO wfiDeliver = new WfiDeliverDO();
        wfiDeliver.setId(UUID.randomUUID().toString());
        wfiDeliver.setFlowId(flowId);
        wfiDeliver.setUserId(userId);
        wfiDeliver.setDeliverInfo(JSONObject.toJSONString(deliverInfo));
        wfiDeliver.setDeliverTime(LocalDateTime.now());
        wfiDeliverService.save(wfiDeliver);
        return wfiDeliver;
    }

    private void initVariables(Flow flow, DeliverInfo deliverInfo, Map<String, Object> variables) {
        List<String> nodeIds = new ArrayList<>();
        List<String> firstArrivingNodeIds = new ArrayList<>();
        for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
            String deliverNodeId = deliverNode.getNodeId();
            nodeIds.add(deliverNodeId);
            Node actualNode = flow.getNodeById(deliverNodeId);
            if (FlowInMode.Any.isEquals(actualNode.getFlowInMode())) {
                /*
                ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                 */
                if (isFirstArriving()) {
                    firstArrivingNodeIds.add(deliverNodeId);
                }
            }
        }
        variables.put(TaskVariableName.FIRST_ARRIVING_NODE_IDS.getName(), firstArrivingNodeIds);
        variables.put(TaskVariableName.NODE_IDS.getName(), nodeIds);
    }

    private void deliverWork(WfiFlowDO wfiFlow, Flow flow, Node node, Task
            task, Map<String, Object> variables, DeliverInfo deliverInfo, SysUserDO currentUser, UserType userType) {
        List<Node> nextNodes = flow.getNextNodesByNodeId(task.getName());
        for (Node nextNode : nextNodes) {
            if (NodeType.Subflow.isEquals(nextNode.getNodeType())) {
                WfdVFlowDO wfdVFlow = queryUtil.getEffectiveWfdVFlowByFlowId(nextNode.getSubFlowId());
                if (!IntegerUtils.isTrue(wfdVFlow.getUsed())) {
                    deployToEngine(wfdVFlow);
                }
            }
        }
        variables.put(TaskVariableName.OPTION.getName(), deliverInfo.getOpinion());
        initVariables(flow, deliverInfo, variables);
        HttpSession httpSession = securityUtils.getSession();
        SessionAttribute.initElSession(httpSession, wfiFlow.getId(), flow, wfiFlow.getBusinessKey());
        FlowUtils.initDeliverNodes(flow, deliverInfo, wfiFlow.getBusinessKey());
        WfiDeliverDO wfiDeliver = getWfiDeliver(wfiFlow.getId(), deliverInfo, currentUser.getId());
        taskService.setVariableLocal(task.getId(), TaskVariableName.WFI_DELIVER_ID.getName(), wfiDeliver.getId());
        httpSession.setAttribute(SessionAttribute.TASK_WFD_DELIVER, wfiDeliver);
        httpSession.setAttribute(SessionAttribute.TASK_DELIVER_INFO, deliverInfo);
        onTaskCompleting(wfiFlow, flow, node, task, variables, deliverInfo);
        addComment(task.getProcessInstanceId(), task.getId(), userType, currentUser, deliverInfo.getOpinion());
        BpmnModel bpmnModel = queryUtil.getBpmnModel(task.getProcessDefinitionId());
        BpmnUtil.initUserTasks(flow, bpmnModel, deliverInfo, variables);
        List<WfdMessageVO> messageVOS = new ArrayList<>();
        List<WfdMessageEventVO> eventVOS = new ArrayList<>();
        // ????????????
        if (nextNodes.stream().anyMatch(n -> NodeType.End.isEquals(n.getNodeType()))) {
            Map<String, Object> data = new HashMap<>();
            data.put("workFlowName", wfiFlow.getFlowNo());
            data.put("flowCode", flow.getCode());
            data.put("formId", wfiFlow.getBusinessKey());
            data.putAll(getMessageVariableFields(flow, node.getId(), false));
            // ????????????????????????????????? ?????????????????????
            // 2020-4-27
            List<String> userIds = getReceiveUserIds(flow, node.getId(), wfiFlow.getId(), new ArrayList<>());
            messageVOS.add(new WfdMessageVO(userIds,
                    Constants.BpmFinshNoticeChannelName,
                    getMessageTemplate(flow, node.getId(), Constants.BpmFinshNoticeTemplate, false), data));
            Set<String> userIdSet = new HashSet<>(userIds);
            userIdSet.add(securityUtils.getCurrentUserId());
            eventVOS.add(new WfdMessageEventVO(new ArrayList<>(userIdSet),
                    Constants.BpmFinshNoticeEvent, null));
        }

        doDeliverWork(flow, bpmnModel, node, currentUser, task, userType, variables, wfiDeliver, messageVOS, eventVOS, deliverInfo);
        onTaskCompleted(wfiFlow, flow, node, task, variables, deliverInfo);
        onFlowEnded(wfiFlow, flow);

        // ??????????????????????????????????????????
        updateFocusWorkReadFlagByProcessInsId(wfiFlow.getId());

        sysAuditLogService.saveLog(LocaleUtils.getFlowOperationDeliver(), LocaleUtils.getFlowOperationDeliverLog(wfiFlow.getFlowCode(), node.getCode(), node.getName(), task.getId()), LocaleUtils.getFlowOperation());

        // ???????????? 2020-4-27
        // ?????? ?????? ???????????????
        sendWfdMsg(messageVOS);
        sendWfdEventMsg(eventVOS);
    }

    private void doDeliverWork(Flow flow, BpmnModel bpmnModel, Node node, SysUserDO currentUser, TaskInfo task, UserType userType, Map<String, Object> variables,
                               WfiDeliverDO wfiDeliver, List<WfdMessageVO> messageVOS, List<WfdMessageEventVO> eventVOS, DeliverInfo deliverInfo) {
        Expression expressionOri = null;
        if (UserType.Host.equals(userType)) {
            // ????????????????????????????????????????????????????????????????????????????????????=?????????id
            UserTask userTask = BpmnUtil.getUserTaskByNodeId(flow, bpmnModel, node);
            if (userTask.getBehavior() instanceof MultiInstanceActivityBehavior) {
                MultiInstanceActivityBehavior behavior = (MultiInstanceActivityBehavior) userTask.getBehavior();
                behavior.setCollectionVariable(ManualNode.ASSIGNEE_LIST);
                List<String> assigneeList = new ArrayList<>();
                assigneeList.add(currentUser.getId());
                runtimeService.setVariable(task.getExecutionId(), ManualNode.ASSIGNEE_LIST, assigneeList);
                expressionOri = behavior.getCompletionConditionExpression();
                Expression expression = queryUtil.getExpressionManager().createExpression("${1>0}");
                behavior.setCompletionConditionExpression(expression);
            }
            // ????????????????????????????????????????????????????????????
            updateDeliverInfo(node, task, currentUser.getId());
        }
        if (StringUtils.isNotBlank(task.getOwner())) {
            taskService.resolveTask(task.getId(), variables);
        }
        complete(node, task, variables, deliverInfo, wfiDeliver.getFlowId(), wfiDeliver.getId(), bpmnModel);
        autoDelegate(task, flow, wfiDeliver, messageVOS, eventVOS);
        if (UserType.Host.equals(userType)) {
            // ??????????????????d
            UserTask userTask = BpmnUtil.getUserTaskByNodeId(flow, bpmnModel, node);
            if (userTask.getBehavior() instanceof MultiInstanceActivityBehavior) {
                MultiInstanceActivityBehavior behavior = (MultiInstanceActivityBehavior) userTask.getBehavior();
                behavior.setCompletionConditionExpression(expressionOri);
            }
        }
        refreshTaskPending(task.getProcessInstanceId(), flow);
    }

    private void complete(Node node, TaskInfo task, Map<String, Object> variables, DeliverInfo deliverInfo, String flowInstanceId, String wfiDeliverIdFrom, BpmnModel bpmnModel) {
        taskService.complete(task.getId(), variables);
        resolveMultiInstance(deliverInfo, node.getFlow(), flowInstanceId, wfiDeliverIdFrom, bpmnModel, variables);
    }

    private void resolveMultiInstance(DeliverInfo deliverInfo, Flow flow, String flowInstanceId, String wfiDeliverIdFrom, BpmnModel bpmnModel, Map<String, Object> variables) {
        for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
            Node targetNode = flow.getNodeById(deliverNode.getNodeId());
            if (NodeType.Manual.isEquals(targetNode.getNodeType()) || NodeType.Free.isEquals(targetNode.getNodeType())) {
                resolveMultiInstance(targetNode, flowInstanceId, wfiDeliverIdFrom, deliverNode, BpmnUtil.getUserTaskByNodeId(flow, bpmnModel, targetNode), variables);
            }
        }
    }

    private void mapAdd(Map<String, List<String>> map, String key, String value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            List<String> l = new ArrayList<>();
            l.add(value);
            map.put(key, l);
        }
    }

    private void resolveMultiInstance(Node node, String flowInstanceId, String wfiDeliverIdFrom, DeliverNode deliverNode, UserTask userTask, Map<String, Object> variables) {
        if (!MultiInstanceMode.Single.isEquals(node.getMultiInstanceMode())) {
            if (deliverNode.getDeliverUsers().size() > 1) {
                // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                List<Task> tasks = queryUtil.listUnfinishedTasks(flowInstanceId, wfiDeliverIdFrom, node.getId());
                if (tasks.size() > 0) {
                    Map<String, List<String>> multiMap = new HashMap<>(16);
                    DeliverUser firstDeliverUser = deliverNode.getDeliverUsers().get(0);
                    if (MultiInstanceMode.Department.isEquals(node.getMultiInstanceMode())) {
                        for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
                            if (ObjectUtils.notEqual(firstDeliverUser.getDepartmentId(), deliverUser.getDepartmentId())) {
                                mapAdd(multiMap, deliverUser.getDepartmentId(), deliverUser.getUserId());
                            }
                        }
                    } else if (MultiInstanceMode.Organization.isEquals(node.getMultiInstanceMode())) {
                        for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
                            if (ObjectUtils.notEqual(firstDeliverUser.getOrganizationId(), deliverUser.getOrganizationId())) {
                                mapAdd(multiMap, deliverUser.getOrganizationId(), deliverUser.getUserId());
                            }
                        }
                    } else if (MultiInstanceMode.Group.isEquals(node.getMultiInstanceMode())) {
                        for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
                            if (ObjectUtils.notEqual(firstDeliverUser.getGroupId(), deliverUser.getGroupId())) {
                                mapAdd(multiMap, deliverUser.getGroupId(), deliverUser.getUserId());
                            }
                        }
                    }
                    if (multiMap.size() > 0) {
                        Task task = tasks.get(0);
                        multiMap.forEach((deptId, userIds) -> {
                            queryUtil.getSpringProcessEngineConfiguration().getCommandExecutor().execute(new MultiInstanceCmd(
                                    userTask, node, userIds, variables, task
                            ));
                        });
                    }
                }
            }
        }
    }

    /**
     * ????????????
     *
     * @param task       ?????????????????????
     * @param flow       ????????????
     * @param wfiDeliver ????????????
     * @author JonnyJiang
     * @date 2020/4/26 21:16
     */

    private void autoDelegate(TaskInfo task, Flow flow, WfiDeliverDO wfiDeliver,
                              List<WfdMessageVO> messageVOS, List<WfdMessageEventVO> eventVOS) {
        List<Task> runningTasks = queryUtil.listTasks(task.getProcessInstanceId());
        Map<String, Object> deliverInfoIdFroms = queryUtil.listTaskVariableValues(runningTasks.stream().map(Task::getId).collect(Collectors.toSet()), TaskVariableName.WFI_DELIVER_ID_FROM, true);
        for (Task runningTask : runningTasks) {
            String deliverInfoIdFrom = (String) deliverInfoIdFroms.get(runningTask.getId());
            if (wfiDeliver.getId().equals(deliverInfoIdFrom)) {
                // ?????????????????????task??????????????????????????????task
                delegateWork(flow.getId(), runningTask, null, task, messageVOS, eventVOS);
            }
        }
    }

    private void updateDeliverInfo(Node node, TaskInfo task, String currentUserId) {
        if (HostMode.AllowDeliver.isEquals(node.getHostMode()) || HostMode.Everybody.isEquals(node.getHostMode())) {
            // ??????claimTime
            deliverUserClaim(task, currentUserId, node.getFlow());
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @return java.lang.Boolean
     * @author JonnyJiang
     * @date 2019/12/5 8:30
     */

    private Boolean isFirstArriving() {
        return true;
    }

    private void onTaskCompleted(WfiFlowDO wfiFlow, Flow flow, Node node, Task task, Map<String, Object> variables, DeliverInfo deliverInfo) {
        syncSettings(flow, wfiFlow);
        for (NodeEvent event : node.getEvents()) {
            if (TaskEventType.Completed.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getTaskListenerInstance(TaskEventType.Completed, event.getEventClass());
                if (listener instanceof TaskCompletedListener) {
                    ((TaskCompletedListener) listener).notify(new TaskCompletedEventArgs(wfiFlow, flow, node, new TaskInstanceImpl(task, wfiFlow), variables, deliverInfo));
                }
            }
        }
//        for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
//            List<String> receiverIds = new ArrayList<>();
//            iMessage.sendMessage(receiverIds, node.getFlowInMsgChannel(), "", );
//        }
    }

    private void onFlowEnded(WfiFlowDO wfiFlow, Flow flow) {
        HistoricProcessInstance historicProcessInstance = queryUtil.getHistoricProcessInstance(wfiFlow.getId());
        if (historicProcessInstance.getEndTime() != null) {
            // ?????????????????????
            List<HistoricActivityInstance> historicActivityInstances = queryUtil.getHistoricActivityInstanceQuery(wfiFlow.getId()).activityId(historicProcessInstance.getEndActivityId()).orderByHistoricActivityInstanceEndTime().desc().list();
            if (historicActivityInstances.size() == 0) {
                throw new BpmException(LocaleUtils.getHistoricActivitiInstanceNotFoundById(historicProcessInstance.getEndActivityId()));
            }
            HistoricActivityInstance historicActivityInstance = historicActivityInstances.get(0);
            if (BpmnXMLConverter.ELEMENT_EVENT_END.equals(historicActivityInstance.getActivityType())) {
                Node endNode = flow.getNodeById(historicActivityInstance.getActivityName());
                String result = endNode.getResultValue();
                if (IntegerUtils.isTrue(endNode.getSaveResultValue())) {
                    // ?????????????????????
                    wfiFlowService.syncData(flow.getFormDataTable(), flow.getFormIdName(), wfiFlow.getBusinessKey(), flow.getFormFieldByName(flow.getFormResultField()).getName(), result);
                }
                for (Event event : flow.getEvents()) {
                    if (FlowEventType.Ended.isEquals(event.getEventType())) {
                        Object listener = ListenerScanner.getFlowListenerInstance(FlowEventType.Ended, event.getClassName());
                        if (listener instanceof FlowEndedListener) {
                            ((FlowEndedListener) listener).notify(new FlowEndEventArgs(flow, endNode, wfiFlow.getId(), wfiFlow.getBusinessKey(), result));
                        }
                    }
                }
            } else {
                throw new BpmException(LocaleUtils.getUnsupporttedEndActivityType(historicActivityInstance.getActivityType()));
            }
        }
    }

    private void syncSettings(Flow flow, WfiFlowDO wfiFlow) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(WfdFlowElService.DATE_FORMAT_PATTERN);
        SessionAttribute.initElSession(securityUtils.getSession(), wfiFlow.getId(), flow, wfiFlow.getBusinessKey());
        List<WfdGlobalVariantDO> wfdGlobalVariants = wfdGlobalVariantService.list(new QueryWrapper<WfdGlobalVariantDO>().eq("APP_ID", securityUtils.getAppName()));
        // ????????????
        for (SyncSetting syncSetting : flow.getSyncSettings()) {
            Variant variant = getVariant(wfdGlobalVariants, flow, syncSetting);
            String value = ruleUtils.getValue(variant);
            Object val = value;
            if (StringUtils.isNotEmpty(value)) {
                if (StringUtils.isNotEmpty(variant.getDataType())) {
                    if (DataType.Boolean.equals(variant.getDataType())) {
                        val = Integer.parseInt(value);
                    } else if (DataType.DateTime.equals(variant.getDataType())) {
                        try {
                            val = simpleDateFormat.parse(value);
                        } catch (ParseException e) {
                            LOGGER.error(e.getMessage());
                            throw new BpmException(e.getMessage());
                        }
                    } else if (DataType.Integer.equals(variant.getDataType())) {
                        val = Integer.parseInt(value);
                    } else if (DataType.String.equals(variant.getDataType())) {
                        val = value;
                    } else {
                        throw new BpmException(LocaleUtils.getUnsupportedVariantDataType(variant.getDataType()));
                    }
                }
            }
            wfiFlowService.syncData(flow.getFormDataTable(), flow.getFormIdName(), wfiFlow.getBusinessKey(), flow.getFormFieldById(syncSetting.getFieldName()).getName(), val);
        }
    }

    private Variant getVariant(List<WfdGlobalVariantDO> wfdGlobalVariants, Flow flow, SyncSetting syncSetting) {
        if (StringUtils.isEmpty(syncSetting.getVariantName())) {
            throw new BpmException(LocaleUtils.getSettingSyncVariantNameIsNull(syncSetting.getId()));
        }
        if (FlowUtils.isFlowVariant(syncSetting.getVariantName())) {
            String variantId = FlowUtils.getFlowVariantId(syncSetting.getVariantName());
            return flow.getVariantById(variantId);
        } else if (FlowUtils.isGlobalVariant(syncSetting.getVariantName())) {
            String variantId = FlowUtils.getGlobalVariantId(syncSetting.getVariantName());
            return FlowUtils.getGlobalVariantById(wfdGlobalVariants, variantId);
        }
        throw new BpmException(LocaleUtils.getVariantNotFound(syncSetting.getVariantName(), flow.getId()));
    }

    private void onTaskCompleting(WfiFlowDO wfiFlow, Flow flow, Node node, Task task, Map<String, Object> variables, DeliverInfo deliverInfo) {
        backupFlowInstance(wfiFlow.getId(), task.getId(), LocaleUtils.getTaskCompleting(node.getName()));
        for (NodeEvent event : node.getEvents()) {
            if (TaskEventType.Completing.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getTaskListenerInstance(TaskEventType.Completing, event.getEventClass());
                if (listener instanceof TaskCompletingListener) {
                    ((TaskCompletingListener) listener).notify(new TaskCompletingEventArgs(wfiFlow, flow, node, new TaskInstanceImpl(task, wfiFlow), variables, deliverInfo));
                }
            }
        }
    }

    @Override
    public void deleteFlowInstanceByFlowId(String flowId, List<String> businessKeys, String deleteReason) {
        SysUserDO currentUser = securityUtils.getCurrentUser();
        Authentication.setAuthenticatedUserId(currentUser.getId());
        LOGGER.debug("flowId: " + flowId);
        LOGGER.debug("businessKeys: " + businessKeys);
        if (businessKeys.size() > 0) {
            Collection<WfiFlowDO> wfiFlows = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().eq("FLOW_ID", flowId).in("BUSINESS_KEY", businessKeys));
            List<String> ids = wfiFlows.stream().map(WfiFlowDO::getId).collect(Collectors.toList());
            List<WfiTaskPendingDO> wfiTaskPendings = wfiTaskPendingService.listByFlowIds(ids);
            List<WfiTaskPendingDO> currentWfiTaskPendings;
            for (WfiFlowDO wfiFlow : wfiFlows) {
                currentWfiTaskPendings = wfiTaskPendings.stream().filter(o -> StringUtils.equals(o.getFlowId(), wfiFlow.getId())).collect(Collectors.toList());
                doDeleteFlowInstance(wfiFlow, currentWfiTaskPendings, deleteReason, currentUser);
            }
        }
    }

    private void doDeleteFlowInstance(WfiFlowDO wfiFlow, List<WfiTaskPendingDO> wfiTaskPendings, String deleteReason, SysUserDO currentUser) {
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        // ???????????????????????????????????????????????????????????????
        Boolean pendingDelete = true;
        BpmException currentUserException = null;
        for (WfiTaskPendingDO wfiTaskPending : wfiTaskPendings) {
            if (StringUtils.equals(wfiTaskPending.getUserId(), currentUser.getId())) {
                try {
                    deleteFlowInstanceByTaskPending(wfiFlow, flow, wfiTaskPending, wfiFlow.getBusinessKey(), deleteReason);
                    pendingDelete = false;
                } catch (BpmException ex) {
                    ex.printStackTrace();
                    if (currentUserException == null) {
                        currentUserException = ex;
                    }
                }
            }
        }
        Boolean pendingDeleteAdmin = true;
        BpmException adminException = null;
        if (pendingDelete) {
            // ??????????????????????????????????????????????????????????????????????????????
            if (bpmAdapter.hasAdminAuth(flow, currentUser)) {
                for (WfiTaskPendingDO wfiTaskPending : wfiTaskPendings) {
                    if (ObjectUtils.notEqual(wfiTaskPending.getUserId(), currentUser.getId())) {
                        try {
                            deleteFlowInstanceByTaskPending(wfiFlow, flow, wfiTaskPending, wfiFlow.getBusinessKey(), deleteReason);
                            pendingDeleteAdmin = false;
                        } catch (BpmException ex) {
                            if (adminException == null) {
                                adminException = ex;
                            }
                            ex.printStackTrace();
                        }
                    }
                }
            } else {
                if (currentUserException == null) {
                    throw new NoAdminAuthException(flow);
                } else {
                    throw currentUserException;
                }
            }
            if (pendingDeleteAdmin) {
                // ????????????????????????
                if (adminException == null) {
                    // ????????????????????????????????????????????????
                    if (currentUserException == null) {
                        // ?????????????????????????????????????????????????????????????????????????????????????????????
                        if (wfiTaskPendings.size() == 0) {
                            throw new NoTaskPendingException(wfiFlow.getFlowId());
                        } else {
                            // ?????????wfiTaskPendings???????????????????????????flowId???????????????????????????
                            throw new BpmException(wfiTaskPendings.toString());
                        }
                    } else {
                        throw currentUserException;
                    }
                } else {
                    // ???????????????????????????????????????
                    throw adminException;
                }
            }
        }
    }

    @Override
    public void deleteFlowInstanceByCode(String code, List<String> businessKeys, String deleteReason) {
        WfdVFlowDO wfdVFlow = bpmAdapter.getEffectiveWfdVFlowByCode(code);
        deleteFlowInstanceByFlowId(wfdVFlow.getFlowId(), businessKeys, deleteReason);
    }

    @Override
    public void deleteFlowInstanceById(String flowInstanceId, String deleteReason) {
        ProcessInstance processInstance = queryUtil.getProcessInstanceQuery(flowInstanceId).singleResult();
        if (processInstance == null) {
            if (queryUtil.getHistoricProcessInstanceQuery(flowInstanceId).count() > 0) {
                throw new BpmException(LocaleUtils.getDeleteCompletedFlowError(flowInstanceId));
            }
        } else {
            deleteFlowInstance(flowInstanceId, deleteReason);
        }
    }

    @Override
    public void deleteFlowInstanceByBusinessKey(String code, String businessKey, String deleteReason) {
        ProcessInstance processInstance = queryUtil.getProcessInstanceQuery().processDefinitionName(code).processInstanceBusinessKey(businessKey).singleResult();
        if (processInstance == null) {
            List<HistoricProcessInstance> historicProcessInstances = queryUtil.getHistoricProcessInstanceQuery().processDefinitionName(code).processInstanceBusinessKey(businessKey).list();
            if (historicProcessInstances.size() > 0) {
                throw new BpmException(LocaleUtils.getDeleteCompletedFlowError(historicProcessInstances.get(0).getId()));
            }
        } else {
            deleteFlowInstance(processInstance.getId(), deleteReason);
        }
    }

    private void deleteFlowInstance(String flowInstanceId, String deleteReason) {
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(flowInstanceId);
        List<WfiTaskPendingDO> wfiTaskPendings = wfiTaskPendingService.listByFlowId(wfiFlow.getId());
        doDeleteFlowInstance(wfiFlow, wfiTaskPendings, deleteReason, securityUtils.getCurrentUser());
    }

    @Override
    public void forceDeleteFlowInstanceByBusinessKey(String flowCode, List<String> businessKeys, String deleteReason) {
        List<WfiFlowDO> wfiFlows = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().eq("flow_code", flowCode).in("business_key", businessKeys).eq("app_id", securityUtils.getAppName()));
        for (WfiFlowDO wfiFlow : wfiFlows) {
            Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
            doDeleteFlowInstance(wfiFlow.getId(), deleteReason, flow, wfiFlow.getBusinessKey());
            sysAuditLogService.saveLog(LocaleUtils.getFlowOperationDelete(), LocaleUtils.getFlowOperationDeleteLog(wfiFlow.getFlowCode(), wfiFlow.getId(), deleteReason), LocaleUtils.getFlowOperation());
        }
    }

    @Override
    public void forceDeleteFlowInstanceByBusinessKey(String flowCode, String[] businessKeys, String deleteReason) {
        forceDeleteFlowInstanceByBusinessKey(flowCode, Arrays.asList(businessKeys), deleteReason);
    }

    private void deleteFlowInstance(Flow flow, WfiFlowDO wfiFlow, String businessKey, String deleteReason) {
        onFlowDeleting(flow, wfiFlow, businessKey, deleteReason);
        doDeleteFlowInstance(wfiFlow.getId(), deleteReason, flow, businessKey);
        refreshTaskPending(wfiFlow.getId(), flow);
        onFlowDeleted(flow, wfiFlow, businessKey, deleteReason);
    }

    private void doDeleteFlowInstance(String flowInstanceId, String deleteReason, Flow flow, String businessKey) {
        if (StringUtils.isNotEmpty(flowInstanceId)) {
            AbstractDbHelper.getDbHelper().clear(flowInstanceId);
            // ??????????????????????????????
            wfiFlowService.removeById(flowInstanceId);
            wfiBackupService.remove(new QueryWrapper<WfiBackupDO>().eq("app_id", securityUtils.getAppName()).eq("flow_id", flowInstanceId));
            wfiFocusedWorkService.remove(new QueryWrapper<WfiFocusedWorkDO>().eq("app_id", securityUtils.getAppName()).eq("flow_instance_id", flowInstanceId));
        }
        if (flow != null) {
            if (IntegerUtils.isTrue(flow.getFormCascadeDel())) {
                // ??????????????????
                wfiFlowService.deleteFormByBusinessKey(flow.getFormDataTable(), flow.getFormIdName(), businessKey);
            }
        }
    }

    private void onFlowDeleted(Flow flow, WfiFlowDO wfiFlow, String businessKey, String deleteReason) {
        for (Event event : flow.getEvents()) {
            if (FlowEventType.Deleted.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getFlowListenerInstance(FlowEventType.Deleted, event.getClassName());
                if (listener instanceof FlowDeletedListener) {
                    ((FlowDeletedListener) listener).notify(new FlowDeletedEventArgs(flow, wfiFlow, businessKey, deleteReason));
                }
            }
        }
        sysAuditLogService.saveLog(LocaleUtils.getFlowOperationDelete(), LocaleUtils.getFlowOperationDeleteLog(wfiFlow.getFlowCode(), wfiFlow.getId(), deleteReason), LocaleUtils.getFlowOperation());
    }

    private void onFlowDeleting(Flow flow, WfiFlowDO wfiFlow, String businessKey, String deleteReason) {
        for (Event event : flow.getEvents()) {
            if (FlowEventType.Deleting.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getFlowListenerInstance(FlowEventType.Deleting, event.getClassName());
                if (listener instanceof FlowDeletingListener) {
                    ((FlowDeletingListener) listener).notify(new FlowDeletingEventArgs(flow, wfiFlow, businessKey, deleteReason));
                }
            }
        }
    }

    @Override
    public void deploy(String id) {
        deploy(id, FIRST_FLOW_VERSION, LocalDateTime.now().plusDays(1));
    }

    @Override
    public void deploy(String id, LocalDateTime startDate) {
        deploy(id, FIRST_FLOW_VERSION, startDate);
    }

    private void validateVersion(Integer maxFlowVersion, Integer targetVersion) {
        if (maxFlowVersion > targetVersion) {
            throw new BpmException(LocaleUtils.getWfdVFlowBiggerVersionExist(maxFlowVersion));
        } else if (maxFlowVersion.equals(targetVersion)) {
            throw new BpmException(LocaleUtils.getWfdVFlowVersionExist(targetVersion));
        } else if (maxFlowVersion < targetVersion - 1) {
            throw new BpmException(LocaleUtils.getWfdVFlowSmallerVersionExist(targetVersion - 1));
        }
    }

    @Override
    public void deploy(String id, Integer version, LocalDateTime beginDate) {
        WfdVFlowDO wfdVFlow = new WfdVFlowDO();
        // ????????????????????????
        beginDate = beginDate.withNano(0);
        WfdVFlowDO maxVersionWfdVFlow = wfdVFlowService.getMaxVersion(id);
        Integer maxFlowVersion = 0;
        if (maxVersionWfdVFlow != null) {
            maxFlowVersion = maxVersionWfdVFlow.getFlowVersion();
            // ?????????????????????????????????????????????????????????
            if (maxVersionWfdVFlow.getVersionBeginDate() == null) {
                throw new BpmException(LocaleUtils.getWfdVFlowVersionBeginDateIsNull(maxVersionWfdVFlow.getId()));
            }
            if (ObjectUtils.compare(maxVersionWfdVFlow.getVersionBeginDate().withNano(0), beginDate) >= 0) {
                throw new BpmException(LocaleUtils.getWfdVFlowVersionBeginDateError(maxVersionWfdVFlow.getVersionBeginDate()));
            }
        }
        validateVersion(maxFlowVersion, version);
        wfdVFlow.setFlowVersion(version);
        LOGGER.debug("loading flow: " + id);
        WfdFlowDO wfdFlow = wfdFlowService.getById(id);
        if (wfdFlow == null) {
            throw new BpmException(LocaleUtils.getWfdFlowNotFound(id));
        }
        // ????????????????????????????????????
        LocalDateTime endDate = beginDate.minusNanos(1);
        wfdVFlowService.updateVersionEndDate(id, version - 1, endDate);
        Flow flow = FlowUtils.getFlow(wfdFlow.getModel());
        FlowValidator.validate(flow);
        String bpmn = getBpmnXml(flow);
        wfdVFlowService.updateLatestByFlowId(id, 0);
        wfdVFlow.setName(flow.getName());
        wfdVFlow.setCode(flow.getCode());
        wfdVFlow.setSortNo(flow.getSortNo());
        wfdVFlow.setCategoryId(flow.getCategoryId());
        wfdVFlow.setDescription(flow.getDescription());
        wfdVFlow.setAdminAuthId(flow.getAdminAuthId());
        wfdVFlow.setQueryAuthId(flow.getQueryAuthId());
        wfdVFlow.setFlowId(flow.getId());
        wfdVFlow.setLatest(1);
        wfdVFlow.setModel(wfdFlow.getModel());
        wfdVFlow.setBpmn(bpmn);
        wfdVFlow.setFormDataTable(flow.getFormDataTable());
        wfdVFlow.setFormDataSourceId(flow.getFormDatasourceId());
        wfdVFlow.setInitAuthId(wfdFlow.getInitAuthId());
        wfdVFlow.setVersionBeginDate(beginDate);
        wfdVFlow.setAppId(wfdFlow.getAppId());
        wfdVFlowService.save(wfdVFlow);
        sysAuditLogService.saveLog(LocaleUtils.getFlowOperationDeploy(), LocaleUtils.getFlowOperationDeployLog(wfdVFlow.getCode(), wfdVFlow.getName(), wfdVFlow.getFlowVersion()), LocaleUtils.getFlowOperation());
    }

    @Override
    public void revokeDeploy(String flowId, Integer version) {
        wfdVFlowService.updateLatestByFlowId(flowId, 0);
        // ??????????????????
        // ????????????????????????????????????
        List<WfdVFlowDO> wfdVFlows = wfdVFlowService.list(new QueryWrapper<WfdVFlowDO>().eq("FLOW_ID", flowId).ge("FLOW_VERSION", version).orderByDesc("FLOW_VERSION"));
        if (wfdVFlows.size() == 0) {
            throw new BpmException(LocaleUtils.getWfdVFlowVersionNotExist(version));
        }
        WfdVFlowDO targetWfdVFlow = wfdVFlows.get(wfdVFlows.size() - 1);
        if (!targetWfdVFlow.getFlowVersion().equals(version)) {
            throw new BpmException(LocaleUtils.getWfdVFlowVersionNotExist(targetWfdVFlow.getFlowVersion()));
        }
        wfdVFlows.remove(targetWfdVFlow);
        for (WfdVFlowDO wfdVFlow : wfdVFlows) {
            if (IntegerUtils.isTrue(wfdVFlow.getUsed())) {
                throw new BpmException(LocaleUtils.getWfdVFlowUsed(wfdVFlow.getFlowVersion()));
            }
            wfdVFlowService.removeById(wfdVFlow.getId());
        }
        targetWfdVFlow.setLatest(1);
        wfdVFlowService.update(new WfdVFlowDO(), new UpdateWrapper<WfdVFlowDO>().set("VERSION_END_DATE", null).set("IS_LATEST", 1).eq("ID", targetWfdVFlow.getId()));
        sysAuditLogService.saveLog(LocaleUtils.getFlowOperationRevokeDeploy(), LocaleUtils.getFlowOperationRevokeDeployLog(targetWfdVFlow.getCode(), targetWfdVFlow.getName(), targetWfdVFlow.getFlowVersion()), LocaleUtils.getFlowOperation());
    }

    @Override
    public void claim(String taskId) {
        SysUserDO currentUser = securityUtils.getCurrentUser();
        claim(taskId, currentUser.getId(), currentUser.getRealName());
        // ?????????????????????????????????????????? ????????????
        Task task = queryUtil.getTaskById(taskId);
        List<Task> tasks = queryUtil.listTasks(task.getProcessInstanceId());
        tasks.stream().forEach(taskT -> {
            if (!Objects.equals(taskId, taskT.getId()) && Objects.equals(taskT.getAssignee(), currentUser.getId())) {
                wfiUserTaskStateService.updateUserTaskState(currentUser.getId(), taskT.getProcessInstanceId(), taskT.getId(), UserTaskState.WORKING);
            }
        });
    }
//
//    /**
//     * ????????????
//     *
//     * @param node ??????
//     * @param task ??????
//     * @author JonnyJiang
//     * @date 2019/10/17 20:13
//     */
//
//    private void forceComplete(Node node, Task task) {
//        // ???????????????????????????????????????????????????????????????????????????
//        List<Task> assistTasks = queryUtils.getTaskQuery(task.getProcessInstanceId()).taskName(node.getId()).executionId(task.getExecutionId()).list();
//        for (Task assistTask : assistTasks) {
//
//        }
////        if (IntegerUtils.isFalse(node.getAllowPassAfterHostCompleted())) {
////            // ??????????????????????????????task???????????????
////            List<Task> assistTasks = queryUtils.getTaskQuery(task.getProcessInstanceId()).taskName(node.getId()).list();
////            for (Task assistTask : assistTasks) {
////                taskService.complete(assistTask.getId());
////                addComment(assistTask, LocaleUtils.getTaskForceComplete());
////            }
////        }
//    }

    @Override
    public void claim(String taskId, String userId, String realName) {
        String currentUserId = securityUtils.getCurrentUserId();
        Authentication.setAuthenticatedUserId(currentUserId);
        Task task = queryUtil.getTaskById(taskId);
        if (!currentUserId.equals(task.getAssignee())) {
            throw new NoAccessToClaimTaskException(taskId);
        }
        WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(task.getProcessInstanceId());
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        deliverUserClaim(task, currentUserId, flow);
        refreshTaskPending(task.getProcessInstanceId(), flow);
    }

    private void deliverUserClaim(TaskInfo task, String currentUserId, Flow flow) {
        String wfiDeliverIdFrom = queryUtil.getWfiDeliverIdFrom(task.getId());
        WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverById(wfiDeliverIdFrom);
        DeliverInfo deliverInfoFrom = wfiDeliver.getDeliverInfoClass();
        DeliverNode deliverNode = deliverInfoFrom.getDeliverNodeByNodeId(task.getName());
        if (OperationType.Reject.isEquals(deliverInfoFrom.getOperationType())) {
            // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            DeliverUser host = null;
            for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
                if (deliverUser.getClaimTime() != null) {
                    host = deliverUser;
                    break;
                }
            }
            if (host == null) {
                throw new BpmException(LocaleUtils.getOriginalHostNotFound());
            } else {
                if (!currentUserId.equals(host.getUserId())) {
                    throw new BpmException(LocaleUtils.getNotMatchOriginalHost(host.getRealName()));
                }
            }
        } else {
            Node node = flow.getNodeById(deliverNode.getNodeId());
            if (MultiInstanceMode.Single.isEquals(node.getMultiInstanceMode())) {
                if (deliverNode.getDeliverUsers().stream().anyMatch(o -> o.getClaimTime() != null)) {
                    throw new TaskAlreadyClaimedException(task.getId());
                }
            } else {
                DeliverUser currentDeliverUser = deliverNode.getDeliverUserByUserId(currentUserId);
                if (MultiInstanceMode.Department.isEquals(node.getMultiInstanceMode())) {
                    if (deliverNode.getDeliverUsers().stream().anyMatch(o -> StringUtils.equals(currentDeliverUser.getDepartmentId(), o.getDepartmentId()) && o.getClaimTime() != null)) {
                        throw new TaskAlreadyClaimedException(task.getId());
                    }
                } else if (MultiInstanceMode.Organization.isEquals(node.getMultiInstanceMode())) {
                    if (deliverNode.getDeliverUsers().stream().anyMatch(o -> StringUtils.equals(currentDeliverUser.getOrganizationId(), o.getOrganizationId()) && o.getClaimTime() != null)) {
                        throw new TaskAlreadyClaimedException(task.getId());
                    }
                } else if (MultiInstanceMode.Group.isEquals(node.getMultiInstanceMode())) {
                    if (deliverNode.getDeliverUsers().stream().anyMatch(o -> StringUtils.equals(currentDeliverUser.getGroupId(), o.getGroupId()) && o.getClaimTime() != null)) {
                        throw new TaskAlreadyClaimedException(task.getId());
                    }
                }
            }
        }
        for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
            if (StringUtils.equals(currentUserId, deliverUser.getUserId())) {
                deliverUser.setClaimTime(LocalDateTime.now());
            } else {
                deliverUser.setClaimTime(null);
            }
        }
        wfiDeliverService.updateDeliverInfo(wfiDeliverIdFrom, deliverInfoFrom);
    }

    @Override
    public void revokeClaim(String taskId) {
        String currentUserId = securityUtils.getCurrentUserId();
        Authentication.setAuthenticatedUserId(currentUserId);
        HistoricTaskInstance task = queryUtil.getHistoricTaskInstance(taskId);
        if (!currentUserId.equals(task.getAssignee())) {
            throw new BpmException(LocaleUtils.getNoAccessToRevokeClaimTask(taskId));
        }
        if (task.getEndTime() == null) {
            // ?????????????????????????????????????????????????????????
            WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverById(queryUtil.getWfiDeliverIdFrom(task.getId()));
            DeliverInfo deliverInfo = wfiDeliver.getDeliverInfoClass();
            DeliverNode deliverNode = deliverInfo.getDeliverNodeByNodeId(task.getName());
            List<DeliverUser> deliverUsers = deliverNode.getDeliverUsers().stream().filter(o -> currentUserId.equals(o.getUserId())).collect(Collectors.toList());
            if (deliverUsers.size() == 0) {
                throw new BpmException(LocaleUtils.getNoAccessToRevokeClaimTask(taskId));
            } else {
                if (deliverUsers.stream().anyMatch(o -> o.getClaimTime() != null)) {
                    for (DeliverUser deliverUser : deliverUsers) {
                        deliverUser.setClaimTime(null);
                    }
                    wfiDeliverService.updateDeliverInfo(wfiDeliver.getId(), deliverInfo);
                    WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(task.getProcessInstanceId());
                    refreshTaskPending(wfiFlow.getId(), FlowUtils.getFlow(wfiFlow.getModel()));
                    wfiUserTaskStateService.updateUserTaskState(currentUserId, task.getProcessInstanceId(), task.getId(), UserTaskState.UN_CLAIM);
                } else {
                    throw new BpmException(LocaleUtils.getTaskUnclaimed(taskId));
                }
            }
        } else {
            throw new BpmException(LocaleUtils.getRevokeClaimTaskEnded(taskId));
        }
    }

    @Override
    public void delegateWork(String taskId, String userId) {
        Authentication.setAuthenticatedUserId(securityUtils.getCurrentUserId());
        Task newTask = queryUtil.getTaskById(taskId);
        WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(newTask.getProcessInstanceId());
        List<WfdMessageVO> messageVOS = new ArrayList<>();
        List<WfdMessageEventVO> eventVOS = new ArrayList<>();
        delegateWork(wfiFlow.getFlowId(), newTask, userId, null, messageVOS, eventVOS);

        // ??????????????????????????????????????????
        updateFocusWorkReadFlagByProcessInsId(wfiFlow.getId());

        sendWfdMsg(messageVOS);
        sendWfdEventMsg(eventVOS);
    }

    /**
     * ?????????????????????????????? ??? ???????????????
     *
     * @param flowId  ??????id
     * @param newTask ?????????
     * @param userId  ???????????????????????? ??????????????????????????????????????????
     * @return
     * @author FourLeaves
     * @date 2020/2/24 17:49
     */
    private void delegateWork(String flowId, Task newTask, String userId, TaskInfo lastTask,
                              List<WfdMessageVO> messageVOS, List<WfdMessageEventVO> eventVOS) {
        // ????????????????????????
        String wfiDeliverId = (String) queryUtil.getTaskVariable(newTask.getId(), TaskVariableName.WFI_DELIVER_ID_FROM);
        WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverById(wfiDeliverId);
        DeliverInfo deliverInfo = wfiDeliver.getDeliverInfoClass();
        DeliverNode deliverNode = deliverInfo.getDeliverNodeByNodeId(newTask.getName());
        List<WfdDelegateWorkDO> wfdDelegateWorks = new ArrayList<>();
        Map<String, Object> data = new HashMap<>(16);

        WfiFlowDO wfiFlowDO = wfiFlowService.getById(newTask.getProcessInstanceId());
        WfdVFlowDO wfdVFlow = queryUtil.getWfdVFlowById(wfiFlowDO.getVFlowId());

        if (StringUtils.isNotBlank(userId)) {
            // ???????????????
            taskService.delegateTask(newTask.getId(), userId);

            DeliverUser deliverUser = deliverNode.getDeliverUsers().stream()
                    .filter(user -> Objects.equals(user.getUserId(), securityUtils.getCurrentUserId())).findFirst().get();
            DeliverUser newDeliverUser = new DeliverUser();
            newDeliverUser.setSortIndex(deliverUser.getSortIndex());
            newDeliverUser.setUserType(deliverUser.getUserType());
            newDeliverUser.setClaimTime(deliverUser.getClaimTime());
            newDeliverUser.setUserId(userId);
            String realName = iUser.getUserById(userId).getRealName();
            newDeliverUser.setRealName(realName);
            deliverNode.addDeliverUser(newDeliverUser);
            deliverInfo.setDeliverNodeByNodeId(deliverNode, newTask.getName());
            wfiDeliverService.updateDeliverInfo(wfiDeliverId, deliverInfo);
            String nodeName = deliverNode.getNodeName();
            if (StringUtils.isBlank(nodeName)) {
                Flow flow = FlowUtils.getFlow(wfiFlowDO.getModel());
                Node node = flow.getNodeById(newTask.getName());
                nodeName = node.getName();
            }
            wfdDelegateWorks.add(new WfdDelegateWorkDO(securityUtils.getAppName(), securityUtils.getCurrentUserId()
                    , userId, realName, newTask.getId(), newTask.getName(), nodeName,
                    newTask.getProcessInstanceId(), wfiFlowDO.getFlowNo(), wfdVFlow.getCategoryId(), wfdVFlow.getFlowId()));
        } else {
            // ????????????????????????
            String delegateUserId = wfdDelegateUserService.getEffectiveDelegateUserId(flowId, newTask.getAssignee());
            if (StringUtils.isNotBlank(delegateUserId)) {
                String finalUserId = delegateUserId;
//                if (deliverNode.getDeliverUsers().stream().anyMatch(o -> finalUserId.equals(o.getUserId()))) {
//                    // ????????????????????????????????????????????????????????????????????????????????????
//                    // TODO: 2020/4/26 ??????????????????
////                    iMessage.sendMessage()
//                } else {
                Flow flow = FlowUtils.getFlow(wfiFlowDO.getModel());
                for (int i = 0; i < deliverNode.getDeliverUsers().size(); i++) {
                    DeliverUser deliverUser = deliverNode.getDeliverUsers().get(i);
                    if (newTask.getAssignee().equals(deliverUser.getUserId())) {
                        SysUserDO user = iUser.getUserById(delegateUserId);
                        if (!deliverNode.getDeliverUsers().stream().anyMatch(o -> finalUserId.equals(o.getUserId()))) {
                            DeliverUser deliverUserNew = new DeliverUser();
                            deliverUserNew.setSortIndex(deliverUser.getSortIndex());
                            deliverUserNew.setClaimTime(deliverUser.getClaimTime());
                            deliverUserNew.setUserType(deliverUser.getUserType());
                            deliverUserNew.setUserId(delegateUserId);
                            deliverUserNew.setRealName(user.getRealName());
                            deliverNode.addDeliverUser(deliverUserNew);
                            deliverUser.getDelegateMap().put(newTask.getAssignee(), delegateUserId);
                            wfiDeliverService.updateDeliverInfo(wfiDeliverId, deliverInfo);
                        }
                        taskService.delegateTask(newTask.getId(), delegateUserId);
                        Node node = flow.getNodeById(newTask.getName());
                        wfdDelegateWorks.add(new WfdDelegateWorkDO(securityUtils.getAppName(), newTask.getAssignee()
                                , delegateUserId, user == null ? "" : user.getRealName(), newTask.getId(), newTask.getName(),
                                node.getName(), newTask.getProcessInstanceId()
                                , wfiFlowDO.getFlowNo(), wfdVFlow.getCategoryId(), wfdVFlow.getFlowId()));
                        break;
                    }
                }
            }
        }
        if (wfdDelegateWorks.size() > 0) {
            if (!wfdDelegateWorkService.saveBatch(wfdDelegateWorks)) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }
            List<String> userIds = wfdDelegateWorks.parallelStream().map(WfdDelegateWorkDO::getUserId).collect(Collectors.toList());
            List<SysUserDO> users = iUser.getUsersByIds(userIds);
            WfdDelegateWorkDO work = wfdDelegateWorks.get(0);
            WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(newTask.getProcessInstanceId());
            Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
            Node node = flow.getNodeById(newTask.getName());

            // ????????????????????????
            if (node.getAllowPassToAgent() == 0 && StringUtils.isNotBlank(userId)) {
                throw new BpmException(LocaleUtils.getNodeAllowDelegate());
            }

            SysUserDO user = users.stream().filter(u -> Objects.equals(work.getUserId(), u.getId())).findFirst().get();
            data.clear();
            data.put("delegateUserName", user.getRealName());
            data.put("delegateTaskName", node.getName());
            data.put("taskId", newTask.getId());
            List<String> delegateUserIds = wfdDelegateWorks.stream().map(WfdDelegateWorkDO::getDelegateUserId).collect(Collectors.toList());
            messageVOS.add(new WfdMessageVO(delegateUserIds,
                    Constants.BpmDelegateNoticeChannelName, Constants.BpmDelegateNoticeTemplate, data));
            // ?????????????????????????????????
            List<String> delegateUserIdsNew = AceCollectionUtils.copyList(delegateUserIds);
            delegateUserIdsNew.removeAll(userIds);
            delegateUserIdsNew.addAll(userIds);
            eventVOS.add(new WfdMessageEventVO(delegateUserIdsNew,
                    Constants.BpmDelegateNoticeEvent, null));
        } else {
            // ????????????
            // ?????????????????????
            String lastWfiDeliverId = (String) queryUtil.getTaskVariable(newTask.getId(), TaskVariableName.WFI_DELIVER_ID_FROM);
            WfiDeliverDO lastWfiDeliver = queryUtil.getWfiDeliverById(lastWfiDeliverId);
            DeliverInfo lastDeliverInfo = lastWfiDeliver.getDeliverInfoClass();
            NodeInfo nodeInfo = bpmAdapter.getNodeInfoByTaskId(lastTask.getId());
            if (!OperationType.Reject.isEquals(lastDeliverInfo.getOperationType())) {
                data.clear();
                WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(lastTask.getProcessInstanceId());
                Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
                Node node = flow.getNodeById(lastTask.getName());
                // ??????
                data.put("transferUserName", securityUtils.getCurrentUser().getRealName());
                data.put("transferTaskName", nodeInfo.getName());
                data.put("taskId", newTask.getId());
                data.putAll(getMessageVariableFields(flow, node.getId(), true));
                //userId = newTask.getAssignee();
                List<String> userIds = new ArrayList<>();
                //userIds.addAll(getReceiveUserIds(flow, node.getId(), wfiFlow.getId(), Arrays.asList(new String[]{userId})));
                userIds.add(newTask.getAssignee());
                messageVOS.add(new WfdMessageVO(userIds, getMessageChannelName(flow, node.getId(), Constants.BpmTransferNoticeChannelName),
                        getMessageTemplate(flow, node.getId(), Constants.BpmTransferNoticeTemplate, true), data));
                // ?????????????????????????????????
                List<String> userIdsCp = AceCollectionUtils.copyList(userIds);
//                org.apache.commons.collections.CollectionUtils.addAll(userIdsCp, new Object[userIds.size()]);
//                Collections.copy(userIdsCp, userIds);
                if (!userIdsCp.contains(securityUtils.getCurrentUserId())) {
                    userIdsCp.add(securityUtils.getCurrentUserId());
                }
                eventVOS.add(new WfdMessageEventVO(userIdsCp,
                        Constants.BpmTransferNoticeEvent, null));
            }


        }
    }


    @Override
    public String getBpmnXml(Flow flow) {
        LOGGER.debug(JSONObject.toJSONString(flow));
        BpmnModel model = BpmnUtil.generate(flow);
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] convertToXML = bpmnXMLConverter.convertToXML(model);
        String bpmn = null;
        try {
            bpmn = new String(convertToXML, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LOGGER.debug(bpmn);
        return bpmn;
    }

    private void onTaskRejecting(WfiFlowDO wfiFlow, HistoricTaskInstance task, Node node, DeliverInfo deliverInfo) {
        backupFlowInstance(wfiFlow.getId(), task.getId(), LocaleUtils.getTaskRejecting(node.getName()));
        for (NodeEvent event : node.getEvents()) {
            if (TaskEventType.Rejecting.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getTaskListenerInstance(TaskEventType.Rejecting, event.getEventClass());
                if (listener instanceof TaskRejectingListener) {
                    ((TaskRejectingListener) listener).notify(new TaskRejectingEventArgs(node, deliverInfo, wfiFlow));
                }
            }
        }
    }

    private void onTaskRejected(WfiFlowDO wfiFlow, Flow flow, Node node, DeliverInfo deliverInfo) {
        syncSettings(flow, wfiFlow);
        for (NodeEvent event : node.getEvents()) {
            if (TaskEventType.Rejected.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getTaskListenerInstance(TaskEventType.Rejected, event.getEventClass());
                if (listener instanceof TaskRejectedListener) {
                    ((TaskRejectedListener) listener).notify(new TaskRejectedEventArgs(node, deliverInfo, wfiFlow));
                }
            }
        }
    }

    private void checkRejectInfo(WfiFlowDO wfiFlow, RejectInfo rejectInfo, Node node, HistoricTaskInstance currentTask) {
        if (rejectInfo.getTaskRejectTos().size() == 0) {
            throw new TaskRejectToIsNotSpecifiedException();
        } else {
            if (IntegerUtils.isFalse(node.getAllowConfigReject())) {
                // ?????????????????????
                if (NodeRejectTo.First.isEquals(node.getRejectTo())) {
                    if (rejectInfo.getTaskRejectTos().size() != 1) {
                        // ???????????????????????????????????????????????????
                        throw new TaskRejectToQuantityNotMatchException(1, rejectInfo.getTaskRejectTos().size());
                    } else {
                        // ???????????????????????????????????????????????????????????????????????????
                        TaskRejectTo taskRejectTo = bpmAdapter.getTaskRejectToByRejectToFirst(wfiFlow, node.getFlow(), node);
                        TaskRejectTo targetRejectTo = rejectInfo.getTaskRejectTos().get(0);
                        if (!StringUtils.equals(taskRejectTo.getTaskId(), targetRejectTo.getTaskId())) {
                            throw new RejectToNodeIsNotFirstTaskException(targetRejectTo.getTaskId(), taskRejectTo.getTaskId());
                        }
                    }
                } else if (NodeRejectTo.Last.isEquals(node.getRejectTo())) {
                    // ???????????????????????????
                    List<TaskRejectTo> taskRejectToByRejectToLast = bpmAdapter.listTaskRejectToByRejectToLast(wfiFlow, node.getFlow(), node);
                    for (TaskRejectTo taskRejectTo : rejectInfo.getTaskRejectTos()) {
                        if (taskRejectToByRejectToLast.stream().noneMatch(o -> StringUtils.equals(o.getTaskId(), taskRejectTo.getTaskId()))) {
                            throw new NotAllowRejectToTaskException();
                        }
                    }
                } else if (NodeRejectTo.Specific.isEquals(node.getRejectTo())) {
                    Collection<List<TaskRejectTo>> lists = bpmAdapter.listTaskRejectToByRejectToSpecified(wfiFlow, node.getFlow(), node, currentTask.getId());
                    for (TaskRejectTo taskRejectTo : rejectInfo.getTaskRejectTos()) {
                        Boolean exist = false;
                        for (List<TaskRejectTo> taskRejectTos : lists) {
                            if (taskRejectTos.stream().anyMatch(o -> StringUtils.equals(o.getTaskId(), taskRejectTo.getTaskId()))) {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            throw new NotAllowRejectToTaskException();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void rejectWork(RejectInfo rejectInfo) {
        // ??????????????????
        List<TaskRejectTo> taskRejectTos = rejectInfo.getTaskRejectTos();
        List<TaskRejectTo> taskRejectTosNew = new ArrayList<>();
        Set<String> taskIds = new HashSet<>();
        for (TaskRejectTo taskRejectTo : taskRejectTos) {
            if (!taskIds.contains(taskRejectTo.getTaskId())) {
                taskIds.add(taskRejectTo.getTaskId());
                taskRejectTosNew.add(taskRejectTo);
            }
        }
        rejectInfo.setTaskRejectTos(taskRejectTosNew);
        HistoricTaskInstance currentTask = queryUtil.getHistoricTaskInstance(rejectInfo.getTaskId());
        if (securityUtils.getCurrentUserId().equals(currentTask.getAssignee())) {
            if (currentTask.getEndTime() == null) {
                WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(currentTask.getProcessInstanceId());
                Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
                Node node = flow.getNodeById(currentTask.getName());
                if (IntegerUtils.isFalse(node.getEnableReject())) {
                    throw new BpmException(LocaleUtils.getNodeNotEnableReject(node.getCode(), node.getName()));
                }
                checkRejectInfo(wfiFlow, rejectInfo, node, currentTask);
                Map<String, Object> variables = new HashMap<>();
                // ???????????????????????????
                // ????????????????????????
                BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());
                // ??????????????????
                UserTask currentUserTask = BpmnUtil.getUserTaskByNodeId(flow, bpmnModel, node);
                // ?????????????????????
                List<SequenceFlow> oriSequenceFlows = new ArrayList<>(currentUserTask.getOutgoingFlows());
                // ?????????????????????????????????
                currentUserTask.getOutgoingFlows().clear();
                DeliverInfo deliverInfo = new DeliverInfo();
                deliverInfo.setTaskId(currentTask.getId());
                deliverInfo.setOpinion(rejectInfo.getRejectReason());
                deliverInfo.setOperationType(OperationType.Reject.getValue());
                List<String> parentNodeIds = new ArrayList<>();
                queryUtil.listParentNodeIds(parentNodeIds, node);
                List<HistoricTaskInstance> parentTasks;
                if (parentNodeIds.size() > 0) {
                    parentTasks = queryUtil.getHistoricTaskInstanceQuery(currentTask.getProcessInstanceId()).taskNameIn(parentNodeIds).list();
                } else {
                    parentTasks = new ArrayList<>();
                }
                List<String> rejectToNodeNames = new ArrayList<>();
                // ??????????????????????????????????????????
                Map<String, WfiFlowDO> targetWfiFlows = new HashMap<>(16);
                Map<String, HistoricTaskInstance> targetTasks = new HashMap<>(16);
                List<String> processInstanceIds = new ArrayList<>();
                for (HistoricTaskInstance parentTask : parentTasks) {
                    targetTasks.put(parentTask.getId(), parentTask);
                    processInstanceIds.add(parentTask.getProcessInstanceId());
                }
                if (!CollectionUtils.isEmpty(processInstanceIds)) {
                    List<WfiFlowDO> wfiFlowDOS = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().in("id", processInstanceIds));
                    for (WfiFlowDO wfiFlowDO : wfiFlowDOS) {
                        targetWfiFlows.put(wfiFlow.getId(), wfiFlowDO);
                    }
                }

                for (TaskRejectTo taskRejectTo : rejectInfo.getTaskRejectTos()) {
//                    HistoricTaskInstance targetTask = null;
//                    for (HistoricTaskInstance parentTask : parentTasks) {
//                        if (parentTask.getId().equals(taskRejectTo.getTaskId())) {
//                            targetTask = parentTask;
//                            break;
//                        }
//                    }
                    HistoricTaskInstance targetTask = targetTasks.get(taskRejectTo.getTaskId());
                    if (targetTask == null) {
                        throw new BpmException(LocaleUtils.getTaskRejectToNotFound(taskRejectTo.getTaskId()));
                    }
//                    WfiFlowDO targetWfiFlow = bpmAdapter.getWfiFlowById(targetTask.getProcessInstanceId());
                    WfiFlowDO targetWfiFlow = targetWfiFlows.get(targetTask.getProcessInstanceId());
                    Flow targetFlow = FlowUtils.getFlow(targetWfiFlow.getModel());
                    Node targetNode = targetFlow.getNodeById(targetTask.getName());
                    DeliverNode deliverNode = queryUtil.getDeliverNodeFrom(targetNode, targetTask.getId());
                    deliverInfo.addDeliverNode(deliverNode);
                    // ??????????????????
                    UserTask targetUserTask = BpmnUtil.getUserTaskByNodeId(flow, bpmnModel, targetNode);
                    // ????????????????????????
                    List<SequenceFlow> newSequenceFlows = new ArrayList<>();
                    SequenceFlow newSequenceFlow = AbstractNode.addSequenceFlow(bpmnModel.getMainProcess(), currentUserTask, targetUserTask);
                    newSequenceFlows.add(newSequenceFlow);
                    currentUserTask.setOutgoingFlows(newSequenceFlows);
                    rejectToNodeNames.add(taskRejectTo.getNodeName());
                    List<String> userIds = FlowUtils.getFirstMultiInstanceUserIds(targetNode, deliverNode);
                    BpmnUtil.initUserTask(targetUserTask, targetNode, userIds, variables);
                }
                WfiDeliverDO wfiDeliver = getWfiDeliver(currentTask.getProcessInstanceId(), deliverInfo, securityUtils.getCurrentUserId());
                // ????????????
                SysUserDO currentUser = securityUtils.getCurrentUser();
                Authentication.setAuthenticatedUserId(currentUser.getId());
                taskService.setVariableLocal(rejectInfo.getTaskId(), TaskVariableName.WFI_DELIVER_ID.getName(), wfiDeliver.getId());
                HttpSession httpSession = securityUtils.getSession();
                httpSession.setAttribute(SessionAttribute.TASK_WFD_DELIVER, wfiDeliver);
                httpSession.setAttribute(SessionAttribute.TASK_DELIVER_INFO, deliverInfo);
                onTaskRejecting(wfiFlow, currentTask, node, deliverInfo);
                addComment(currentTask.getProcessInstanceId(), currentTask.getId(), UserType.Host, currentUser, deliverInfo.getOpinion());

                List<WfdMessageVO> messageVOS = new ArrayList<>();
                List<WfdMessageEventVO> eventVOS = new ArrayList<>();
                doDeliverWork(flow, bpmnModel, node, currentUser, currentTask, UserType.Host, variables, wfiDeliver, messageVOS, eventVOS, deliverInfo);
                // ??????????????????
                currentUserTask.setOutgoingFlows(oriSequenceFlows);
                stopTasksAfterReject(currentTask, node, bpmnModel);
                refreshTaskPending(wfiFlow.getId(), flow);
                onTaskRejected(wfiFlow, flow, node, deliverInfo);
                sysAuditLogService.saveLog(LocaleUtils.getFlowOperationReject(), LocaleUtils.getFlowOperationRejectLog(currentTask.getId(), rejectToNodeNames), LocaleUtils.getFlowOperation());


                // ????????????
                StringBuffer userIds = new StringBuffer();
                Map<String, String> userIdNodeName = new HashMap<>(16);
                deliverInfo.getDeliverNodes().forEach(deliverNode -> {
                    deliverNode.getDeliverUsers().forEach(user -> {
                        if (!userIdNodeName.containsKey(user.getUserId())) {
                            userIds.append("," + user.getUserId());
                            userIdNodeName.put(user.getUserId(), deliverNode.getNodeName());
                        }
                    });
                });
                for (String userId : userIdNodeName.keySet()) {
                    Map<String, Object> data = new HashMap<>(16);
                    data.put("taskName", userIdNodeName.get(userId));
                    data.put("rejectUserName", securityUtils.getCurrentUser().getRealName());
                    data.put("rejectComment", rejectInfo.getRejectReason());
                    data.put("flowCode", flow.getCode());
                    data.put("formId", wfiFlow.getBusinessKey());
                    messageVOS.add(new WfdMessageVO(Arrays.asList(new String[]{userId}),
                            Constants.BpmRejectNoticeChannelName, Constants.BpmRejectNoticeTemplate, data));
                }
                Set<String> userSet = new HashSet<>(userIdNodeName.keySet());
                System.out.println(securityUtils.getCurrentUserId());
                userSet.add(securityUtils.getCurrentUserId());
                eventVOS.add(new WfdMessageEventVO(new ArrayList<>(userSet), Constants.BpmRejectNoticeEvent, null));

                // ??????????????????????????????????????????
                updateFocusWorkReadFlagByProcessInsId(wfiFlow.getId());

                // ?????????
                sendWfdMsg(messageVOS);
                sendWfdEventMsg(eventVOS);
            } else {
                throw new BpmException(LocaleUtils.getRevokeClaimTaskEnded(rejectInfo.getTaskId()));
            }
        } else {
            throw new BpmException(LocaleUtils.getNoAccessToRejectTask(rejectInfo.getTaskId()));
        }
    }



    @Override
    public WfiFlowDO getWfiFlowById(String id) {
        WfiFlowDO wfiFlow = wfiFlowService.getById(id);
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByIdException(id);
        }
        return wfiFlow;
    }

//    @Override
//    public void addUrgeTaskDO(String nodeId,String flowId,String taskId) throws ParseException {
//        WfiFlowDO wfiFlow = getWfiFlowById(flowId);
//        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
//        Node node = flow.getNodeById(nodeId);
//        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(node.getParticipateRule());
//        System.out.println(node);
//        List list = (List) jsonObject.get("params");
//        System.out.println(list);
//        Map<String, Object> data = new HashMap<>();
//        UrgeTaskMessageVO urgeTaskMessageVO = new UrgeTaskMessageVO();
//        urgeTaskMessageVO.setOverTimeRemindTime(node.getOverTimeRemindTime()+5);
//        urgeTaskMessageVO.setIntervalTime(node.getOverTimeRemindIntv());
//        urgeTaskMessageVO.setNodeId(nodeId);
//        urgeTaskMessageVO.setFlowId(flowId);
//        urgeTaskMessageVO.setCurrentTime(0);
//        urgeTaskMessageVO.setId(UUID.randomUUID().toString());
//        urgeTaskMessageVO.setTaskId(taskId);
//        //?????????????????????????????????
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        DateFormat df2 = DateFormat.getDateTimeInstance();
//        Date date1 = formatter.parse(df2.format(date));
//        String dateString = formatter.format(date1);
//        Date dateTemp = formatter.parse(dateString);
//        urgeTaskMessageVO.setStartTime(dateTemp);
//        //???????????????????????????
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dateTemp);
//        cal.add(Calendar.HOUR,24);
//        Date date2 = null;
//        date2 = cal.getTime();
//        urgeTaskMessageVO.setEndTime(date2);
//        //????????????????????????????????????
//        List<String> ls = new ArrayList<>();
//        for(int i=0;i<list.size();i++) {
//            net.sf.json.JSONObject js1 = net.sf.json.JSONObject.fromObject(list.get(i));
//            ls.add(js1.get("id").toString());
//        }
//        String json = JSONArray.fromObject(ls).toString();
//        urgeTaskMessageVO.setUrgeUserIds(json);
//        urgeTaskMessageService.save(urgeTaskMessageVO);
//    }

    //????????????????????????????????????????????????????????????
    private Date addWorkTime(Date fromDate,Integer addDay){
//            Calendar cal = new GregorianCalendar();
            // cal now contains current date
//            System.out.println(cal.getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            // add the working days
            int workingDaysToAdd = 5;
            for (int i=0; i<workingDaysToAdd; i++)
                do {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                } while (! isWorkingDay(cal));
                return cal.getTime();
        }
    private static boolean isWorkingDay(Calendar cal) {
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)
                return false;
            return true;
        }

    public Date calDate(Integer urgentType,Node node){
        Integer urgentTime;
        //?????????????????????
        Date purposeDate = new Date();
        if(urgentType==1){
            //??????????????????
            if(Integer.parseInt(node.getTimeLimitUnitG())==0){
                urgentTime = node.getTimeLimitGeneral();
                purposeDate = addWorkTime(new Date(),urgentTime);
            }
            //??????????????????
            else if(Integer.parseInt(node.getTimeLimitUnitG())==1){
                urgentTime = node.getTimeLimitGeneral();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, urgentTime);
                purposeDate = cal.getTime();
            }
            //???????????????
            else{
                urgentTime = node.getTimeLimitGeneral();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.HOUR,urgentTime);
                purposeDate = cal.getTime();
            }
        }
        //?????????????????????
        else if(urgentType==2){
            //??????????????????
            if(Integer.parseInt(node.getTimeLimitUnitU())==0){
                urgentTime = node.getTimeLimitUrgent();
                purposeDate = addWorkTime(new Date(),urgentTime);
            }
            //??????????????????
            else if(Integer.parseInt(node.getTimeLimitUnitU())==1){
                urgentTime = node.getTimeLimitUrgent();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, urgentTime);
                purposeDate = cal.getTime();
            }
            //???????????????
            else{
                urgentTime = node.getTimeLimitUrgent();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.HOUR,urgentTime);
                purposeDate = cal.getTime();
            }
        }
        //?????????????????????
        else{
            //??????????????????
            if(Integer.parseInt(node.getTimeLimtUnitEu())==0){
                urgentTime = node.getTimeLimitExtraUrgent();
                purposeDate = addWorkTime(new Date(),urgentTime);
            }
            //??????????????????
            else if(Integer.parseInt(node.getTimeLimitUnitU())==1){
                urgentTime = node.getTimeLimitExtraUrgent();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, urgentTime);
                purposeDate = cal.getTime();
            }
            //???????????????
            else{
                urgentTime = node.getTimeLimitExtraUrgent();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.HOUR,urgentTime);
                purposeDate = cal.getTime();
            }
        }
        return purposeDate;
    }


    @Override
    public void addUrgeTaskDO(String flowId,String nodeId,String taskId) throws ParseException {
        WfiFlowDO wfiFlow = getWfiFlowById(flowId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(nodeId);
        UrgeTaskMessageVO urgeTaskMessageVO = new UrgeTaskMessageVO();
        net.sf.json.JSONObject model = net.sf.json.JSONObject.fromObject(wfiFlow.getModel());
        List<Object> objTemp1 = Arrays.asList(model.get("nodes"));
        List<Node> nodelist = (List<Node>)(List)objTemp1.get(0);
        System.out.println(nodelist.get(0));
        Node firstNode = nodelist.get(0);
        //??????????????????
        Integer urgentType = firstNode.getUrgentType();
//        Integer urgentTime;
        //??????????????????????????????????????????????????????????????????
        Date purposeDate = new Date();
        if(node.getExtendsFlowUrge()==1){
            purposeDate = calDate(urgentType,node);
        }
        //????????????????????????????????????????????????????????????????????????
        else{
            purposeDate = calDate(urgentType,node);
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df2 = DateFormat.getDateTimeInstance();
        Date date1 = formatter.parse(df2.format(date));
        String dateString = formatter.format(date1);
        Date dateTemp = formatter.parse(dateString);
        urgeTaskMessageVO.setStartTime(purposeDate);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(node.getParticipateRule());
        System.out.println(node);
        List list = (List) jsonObject.get("params");
        System.out.println(list);
        Map<String, Object> data = new HashMap<>();
        urgeTaskMessageVO.setOverTimeRemindTime(node.getOverTimeRemindTime()+5);
        urgeTaskMessageVO.setIntervalTime(node.getOverTimeRemindIntv());
        urgeTaskMessageVO.setNodeId(flowId);
        urgeTaskMessageVO.setCurrentTime(0);
        urgeTaskMessageVO.setId(UUID.randomUUID().toString());
        urgeTaskMessageVO.setTaskId(taskId);
        //?????????????????????????????????
        //???????????????????????????
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dateTemp);
//        cal.add(Calendar.HOUR,24);
//        Date date2 = cal.getTime();
//        date2 = cal.getTime();
//        urgeTaskMessageVO.setEndTime(purposeDate);
        //????????????????????????????????????
        List<String> ls = new ArrayList<>();
        for(int i=0;i<list.size();i++) {
            net.sf.json.JSONObject js1 = net.sf.json.JSONObject.fromObject(list.get(i));
            ls.add(js1.get("id").toString());
        }
        String json = JSONArray.fromObject(ls).toString();
        urgeTaskMessageVO.setUrgeUserIds(json);
        urgeTaskMessageService.save(urgeTaskMessageVO);
    }

    @Override
    public void urgeTask() {
        //????????????
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);//?????????????????????
        List<WfdMessageVO> wfdMessageVOS = new ArrayList<>();
        List<WfdMessageEventVO> eventVOS = new ArrayList<>();
        List<UrgeTaskMessageVO> urgeTaskMessageVOS = urgeTaskMessageService.list(new QueryWrapper<UrgeTaskMessageVO>().select("id","current_time","over_time_remind_time","start_time","end_time","urge_user_ids","interval_time","task_id"));
        for(UrgeTaskMessageVO urgeTaskMessageVO:urgeTaskMessageVOS){
//          Calendar date = Calendar.getInstance();
//          date.setTime(new Date());
//          Calendar end = Calendar.getInstance();
//          end.setTime(urgeTaskMessageVO.getEndTime());
            Date date = new Date();
            Date end = urgeTaskMessageVO.getEndTime();
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????
            Calendar c = Calendar.getInstance();
            c.setTime(urgeTaskMessageVO.getStartTime());
            c.add(Calendar.HOUR_OF_DAY, urgeTaskMessageVO.getCurrentTime()*urgeTaskMessageVO.getIntervalTime());
            Date newDate = c.getTime();
            //???????????????????????????????????????????????????????????????????????????????????????
            if(date.getTime()>end.getTime()){
                System.out.println("date.after(end)");
                urgeTaskMessageService.removeById(urgeTaskMessageVO);
            }
            if(isEffectiveDate(new Date(),newDate,urgeTaskMessageVO.getEndTime())){
                System.out.println("?????????????????????????????????");
                Map<String, Object> data = new HashMap<>();
                TaskInstance taskInstance = bpmAdapter.getTaskInstanceById(urgeTaskMessageVO.getTaskId());
                WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(taskInstance.getFlowInstanceId());
                Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
                Node node = flow.getNodeById(taskInstance.getNodeId());
                //????????????????????????????????????
                data.put("urgeUserName", "??????????????????????????????");
                data.put("urgeTaskName", node.getName());
                WfdMessageVO wfdMessageVO = new WfdMessageVO();
                wfdMessageVO.setData(data);
                wfdMessageVO.setChannelName("BpmUrgeNoticeChannelName");
//              wfdMessageVO.setUserId(urgeTaskMessageVO.getUrgeUserIds());
                wfdMessageVO.setUserId(urgeTaskMessageVO.getUrgeUserIds());
                System.out.println(urgeTaskMessageVO.getUrgeUserIds());
                JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(urgeTaskMessageVO.getUrgeUserIds());
                List output = (List) JSONSerializer.toJava(jsonArray);
                System.out.println(output);
                List<String> userIdString = new ArrayList<>(output);
                wfdMessageVO.setTemplateId("BpmUrgeNoticeTemplate");
                wfdMessageVO.setUserIds(output);
                wfdMessageVOS.add(wfdMessageVO);
                eventVOS.add(new WfdMessageEventVO(userIdString, Constants.BpmTransferNoticeEvent, data));
                urgeTaskMessageVO.setCurrentTime(urgeTaskMessageVO.getCurrentTime()+1);
                urgeTaskMessageService.updateById(urgeTaskMessageVO);
                System.out.println("=");
                sendWfdMsg(wfdMessageVOS);
                sendWfdEventMsg(eventVOS);
                //???????????????????????????????????????
                if(urgeTaskMessageVO.getCurrentTime()>=urgeTaskMessageVO.getOverTimeRemindTime()){
                    urgeTaskMessageService.removeById(urgeTaskMessageVO);
                }
            }
        }
    }

    @Override
    public void removeUrgeTask(String taskId){
        List<UrgeTaskMessageVO> urgeTaskMessageVOS = urgeTaskMessageService.list(new QueryWrapper<UrgeTaskMessageVO>().eq("task_id",taskId));
        urgeTaskMessageService.removeById(urgeTaskMessageVOS.get(0).getId());
    }

    @Override
    public boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateFlowNo(String flowCode, String businessKey, String flowNo) {
        WfiFlowDO wfiFlow = queryUtil.getWfiFlowByBusinessKey(flowCode, businessKey);
        wfiFlowService.updateFlowNo(wfiFlow.getFlowCode(), wfiFlow.getId(), flowNo);
    }

    @Override
    public void deleteInvalidFlowInstances() {
        List<ProcessInstance> processInstances = queryUtil.getProcessInstanceQuery().list();
        List<WfiFlowDO> wfiFlows = wfiFlowService.list(new QueryWrapper<WfiFlowDO>().in("ID", processInstances.stream().map(ProcessInstance::getId).collect(Collectors.toList())).eq("APP_ID", securityUtils.getAppName()));
        for (ProcessInstance processInstance : processInstances) {
            runtimeService.setVariable(processInstance.getId(), ProcessVariableName.Invalid.getName(), 1);
            List<WfiFlowDO> wfiFlowsCurrent = new ArrayList<>();
            for (int i = 0; i < wfiFlows.size(); ) {
                WfiFlowDO wfiFlow = wfiFlows.get(i);
                if (processInstance.getId().equals(wfiFlow.getId())) {
                    wfiFlows.remove(i);
                    wfiFlowsCurrent.add(wfiFlow);
                    Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
                    if (businessKeyNotExists(flow, wfiFlow.getBusinessKey())) {
                        // ???????????????????????????????????????????????????????????????
                        doDeleteFlowInstance(wfiFlow.getId(), LocaleUtils.getDeleteInvalid(processInstance.getId()), flow, processInstance.getBusinessKey());
                    }
                } else {
                    i++;
                }
            }
            if (wfiFlowsCurrent.size() == 0) {
                // ?????????????????????????????????????????????wfiFlow?????????
                doDeleteFlowInstance("", LocaleUtils.getDeleteInvalid(processInstance.getId()), null, processInstance.getBusinessKey());
            }
        }
        deleteInvalidWfiFlow();
    }

    private void backupFlowInstance(String flowInstanceId, String taskId, String description) {
        Integer cnt = wfiBackupService.count(new QueryWrapper<WfiBackupDO>().eq("FLOW_ID", flowInstanceId));
        WfiBackupDO wfiBackup = new WfiBackupDO();
        wfiBackup.setId(UUID.randomUUID().toString());
        wfiBackup.setFlowId(flowInstanceId);
        wfiBackup.setDescription(description);
        wfiBackup.setVersion(cnt + 1);
        wfiBackup.setBackupTime(LocalDateTime.now());
        wfiBackup.setTaskId(taskId);
        wfiBackup.setAppId(securityUtils.getAppName());
        wfiBackup.setEngineVersion(BpmManager.ENGINE_VERSION);
        Map<String, Object> data = queryUtil.getBackupData(flowInstanceId);
        String backupData = SerializeUtils.compressAfterSerialize(data);
        LOGGER.trace("flowId:" + flowInstanceId);
        LOGGER.trace("backupData:" + backupData);
        wfiBackup.setBackupData(backupData);
        Map<String, Object> map;
        if (ProcessEngineConfigurationImpl.DATABASE_TYPE_ORACLE.equals(BpmConfiguration.getActualDatabaseType())) {
            map = com.csicit.ace.bpm.activiti.impl.v7v1v81.oracle.History.resolveWfiBackup(wfiBackup);
        } else if (ProcessEngineConfigurationImpl.DATABASE_TYPE_MYSQL.equals(BpmConfiguration.getActualDatabaseType())) {
            map = com.csicit.ace.bpm.activiti.impl.v7v1v81.mysql.History.resolveWfiBackup(wfiBackup);
        } else if (DbUpdateTask.DATABASE_TYPE_ST.equals(BpmConfiguration.getActualDatabaseType())) {
            map = com.csicit.ace.bpm.activiti.impl.v7v1v81.st.History.resolveWfiBackup(wfiBackup);
        } else {
            map = com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveWfiBackup(wfiBackup);
        }
        wfiBackupService.insertWfiBackup(map);
    }

    private void checkAllowRecover(String flowInstanceId, HistoricTaskInstance currentTask, Node currentNode, SysUserDO currentUser, List<HistoricTaskInstance> processTasks) {
        UserType userType = bpmAdapter.getUserType(currentNode, currentTask.getId(), currentUser.getId());
        String wfiDeliverIdFrom = queryUtil.getWfiDeliverIdFrom(currentTask.getId());
        if (UserType.Host.equals(userType)) {
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            List<HistoricTaskInstance> tasks = processTasks.stream().filter(o -> ObjectUtils.notEqual(currentTask.getId(), o.getId()) && o.getEndTime() != null && o.getEndTime().compareTo(currentTask.getEndTime()) > 0).collect(Collectors.toList());
            if (tasks.size() > 0) {
                Map<String, Object> wfiDeliverIdFromMap = queryUtil.listTaskVariableValues(tasks, TaskVariableName.WFI_DELIVER_ID_FROM);
                for (HistoricTaskInstance task : tasks) {
                    if (StringUtils.equals(currentTask.getName(), task.getName())) {
                        // ?????????????????????????????????????????????????????????
                        if (!wfiDeliverIdFrom.equals(wfiDeliverIdFromMap.get(task.getId()))) {
                            throw new NotLatestFinishedTaskException(currentTask.getId(), task.getId());
                        }
                    } else {
                        // ????????????????????????????????????????????????
                        throw new NotLatestFinishedTaskException(currentTask.getId(), task.getId());
                    }
                }
            }
        } else if (UserType.Assistant.equals(userType)) {
            /**
             *  ???????????????????????????????????????????????????
             *  ???????????????????????????????????????????????????????????????????????????
             *  ????????????????????????????????????????????????
             **/
            DeliverInfo deliverInfoFrom = queryUtil.getDeliverInfoFrom(wfiDeliverIdFrom);
            DeliverNode deliverNode = deliverInfoFrom.getDeliverNodeByNodeId(currentNode.getId());
            // ?????????
            DeliverUser currentDeliverUser = deliverNode.getDeliverUserByUserId(currentUser.getId());
            Optional<DeliverUser> hostDeliverUserOpt;
            if (MultiInstanceMode.Department.isEquals(currentNode.getMultiInstanceMode())) {
                hostDeliverUserOpt = deliverNode.getDeliverUsers().stream().filter(o -> StringUtils.equals(o.getDepartmentId(), currentDeliverUser.getDepartmentId()) && o.getClaimTime() != null).findFirst();
            } else if (MultiInstanceMode.Organization.isEquals(currentNode.getMultiInstanceMode())) {
                hostDeliverUserOpt = deliverNode.getDeliverUsers().stream().filter(o -> StringUtils.equals(o.getOrganizationId(), currentDeliverUser.getOrganizationId()) && o.getClaimTime() != null).findFirst();
            } else if (MultiInstanceMode.Group.isEquals(currentNode.getMultiInstanceMode())) {
                hostDeliverUserOpt = deliverNode.getDeliverUsers().stream().filter(o -> StringUtils.equals(o.getGroupId(), currentDeliverUser.getGroupId()) && o.getClaimTime() != null).findFirst();
            } else {
                hostDeliverUserOpt = deliverNode.getDeliverUsers().stream().filter(o -> o.getClaimTime() != null).findFirst();
            }
            Boolean hostFinished = false;
            List<HistoricTaskInstance> tasks = processTasks.stream().filter(o -> ObjectUtils.notEqual(o.getId(), currentTask.getId()) && StringUtils.equals(o.getName(), currentTask.getName()) && o.getEndTime() != null).collect(Collectors.toList());
            if (hostDeliverUserOpt.isPresent()) {
                // ?????????????????????
                if (tasks.size() > 0) {
                    DeliverUser hostDeliverUser = hostDeliverUserOpt.get();
                    Map<String, Object> wfiDeliverIdFromMap = queryUtil.listTaskVariableValues(tasks, TaskVariableName.WFI_DELIVER_ID_FROM);
                    for (HistoricTaskInstance task : tasks) {
                        if (StringUtils.equals(task.getAssignee(), hostDeliverUser.getUserId()) && StringUtils.equals(wfiDeliverIdFrom, (String) wfiDeliverIdFromMap.get(task.getId()))) {
                            hostFinished = true;
                            break;
                        }
                    }
                }
            }
            if (hostFinished) {
                // ?????????????????????????????????????????????
                throw new HostFinishedException(flowInstanceId, currentTask.getId());
            } else {
                // ??????????????????????????????????????????????????????????????????????????????????????????
                for (HistoricTaskInstance task : tasks) {
                    if (ObjectUtils.notEqual(task.getId(), currentTask.getId()) && task.getEndTime().compareTo(currentTask.getEndTime()) >= 0) {
                        // ?????????????????????????????????
                        throw new NotLatestFinishedTaskException(currentTask.getId(), task.getId());
                    }
                }
            }
        } else if (UserType.None.equals(userType)) {
            // ??????????????????????????????????????????
            if (!queryUtil.hasAdminAuth(currentNode.getFlow().getId(), currentUser.getId())) {
                throw new NoAccessToWithdrawWorkException(currentNode.getFlow().getId(), currentUser.getId());
            }
        }
    }

    @Override
    public void recoverFlowInstance(String flowInstanceId, String taskId) {
        SysUserDO currentUser = securityUtils.getCurrentUser();
        HistoricTaskInstance taskInstance = queryUtil.getHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (taskInstance.getEndTime() == null) {
            throw new TaskNotEndedException(taskInstance.getId());
        }
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(flowInstanceId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(taskInstance.getName());

        // ??????????????????????????? WFI_FLOW
        List<WfiFocusedWorkDO> wfiFocusedWorkDOS = wfiFocusedWorkService.list(new QueryWrapper<WfiFocusedWorkDO>()
                .eq("flow_Instance_Id", wfiFlow.getId()));

        List<HistoricTaskInstance> processTasks = queryUtil.listHistoricTaskInstances(flowInstanceId);

        checkAllowRecover(flowInstanceId, taskInstance, node, currentUser, processTasks);

        // ?????????????????????????????????????????????

        String currentRealName = currentUser.getRealName();
        List<String> receiveUserIds = new ArrayList<>();
        List<WfdMessageVO> messageVOS = new ArrayList<>();
        List<WfdMessageEventVO> eventVOS = new ArrayList<>();
        for (HistoricTaskInstance outTask : processTasks) {
            if (outTask.getStartTime().compareTo(taskInstance.getEndTime()) >= 0) {
                // ?????????????????????????????????????????????
                String userId = outTask.getAssignee();
                Node nodeT = flow.getNodeById(outTask.getName());
                Map<String, Object> data = new HashMap<>();
                data.put("drawBackUserName", currentRealName);
                data.put("taskName", nodeT.getName());
                data.put("flowCode", flow.getCode());
                data.put("formId", wfiFlow.getBusinessKey());
                receiveUserIds.add(userId);
                messageVOS.add(new WfdMessageVO(Arrays.asList(userId), Constants.BpmDrawBackNoticeChannelName,
                        Constants.BpmDrawBackNoticeTemplate, data));
            }
        }
        Set<String> userIdSet = new HashSet<>(receiveUserIds);
        userIdSet.add(securityUtils.getCurrentUserId());
        eventVOS.add(new WfdMessageEventVO(new ArrayList<>(userIdSet), Constants.BpmDrawBackNoticeEvent, new HashMap<>()));

        WfiBackupDO wfiBackup = queryUtil.getWfiBackupByTaskId(flowInstanceId, taskId);
        LOGGER.trace("flowId:" + flowInstanceId);
        LOGGER.trace("backupData:" + wfiBackup.getBackupData());
        AbstractHistory history = AbstractHistory.getHistory(wfiBackup);
        onTaskWithdrawing(node, wfiFlow);
        history.recovery();
        refreshTaskPending(wfiFlow.getId(), flow);
        onTaskWithdrawn(node, wfiFlow);

        // ??????????????????????????????????????????
        if (!CollectionUtils.isEmpty(wfiFocusedWorkDOS)) {
            wfiFocusedWorkService.saveOrUpdateBatch(wfiFocusedWorkDOS);
            updateFocusWorkReadFlagByProcessInsId(wfiFlow.getId());
        }

        sendWfdMsg(messageVOS);
        sendWfdEventMsg(eventVOS);
    }

    private void onTaskWithdrawing(Node node, WfiFlowDO wfiFlow) {
        for (NodeEvent event : node.getEvents()) {
            if (TaskEventType.Withdrawing.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getTaskListenerInstance(TaskEventType.Withdrawing, event.getEventClass());
                if (listener instanceof TaskWithdrawingListener) {
                    ((TaskWithdrawingListener) listener).notify(new TaskWithdrawingEventArgs(node, wfiFlow));
                }
            }
        }
    }

    private void onTaskWithdrawn(Node node, WfiFlowDO wfiFlow) {
        syncSettings(node.getFlow(), wfiFlow);
        for (NodeEvent event : node.getEvents()) {
            if (TaskEventType.Withdrawn.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getTaskListenerInstance(TaskEventType.Withdrawn, event.getEventClass());
                if (listener instanceof TaskWithdrawnListener) {
                    ((TaskWithdrawnListener) listener).notify(new TaskWithdrawnEventArgs(node, wfiFlow));
                }
            }
        }
    }

    private void deleteInvalidWfiFlow() {
        List<WfiFlowDO> wfiFlows = wfiFlowService.listInvalidFromActiviti();
        for (WfiFlowDO wfiFlow : wfiFlows) {
            Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
            if (businessKeyNotExists(flow, wfiFlow.getBusinessKey())) {
                // ??????????????????????????????????????????wfiFlow????????????????????????????????????
                doDeleteFlowInstance("", LocaleUtils.getDeleteInvalid(wfiFlow.getId()), null, wfiFlow.getBusinessKey());
            }
        }
    }

    private Boolean businessKeyNotExists(Flow flow, String businessKey) {
        if (StringUtils.isNotEmpty(flow.getFormDataTable())) {
            if (wfiFlowService.businessExists(flow.getFormDataTable(), flow.getFormIdName(), businessKey)) {
                return false;
            } else {
                return true;
            }
        }
        // ???????????????????????????????????????????????????
        return false;
    }

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @param currentTask ??????
     * @param node        ??????
     * @author JonnyJiang
     * @date 2019/12/10 19:32
     */

    private void stopTasksAfterReject(HistoricTaskInstance currentTask, Node node, BpmnModel bpmnModel) {
        List<Node> childNodes = new ArrayList<>();
        for (Link link : node.getFlowInLinks()) {
            childNodes.addAll(queryUtil.listChildNodes(link.getFromNode()));
        }
        EndEvent endEvent = new EndEvent();
        endEvent.setId(AbstractNode.generateId());
        endEvent.setName(STOP_AFTER_REJECT_END_EVENT_NAME);
        bpmnModel.getMainProcess().addFlowElement(endEvent);
        for (Node childNode : childNodes) {
            stopTaskAfterReject(currentTask.getProcessInstanceId(), bpmnModel, childNode, endEvent);
        }
    }

    /**
     * ???????????????????????????
     *
     * @param flowInstanceId ????????????id
     * @param bpmnModel      ????????????
     * @param childNode      ??????
     * @param endEvent       ????????????????????????
     * @author JonnyJiang
     * @date 2019/12/11 8:36
     */

    private void stopTaskAfterReject(String flowInstanceId, BpmnModel bpmnModel, Node childNode, EndEvent endEvent) {
        // ??????????????????
        FlowNode currentFlowNode = getFlowNode(bpmnModel, childNode.getId());
        // ?????????????????????
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>(currentFlowNode.getOutgoingFlows());
        // ?????????????????????????????????
        currentFlowNode.getOutgoingFlows().clear();
        List<SequenceFlow> newSequenceFlows = new ArrayList<>();
        SequenceFlow newSequenceFlow = AbstractNode.addSequenceFlow(bpmnModel.getMainProcess(), currentFlowNode, endEvent);
        newSequenceFlows.add(newSequenceFlow);
        currentFlowNode.setOutgoingFlows(newSequenceFlows);
        // ??????????????????
        List<Task> tasks = queryUtil.getTaskQuery(flowInstanceId).taskName(childNode.getId()).taskTenantId(securityUtils.getAppName()).list();
        for (Task t : tasks) {
            DeliverInfo deliverInfo = new DeliverInfo();
            deliverInfo.setTaskId(t.getId());
            deliverInfo.setOperationType(OperationType.StopAfterReject.getValue());
            deliverInfo.setRejectTaskId(t.getId());
            DeliverNode deliverNode = new DeliverNode();
            deliverNode.setNodeId(endEvent.getName());
            deliverInfo.addDeliverNode(deliverNode);
            WfiDeliverDO wfiDeliver = getWfiDeliver(t.getProcessInstanceId(), deliverInfo, securityUtils.getCurrentUserId());
            taskService.setVariableLocal(t.getId(), TaskVariableName.WFI_DELIVER_ID.getName(), wfiDeliver.getId());
            taskService.complete(t.getId());
        }
        currentFlowNode.setOutgoingFlows(oriSequenceFlows);
    }

    private FlowNode getFlowNode(BpmnModel bpmnModel, String nodeId) {
        for (FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
            if (nodeId.equals(flowElement.getName())) {
                return (FlowNode) flowElement;
            }
        }
        throw new BpmException(LocaleUtils.getFlowElementNotFound(nodeId));
    }

    public List<String> getReceiveUserIds(Flow flow, String nodeId, String processInstanceId, List<String> defaultUserIDs) {
        Set<String> receiveUserIds = new HashSet<>(defaultUserIDs);
        List<String> scopes;
        if (StringUtils.isNotBlank(nodeId)) {
            // ??????????????????
            Node node = flow.getNodeById(nodeId);
            if (node != null) {
                scopes = node.getFlowInMsgSendTo();
                // ??????????????????
                if (scopes.contains("0")) {
                    List<HistoricTaskInstance> taskInstances = queryUtil.getHistoricTaskInstanceQuery(processInstanceId, nodeId).list();
                    for (HistoricTaskInstance taskInstance : taskInstances) {
                        receiveUserIds.add(taskInstance.getAssignee());
                        if (StringUtils.isNotBlank(taskInstance.getOwner())) {
                            receiveUserIds.add(taskInstance.getOwner());
                        }
                    }
                }
                // ?????????
                if (scopes.contains("1")) {
                    HistoricProcessInstance historicProcessInstance = queryUtil.getHistoricProcessInstanceQuery(processInstanceId).singleResult();
                    if (historicProcessInstance != null) {
                        receiveUserIds.add(historicProcessInstance.getStartUserId());
                    }
                }
                // ????????????
                if (scopes.contains("2")) {

                }
                // ??????????????????
                if (scopes.contains("3")) {
                    List<HistoricTaskInstance> taskInstances = queryUtil.getHistoricTaskInstanceQuery(processInstanceId).list();
                    for (HistoricTaskInstance taskInstance : taskInstances) {
                        receiveUserIds.add(taskInstance.getAssignee());
                        if (StringUtils.isNotBlank(taskInstance.getOwner())) {
                            receiveUserIds.add(taskInstance.getOwner());
                        }
                    }
                }
                // ??????????????????
                if (scopes.contains("4")) {
                    WfdVFlowDO wfdVFlowDO = wfdVFlowService.getEffectiveByFlowId(flow.getId(), LocalDateTime.now());
                    if (StringUtils.isNotBlank(wfdVFlowDO.getQueryAuthId())) {
                        receiveUserIds.addAll(iUser.getUserIdsByAuthId(wfdVFlowDO.getQueryAuthId()));
                    }
                    if (StringUtils.isNotBlank(wfdVFlowDO.getAdminAuthId())) {
                        receiveUserIds.addAll(iUser.getUserIdsByAuthId(wfdVFlowDO.getAdminAuthId()));
                    }
                }

            }
        } else {
            // ????????????
            scopes = flow.getMsgScopeFinished();
            // ?????????
            if (scopes.contains("0")) {
                HistoricProcessInstance historicProcessInstance = queryUtil.getHistoricProcessInstanceQuery(processInstanceId).singleResult();
                if (historicProcessInstance != null) {
                    receiveUserIds.add(historicProcessInstance.getStartUserId());
                }
            }
            // ??????????????????
            if (scopes.contains("1")) {
                List<HistoricTaskInstance> taskInstances = queryUtil.getHistoricTaskInstanceQuery(processInstanceId).list();
                for (HistoricTaskInstance taskInstance : taskInstances) {
                    receiveUserIds.add(taskInstance.getAssignee());
                    if (StringUtils.isNotBlank(taskInstance.getOwner())) {
                        receiveUserIds.add(taskInstance.getOwner());
                    }
                }
            }
            // ?????????????????????
            if (scopes.contains("2")) {
                WfdVFlowDO wfdVFlowDO = wfdVFlowService.getEffectiveByFlowId(flow.getId(), LocalDateTime.now());
                if (StringUtils.isNotBlank(wfdVFlowDO.getQueryAuthId())) {
                    receiveUserIds.addAll(iUser.getUserIdsByAuthId(wfdVFlowDO.getQueryAuthId()));
                }
                if (StringUtils.isNotBlank(wfdVFlowDO.getAdminAuthId())) {
                    receiveUserIds.addAll(iUser.getUserIdsByAuthId(wfdVFlowDO.getAdminAuthId()));
                }
            }
            // ????????????
            if (scopes.contains("3")) {

            }
        }
        return new ArrayList<>(receiveUserIds);
    }

    /**
     * ?????????
     *
     * @param messageVOS
     * @return
     * @author FourLeaves
     * @date 2020/4/27 18:40
     */
    public void sendWfdMsg(List<WfdMessageVO> messageVOS) {
//        Set<WfdMessageVO> wfdMessageVOSet = new HashSet<>();
//        for (WfdMessageVO message : messageVOS) {
//            List<String> userIds = message.getUserIds();
//            for (int i = 0; i < userIds.size(); i++) {
//                WfdMessageVO wfdMessageVO = new WfdMessageVO();
//                wfdMessageVO.setUserId(userIds.get(i));
//                wfdMessageVO.setChannelName(message.getChannelName());
//                wfdMessageVO.setTemplateId(message.getTemplateId());
//                wfdMessageVO.setData(message.getData());
//                wfdMessageVOSet.add(wfdMessageVO);
//            }
//        }
        // ?????? ChannelName templateId data ???????????????
//        List<WfdMessageVO> messageVOSCp = new ArrayList<>(messageVOS);
//        org.apache.commons.collections.CollectionUtils.addAll(messageVOSCp, new Object[messageVOS.size()]);
//        Collections.copy(messageVOSCp, messageVOS);
        Set<WfdMessageVO> messageVOSCpSet = new HashSet<>();
        for (WfdMessageVO message : messageVOS) {
            message.setUserIds(message.getUserIds());
            messageVOSCpSet.add(message);
        }

//        for (WfdMessageVO message : messageVOSCpSet) {
//            List<String> userIds = new ArrayList<>();
//            for (WfdMessageVO messageSin : wfdMessageVOSet) {
//                if (Objects.equals(message.getChannelName(), messageSin.getChannelName())
//                        && Objects.equals(message.getData(), messageSin.getData())
//                        && Objects.equals(message.getTemplateId(), messageSin.getTemplateId())) {
//                    userIds.add(messageSin.getUserId());
//                }
//            }
//            message.setUserIds(userIds);
//        }
        for (WfdMessageVO message : messageVOS) {
            iMessage.sendMessage(message.getUserIds(), message.getChannelName(), message.getTemplateId(), message.getData());
        }
    }

    /**
     * ???????????????
     *
     * @param eventVOS
     * @return
     * @author FourLeaves
     * @date 2020/4/27 18:40
     */
    public void sendWfdEventMsg(List<WfdMessageEventVO> eventVOS) {
        Set<WfdMessageEventVO> wfdeventVOSet = new HashSet<>();
        for (WfdMessageEventVO eventVO : eventVOS) {
            List<String> userIds = eventVO.getUserIds();
            for (int i = 0; i < userIds.size(); i++) {
                WfdMessageEventVO eventVONew = new WfdMessageEventVO();
                eventVONew.setUserId(userIds.get(i));
                eventVONew.setEventName(eventVO.getEventName());
                eventVONew.setData(eventVO.getData());
                wfdeventVOSet.add(eventVONew);
            }
        }

        Set<WfdMessageEventVO> eventVOSCpSet = new HashSet<>();
        for (WfdMessageEventVO eventVO : eventVOS) {
            eventVO.setUserIds(new ArrayList<>());
            eventVOSCpSet.add(eventVO);
        }

        for (WfdMessageEventVO eventVO : eventVOSCpSet) {
            List<String> userIds = new ArrayList<>();
            for (WfdMessageEventVO eventVOSin : wfdeventVOSet) {
                if (Objects.equals(eventVO.getEventName(), eventVOSin.getEventName())
                        && Objects.equals(eventVO.getData(), eventVOSin.getData())) {
                    userIds.add(eventVOSin.getUserId());
                }
            }
            eventVO.setUserIds(userIds);
        }
        for (WfdMessageEventVO eventVO : eventVOSCpSet) {
            iMessage.fireSocketEvent(eventVO.getUserIds(), eventVO.getEventName(), eventVO.getData());
        }
    }

    /**
     * ????????????????????????
     *
     * @param flow       ????????????
     * @param nodeId     ????????????
     * @param defaultTem ?????????????????????
     * @return
     * @author FourLeaves
     * @date 2021/9/6 14:44
     */
    public String getMessageChannelName(Flow flow, String nodeId, String defaultTem) {
        String channelName = null;
        if (flow != null) {
            if (StringUtils.isNotBlank(nodeId)) {
                Node node = flow.getNodeById(nodeId);
                if (node != null) {
                    channelName = node.getFlowInMsgChannel();
                }
            } else {
                channelName = flow.getMsgChannel();
            }
        }
        if (StringUtils.isNotBlank(channelName)) {
            return channelName;
        }
        return defaultTem;
    }

    /**
     * ????????????????????????
     *
     * @param flow       ????????????
     * @param nodeId     ????????????
     * @param defaultTem ?????????????????????
     * @param waitJob    ????????????
     * @return
     * @author FourLeaves
     * @date 2020/4/27 17:44
     */
    public String getMessageTemplate(Flow flow, String nodeId, String defaultTem, boolean waitJob) {
        String code = null;
        if (flow != null) {
            if (StringUtils.isNotBlank(nodeId)) {
                Node node = flow.getNodeById(nodeId);
                if (node != null) {
                    code = node.getFlowInMsgTemplateCode();
                    if (StringUtils.isBlank(code)) {
                        code = flow.getMsgTemplateCodeWaitJob();
                    }
                }
            } else {
                code = waitJob ? flow.getMsgTemplateCodeWaitJob() : flow.getMsgTemplateCodeOverJob();
            }
        }
        if (StringUtils.isNotBlank(code)) {
            SysMsgTemplateDO templateDO = iMessage.get(code);
            if (templateDO != null) {
                return templateDO.getTemplateId();
            }
        }
        return defaultTem;
    }

    /**
     * ???????????????????????????
     *
     * @param flow    ????????????
     * @param nodeId  ????????????
     * @param waitJob ????????????
     * @return
     * @author FourLeaves
     * @date 2020/4/27 17:44
     */
    public Map<String, Object> getMessageVariableFields(Flow flow, String nodeId, boolean waitJob) {
        Map<String, Object> data = new HashMap<>();
//        List<String> vatiantIds = new ArrayList<>();
//        if (flow != null) {
//            if (StringUtils.isNotBlank(nodeId)) {
//                Node node = flow.getNodeById(nodeId);
//                if (node != null) {
//                    vatiantIds.addAll(node.getVariableFieldInMsg());
//                }
//            } else if (waitJob) {
//                vatiantIds.addAll(flow.getVariableFieldWaitJob());
//            } else {
//                vatiantIds.addAll(flow.getVariableFieldOverJob());
//            }
//        }
//        if (!CollectionUtils.isEmpty(vatiantIds)) {
//            List<Variant> variants = new ArrayList<>();
//            List<Variant> variantsT = flow.getVariants();
//            for (Variant variant : variantsT) {
//                if (vatiantIds.contains(variant.getName())) {
//                    variants.add(variant);
//                }
//            }
//            for (Variant variant : variants) {
//                String value = expressionUtils.getValue(variant);
//                data.put(variant.getName(), value);
//            }
//
//        }
        return data;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @param processInsId
     * @return
     * @author FourLeaves
     * @date 2020/6/16 16:01
     */
    private boolean updateFocusWorkReadFlagByProcessInsId(String processInsId) {
        Map<String, String> params = new HashMap<>();
        params.put("processInsId", processInsId);
        params.put("isRead", "no");
        return updateFocusWorkReadTime(params);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param taskId
     * @return
     * @author FourLeaves
     * @date 2020/6/16 16:01
     */
    private boolean updateFocusWorkReadFlagByTaskId(String taskId) {
        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskId);
        params.put("isRead", "no");
        return updateFocusWorkReadTime(params);
    }


    @Override
    public boolean updateFocusWorkReadTime(Map<String, String> params) {
        String taskId = params.get("taskId");
        String processInsId = params.get("processInsId");
        String formId = params.get("formId");
        String flowCode = params.get("flowCode");
        // ??????????????? ?????? ??????
        String isRead = params.get("isRead");
        if (StringUtils.isBlank(processInsId)) {
            if (StringUtils.isNotBlank(taskId)) {
                HistoricTaskInstance taskInstance = queryUtil.getHistoricTaskInstance(taskId);
                if (taskInstance == null) {
                    return false;
                }
                processInsId = taskInstance.getProcessInstanceId();
            } else if (StringUtils.isNotBlank(flowCode) && StringUtils.isNotBlank(formId)) {
                WfiFlowDO wfiFlowDO = wfiFlowService.getByCode(flowCode, formId);
                if (wfiFlowDO == null) {
                    return false;
                }
                processInsId = wfiFlowDO.getId();
            }
        }
        if (Objects.equals("no", isRead)) {
            return wfiFocusedWorkService.update(new WfiFocusedWorkDO(), new UpdateWrapper<WfiFocusedWorkDO>()
                    .set("latest_read_flag", 1).eq("FLOW_INSTANCE_ID", processInsId).eq("app_id", securityUtils.getAppName()));
        }
        return wfiFocusedWorkService.update(new WfiFocusedWorkDO(), new UpdateWrapper<WfiFocusedWorkDO>()
                .set("latest_read_time", LocalDateTime.now()).set("latest_read_flag", 0).eq("user_Id", securityUtils.getCurrentUserId())
                .eq("FLOW_INSTANCE_ID", processInsId).eq("app_id", securityUtils.getAppName()));
    }

    @Override
    public boolean focusWork(Map<String, Object> params) {
        String taskId = (String) params.get("taskId");
        String formId = (String) params.get("formId");
        String flowCode = (String) params.get("flowCode");
        String nodeId = (String) params.get("nodeId");
        String flowInstanceId = (String) params.get("flowInstanceId");
        String isFocus = (String) params.get("isFocus");
        if (Objects.equals(isFocus, "yes")) {
            if (StringUtils.isNotBlank(taskId)) {
                HistoricTaskInstance taskInstance = queryUtil.getHistoricTaskInstance(taskId);
                if (taskInstance != null) {
                    flowInstanceId = taskInstance.getProcessInstanceId();
                }
            }
            if (StringUtils.isBlank(flowInstanceId)) {
                WfiFlowDO wfiFlowDO = bpmAdapter.getWfiFlowByBusinessKey(flowCode, formId);
                if (wfiFlowDO != null) {
                    flowInstanceId = wfiFlowDO.getId();
                } else {
                    return false;
                }
            }
            if (StringUtils.isBlank(taskId)) {
                List<HistoricTaskInstance> taskInstances = queryUtil.listHistoricTaskInstancesByNodeId(flowInstanceId, nodeId);
                if (CollectionUtils.isEmpty(taskInstances)) {
                    return false;
                }
                try {
                    HistoricTaskInstance taskInstance = taskInstances.stream().filter(task -> Objects.equals(task.getAssignee(), securityUtils.getCurrentUserId())).findFirst().get();
                    taskId = taskInstance.getId();
                } catch (Exception e) {
                    return false;
                }
            }
            if (StringUtils.isBlank(taskId)) {
                return false;
            }
            WfiFocusedWorkDO wfiFocusedWorkDO = new WfiFocusedWorkDO();
            wfiFocusedWorkDO.setId(taskId);
            wfiFocusedWorkDO.setAppId(securityUtils.getAppName());
            wfiFocusedWorkDO.setCreateTime(LocalDateTime.now());
            wfiFocusedWorkDO.setFlowInstanceId(flowInstanceId);
            wfiFocusedWorkDO.setUserId(securityUtils.getCurrentUserId());

            HistoricProcessInstance processInstance = queryUtil.getHistoricProcessInstance(flowInstanceId);
            wfiFocusedWorkDO.setStarterId(processInstance.getStartUserId());
            wfiFocusedWorkDO.setStartTime(processInstance.getStartTime());

            WfiFlowDO wfiFlowDO = wfiFlowService.getById(flowInstanceId);
            Flow flow = FlowUtils.getFlow(wfiFlowDO.getModel());
            wfiFocusedWorkDO.setWfdCategoryId(flow.getCategoryId());
            wfiFocusedWorkDO.setWorkNo(wfiFlowDO.getFlowNo());
            wfiFocusedWorkDO.setWfdId(flow.getId());

            return wfiFocusedWorkService.save(wfiFocusedWorkDO);
        } else {
            List<String> taskIdList = JsonUtils.castObject(params.get("taskIdList"), List.class);
            if (!CollectionUtils.isEmpty(taskIdList)) {
                return wfiFocusedWorkService.removeByIds(taskIdList);
            }
            if (StringUtils.isNotBlank(taskId)) {
                return wfiFocusedWorkService.removeById(taskId);
            }
        }
        return true;
    }

    @Override
    public void invite(String taskId, List<String> userIds) {
        queryUtil.getSpringProcessEngineConfiguration().getCommandExecutor().execute(new InviteCmd(securityUtils.getSession(), bpmAdapter, securityUtils, taskService, wfiDeliverService, queryUtil, iUser, taskId, userIds));
    }

    @Override
    public void refreshTaskPending(String flowInstanceId, Flow flow) {
        // ??????????????????
        List<String> flowIds = new ArrayList<>();
        flowIds.add(flowInstanceId);
        if (flowIds.size() > 0) {
            wfiTaskPendingService.clear(flowIds);
        }
        Collection<WfiTaskPendingDO> wfiTaskPendings = new ArrayDeque<>();
        resolveTaskPending(wfiTaskPendings, flowInstanceId, flow);
        if (wfiTaskPendings.size() > 0) {
            wfiTaskPendingService.saveBatch(wfiTaskPendings);
        }
    }

    @Override
    public void resolveTaskPending(Collection<WfiTaskPendingDO> wfiTaskPendings, String flowInstanceId, Flow flow) {
        taskPendingUtil.resolveTaskPending(wfiTaskPendings, flowInstanceId, flow);
    }

    private void deleteFlowInstanceByTaskPending(WfiFlowDO wfiFlow, Flow flow, WfiTaskPendingDO wfiTaskPending, String businessKey, String deleteReason) {
        LOGGER.debug("------------task id: " + wfiTaskPending.getTaskId() + "------------");
        Node node = flow.getNodeById(wfiTaskPending.getNodeId());
        LOGGER.debug("nodeName: " + node.getName());
        LOGGER.debug("nodeType: " + node.getNodeType());
        LOGGER.debug("allowHostDelete: " + node.getAllowHostDelete());
        UserType userType = UserType.getByValue(wfiTaskPending.getUserType());
        LOGGER.debug("userType: " + userType);
        if (UserType.Host.equals(userType)) {
            if (IntegerUtils.isTrue(node.getAllowHostDelete())) {
                deleteFlowInstance(flow, wfiFlow, businessKey, deleteReason);
            } else {
                throw new NotAllowHostDeleteException(wfiTaskPending.getTaskId());
            }
        } else if (UserType.Assistant.equals(userType)) {
            if (IntegerUtils.isTrue(node.getAllowAssitDelete())) {
                deleteFlowInstance(flow, wfiFlow, businessKey, deleteReason);
            } else {
                throw new NotAllowAssitDeleteException(wfiTaskPending.getTaskId());
            }
        } else {
            throw new UnsupportedTaskPendingUserTypeException(userType);
        }
    }

    @Override
    public void deleteFlowInstanceByTaskPending(WfiFlowDO wfiFlow, Flow flow, WfiTaskPendingDO wfiTaskPending, Boolean hasAdminAuth, String currentUserId, String deleteReason) {
        if (ObjectUtils.notEqual(wfiTaskPending.getUserId(), currentUserId)) {
            // ???????????????????????????????????????????????????
            if (!hasAdminAuth) {
                throw new NoAdminAuthException(flow);
            }
        }
        deleteFlowInstanceByTaskPending(wfiFlow, flow, wfiTaskPending, wfiFlow.getBusinessKey(), deleteReason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createReviewFile(HashMap<String,String> params) {
        //?????????????????????
        SysReviewFile reviewFile = new SysReviewFile();
        reviewFile.setId(params.get("Id"));
        reviewFile.setFlowCode(params.get("flowCode"));
        reviewFile.setApplyTime(LocalDateTime.now());
        reviewFile.setApplyTime(LocalDateTime.now());
        reviewFile.setRemark(params.get("remark"));
        reviewFile.setApplyUserName(securityUtils.getCurrentUserName());
        reviewFile.setApplyUserId(securityUtils.getCurrentUserId());
        reviewFile.setManageUserId(securityUtils.getCurrentUserId());
        reviewFileService.save(reviewFile);
        // ??????????????????????????????
        createFlowInstanceByCode(reviewFile.getFlowCode(), reviewFile.getId());
        bpmAdapter.addReviewFile(reviewFile.getId());
    }
}