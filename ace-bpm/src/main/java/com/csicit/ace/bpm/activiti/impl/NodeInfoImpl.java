package com.csicit.ace.bpm.activiti.impl;

import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.activiti.TaskVariableName;
import com.csicit.ace.bpm.activiti.WfiDeliverTasks;
import com.csicit.ace.bpm.activiti.utils.QueryUtil;
import com.csicit.ace.bpm.enums.HostMode;
import com.csicit.ace.bpm.enums.OperationType;
import com.csicit.ace.bpm.enums.UserTaskState;
import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.exception.NoAccessToCreateFlowException;
import com.csicit.ace.bpm.pojo.domain.WfiDeliverDO;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.DeliverNode;
import com.csicit.ace.bpm.pojo.vo.NodeInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.pojo.vo.wfd.TaskState;
import com.csicit.ace.bpm.service.WfiUserTaskStateService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.activiti.engine.history.HistoricTaskInstance;

import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2019/11/4 14:52
 */
public class NodeInfoImpl extends NodeInfo {


    private static final SecurityUtils securityUtils = SpringContextUtils.getBean(SecurityUtils.class);
    private static final BpmAdapter bpmAdapter = SpringContextUtils.getBean(BpmAdapter.class);
    private static final WfiUserTaskStateService wfiUserTaskStateService = SpringContextUtils.getBean(WfiUserTaskStateService.class);

    public NodeInfoImpl(Node node) {
        super(node);
    }

    public NodeInfoImpl(Node node, WfiFlowDO wfiFlow, TaskInstance task) {
        super(node, wfiFlow, task, true);
    }

    /**
     * 针对无权限人员
     *
     * @return
     * @author FourLeaves
     * @date 2021/5/6 14:23
     */
    public NodeInfoImpl(Node node, WfiFlowDO wfiFlow, TaskInstance task, boolean withAuth) {
        super(node, wfiFlow, task, withAuth);
    }

    @Override
    protected void init() {
        setAllowRevokeClaim(IntegerUtils.FALSE_VALUE);
        setAllowClaim(IntegerUtils.FALSE_VALUE);
        setAllowReject(IntegerUtils.FALSE_VALUE);
        // 计算任务状态
        if (task == null) {
            // 判断是否有发起工作的权限
            if (!bpmAdapter.hasInitAuth(node.getFlow(), securityUtils.getCurrentUser())) {
                throw new NoAccessToCreateFlowException(node.getFlow());
            }
            setState(TaskState.Executing.getValue());
            setUserType(UserType.Host.getValue());
            setAllowSelectWorkResult(IntegerUtils.TRUE_VALUE);
            addAvailableOperations(node.getOperationsHostBd());
            setAllowPresetRoute(IntegerUtils.FALSE_VALUE);
            setMustPresetRoute(IntegerUtils.FALSE_VALUE);
        } else {
            // 设置是否允许选择工作结果的缺省值为false，如果在后续计算中可选择，则再设置为true
            setAllowSelectWorkResult(IntegerUtils.FALSE_VALUE);
            if (isWithAuth()) {
                initWithTask();
            } else {
                initWithTaskWithoutAuth();
            }
        }
    }

    private void initWithTaskWithoutAuth() {
        QueryUtil queryUtil = SpringContextUtils.getBean(QueryUtil.class);
        String wfiDeliverIdFrom = (String) bpmAdapter.getTaskVariable(task.getId(), TaskVariableName.WFI_DELIVER_ID_FROM);
        WfiDeliverDO wfiDeliverFrom = queryUtil.getWfiDeliverById(wfiDeliverIdFrom);
        deliverInfoFrom = wfiDeliverFrom.getDeliverInfoClass();
        setUserType(UserType.Host.getValue());
        setState(TaskState.Completed.getValue());
        setAllowPresetRoute(IntegerUtils.FALSE_VALUE);
        setOperationType(OperationType.Deliver.getValue());
        setMustPresetRoute(IntegerUtils.FALSE_VALUE);
    }

    private void initWithTask() {
        String currentUserId = securityUtils.getCurrentUserId();
        QueryUtil queryUtil = SpringContextUtils.getBean(QueryUtil.class);
        String wfiDeliverIdFrom = (String) bpmAdapter.getTaskVariable(task.getId(), TaskVariableName.WFI_DELIVER_ID_FROM);
        WfiDeliverDO wfiDeliverFrom = queryUtil.getWfiDeliverById(wfiDeliverIdFrom);
        deliverInfoFrom = wfiDeliverFrom.getDeliverInfoClass();
        DeliverNode deliverNode = deliverInfoFrom.getDeliverNodeByNodeId(task.getNodeId());
        // 判断当前用户是否为经办人
        UserType userType = bpmAdapter.getUserType(task.getFlowInstanceId(), task.getId(), task.getEndTime() != null, currentUserId, node, deliverNode);
        setUserType(userType.getValue());
        if (task.getEndTime() == null) {
            setState(TaskState.Executing.getValue());
            if (HostMode.FirstClaim.isEquals(node.getHostMode())) {
                if (UserType.Host.isEquals(getUserType())) {
                    // 如果是主办人，则允许取消接收
                    if (deliverNode.getDeliverUsers().size() > 1) {
                        // 如果经办人超过一个，则允许取消接收
                        setAllowRevokeClaim(IntegerUtils.TRUE_VALUE);
                    }
                    if (IntegerUtils.isTrue(node.getEnableReject())) {
                        setAllowReject(IntegerUtils.TRUE_VALUE);
                    }
                } else if (UserType.Assistant.isEquals(getUserType())) {
                    // 如果不是主办人，则判断是否存在经办人已接收
                    if (FlowUtils.allowClaim(node, deliverNode, currentUserId)) {
                        setAllowClaim(IntegerUtils.TRUE_VALUE);
                    } else {
                        setAllowClaim(IntegerUtils.FALSE_VALUE);
                    }
                }
            } else if (HostMode.Everybody.isEquals(node.getHostMode())) {
                if (UserType.Host.isEquals(getUserType()) || UserType.Assistant.isEquals(getUserType())) {
                    if (IntegerUtils.isTrue(node.getEnableReject())) {
                        setAllowReject(IntegerUtils.TRUE_VALUE);
                    }
                }
            } else {
                if (UserType.Host.isEquals(getUserType())) {
                    if (IntegerUtils.isTrue(node.getEnableReject())) {
                        setAllowReject(IntegerUtils.TRUE_VALUE);
                    }
                }
            }
            // 判断是否可以选择工作结果
            if (node.getWorkResultOptions().length > 0) {
                if (UserType.Host.isEquals(getUserType())) {
                    // 主办人在任务没办结前，可以选择办理结果
                    setAllowSelectWorkResult(IntegerUtils.TRUE_VALUE);
                } else if (UserType.Assistant.isEquals(getUserType())) {
                    if (IntegerUtils.isTrue(node.getEnableVote())) {
                        // 如果启用会签，则协办人可以选择办理结果
                        setAllowSelectWorkResult(IntegerUtils.TRUE_VALUE);
                    }
                }
            }
            initAvailableOperations(getUserType());
            if (IntegerUtils.isTrue(getAllowClaim())) {
                wfiUserTaskStateService.updateUserTaskState(currentUserId, task.getFlowInstanceId(), task.getId(), UserTaskState.UN_CLAIM);
            } else {
                wfiUserTaskStateService.updateUserTaskState(currentUserId, task.getFlowInstanceId(), task.getId(), UserTaskState.WORKING);
            }
            if (UserType.Host.isEquals(getUserType())) {
                setAllowPresetRoute(node.getAllowPresetRoute());
                setMustPresetRoute(node.getMustPresetRoute());
            } else {
                setAllowPresetRoute(IntegerUtils.FALSE_VALUE);
                setMustPresetRoute(IntegerUtils.FALSE_VALUE);
            }
        } else {
            setState(TaskState.Completed.getValue());
            initAvailableOperations(getUserType());
            setAllowPresetRoute(IntegerUtils.FALSE_VALUE);
            setMustPresetRoute(IntegerUtils.FALSE_VALUE);
        }
        if(IntegerUtils.isTrue(getAllowPresetRoute()))
        {
            if(!bpmAdapter.allowPresetRoute(wfiFlow, node, getTaskId()))
            {
                setAllowPresetRoute(IntegerUtils.FALSE_VALUE);
                setMustPresetRoute(IntegerUtils.FALSE_VALUE);
            }
        }
        WfiDeliverTasks wfiDeliverTasks = new WfiDeliverTasks(wfiDeliverIdFrom);
        List<HistoricTaskInstance> nodeTasks = queryUtil.getHistoricTaskInstanceQuery(task.getFlowInstanceId(), node.getId()).list();
        Map<String, Object> wfiDeliverIdFromMap = queryUtil.listTaskVariableValues(nodeTasks, TaskVariableName.WFI_DELIVER_ID_FROM);
        for (HistoricTaskInstance nodeTask : nodeTasks) {
            if (wfiDeliverIdFrom.equals(wfiDeliverIdFromMap.get(nodeTask.getId()))) {
                wfiDeliverTasks.add(nodeTask);
            }
        }
        setOperationType(bpmAdapter.getOperationType(task.getEndTime() != null, userType, node, task.getId(), task.getOwner(), task.getAssignee(), wfiDeliverTasks).getValue());
        initAllowInvite();
    }

    private void initAllowInvite() {
        QueryUtil queryUtil = SpringContextUtils.getBean(QueryUtil.class);
        String wfiDeliverIdFrom = queryUtil.getWfiDeliverIdFrom(task.getId());
        if (IntegerUtils.isTrue(node.getAllowHostInvite())) {
            if (UserType.Host.isEquals(getUserType())) {
                if (task.getEndTime() != null) {
                    // 当前任务未办结，则可以加签
                    setAllowInvite(IntegerUtils.TRUE_VALUE);
                } else {
                    // 当前任务已办结，需要判断与当前任务一同创建的任务是否都已办结
                    if (queryUtil.nodeIsFinished(task.getFlowInstanceId(), wfiDeliverIdFrom, node.getId())) {
                        setAllowInvite(IntegerUtils.FALSE_VALUE);
                    } else {
                        setAllowInvite(IntegerUtils.TRUE_VALUE);
                    }
                }
            }
        } else if (IntegerUtils.isTrue(node.getAllowAssistInvite())) {
            if (UserType.Assistant.isEquals(getUserType())) {
                // 当前是协办人，需要判断与当前任务一同创建的任务是否都已办结
                if (queryUtil.nodeIsFinished(task.getFlowInstanceId(), wfiDeliverIdFrom, node.getId())) {
                    setAllowInvite(IntegerUtils.FALSE_VALUE);
                } else {
                    setAllowInvite(IntegerUtils.TRUE_VALUE);
                }
            }
        }
    }

    private void initAvailableOperations(Integer userType) {
        if (task.getEndTime() == null) {
            if (UserType.Host.isEquals(userType)) {
                addAvailableOperations(node.getOperationsHostBd());
            } else if (UserType.Assistant.isEquals(userType)) {
                addAvailableOperations(node.getOperationsAssistBd());
            }
        } else {
            if (UserType.Host.isEquals(userType)) {
                addAvailableOperations(node.getOperationsHostAd());
            } else if (UserType.Assistant.isEquals(userType)) {
                addAvailableOperations(node.getOperationsAssistAd());
            }
        }
    }

    private void initOperationType() {
        // 如果是无关人员，则判断是否有查询权限
        if (UserType.None.isEquals(getUserType())) {
            if (!bpmAdapter.hasQueryAuth(node.getFlow(), securityUtils.getCurrentUser())) {
                throw new BpmException(LocaleUtils.getNoAccessToQueryTask(task.getId()));
            }
            // 如果是无关人员，则不能进行任何操作
            setOperationType(OperationType.None.getValue());
        } else {
            if (task.getEndTime() == null) {
                if (UserType.Host.isEquals(getUserType())) {
                    setOperationType(OperationType.Deliver.getValue());
                } else if (UserType.Assistant.isEquals(getUserType())) {
                    setOperationType(OperationType.Complete.getValue());
                } else {
                    setOperationType(OperationType.None.getValue());
                }
            } else {
                // 如果工作已办结
                setOperationType(OperationType.None.getValue());
            }
        }
    }
}
