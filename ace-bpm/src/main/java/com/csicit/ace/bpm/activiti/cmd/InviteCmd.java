package com.csicit.ace.bpm.activiti.cmd;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.SessionAttribute;
import com.csicit.ace.bpm.activiti.nodes.ManualNode;
import com.csicit.ace.bpm.activiti.utils.BpmnUtil;
import com.csicit.ace.bpm.activiti.utils.QueryUtil;
import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.exception.*;
import com.csicit.ace.bpm.pojo.domain.WfiDeliverDO;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.DeliverNode;
import com.csicit.ace.bpm.pojo.vo.DeliverUser;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.service.WfiDeliverService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.interfaces.service.IUser;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.csicit.ace.bpm.activiti.nodes.ManualNode.NUMBER_OF_ACTIVE_INSTANCES;
import static com.csicit.ace.bpm.activiti.nodes.ManualNode.NUMBER_OF_INSTANCES;

/**
 * 加签命令
 *
 * @author JonnyJiang
 * @date 2020/6/3 8:26
 */
public class InviteCmd implements Command<List<Task>> {
    /**
     * 来自以下文件
     * activiti-engine\src\main\java\org\activiti\engine\impl\bpmn\behavior\ParallelMultiInstanceBehavior.java
     */
    private String collectionElementIndexVariable = "loopCounter";
    private BpmAdapter bpmAdapter;
    private TaskService taskService;
    private QueryUtil queryUtil;
    private IUser iUser;
    private String taskId;
    private List<String> userIds;
    private WfiDeliverService wfiDeliverService;
    private SecurityUtils securityUtils;
    private HttpSession session;

    public InviteCmd(HttpSession session, BpmAdapter bpmAdapter, SecurityUtils securityUtils, TaskService taskService, WfiDeliverService wfiDeliverService, QueryUtil queryUtil, IUser iUser, String taskId, List<String> userIds) {
        this.session = session;
        this.bpmAdapter = bpmAdapter;
        this.securityUtils = securityUtils;
        this.taskService = taskService;
        this.wfiDeliverService = wfiDeliverService;
        this.queryUtil = queryUtil;
        this.iUser = iUser;
        this.taskId = taskId;
        this.userIds = userIds;
    }

    @Override
    public List<Task> execute(CommandContext commandContext) {
        List<Task> newTasks = new ArrayList<>();
        if (userIds != null && userIds.size() > 0) {
            HistoricTaskInstance task = queryUtil.getHistoricTaskInstance(taskId);
            String wfiDeliverIdFrom = queryUtil.getWfiDeliverIdFrom(taskId);
            List<Task> unfinishedTasks = queryUtil.listUnfinishedTasks(task.getProcessInstanceId(), wfiDeliverIdFrom, task.getName());
            if (unfinishedTasks.size() == 0) {
                // 如果节点已办结，则不能加签
                throw new FinishedNodeUnsupportInviteException(taskId);
            }
            WfiFlowDO wfiFlow = queryUtil.getWfiFlowById(task.getProcessInstanceId());
            Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
            Node node = flow.getNodeById(task.getName());
            String currentUserId = securityUtils.getCurrentUserId();
            checkAuthority(node, task, currentUserId);
            WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverById(wfiDeliverIdFrom);
            DeliverInfo deliverInfo = wfiDeliver.getDeliverInfoClass();
            DeliverNode deliverNode = deliverInfo.getDeliverNodeByNodeId(task.getName());
            checkUserExists(deliverNode, wfiDeliverIdFrom);
            // 当前节点未办理完成的一个任务
            Task unfinishedTask = unfinishedTasks.get(0);
            TaskEntityImpl unfinishedTaskEntity = (TaskEntityImpl) unfinishedTask;

            updateAssignList(node, unfinishedTaskEntity, userIds);
            updateDeliverInfo(wfiDeliver, deliverInfo, deliverNode, userIds, currentUserId);
            session.setAttribute(SessionAttribute.TASK_WFD_DELIVER, wfiDeliver);
            session.setAttribute(SessionAttribute.TASK_DELIVER_INFO, deliverInfo);
            SessionAttribute.initElSession(session, wfiFlow.getId(), flow, wfiFlow.getBusinessKey());
            createInstances(unfinishedTaskEntity, userIds);
//            for (String userId : userIds) {
//                Task newTask = newTask(unfinishedTaskEntity, userId, wfiDeliverIdFrom);
//                newTasks.add(newTask);
//                // TODO: 2020/6/3 发送加签消息提醒
//            }
        }
        return newTasks;
    }

    /**
     * 验证是否可以加签
     *
     * @param node          节点
     * @param task          任务
     * @param currentUserId 当前用户id
     * @author JonnyJiang
     * @date 2020/6/5 10:28
     */

    private void checkAuthority(Node node, HistoricTaskInstance task, String currentUserId) {
        UserType userType = bpmAdapter.getUserType(node, task.getId(), currentUserId);
        if (IntegerUtils.isTrue(node.getAllowHostInvite())) {
            // 如果允许主办人加签
            if (UserType.Host.equals(userType)) {
                // 如果是主办人，则允许加签
                return;
            } else if (queryUtil.hasAdminAuth(node.getFlow().getId(), currentUserId)) {
                // 如果有监控权限，则允许加签
                return;
            }
            throw new NoAccessToInviteException(node.getFlow().getId(), currentUserId);
        } else if (IntegerUtils.isTrue(node.getAllowAssistInvite())) {
            // 如果允许协办人加签
            if (UserType.Assistant.equals(userType)) {
                // 如果是协办人，则允许加签
                return;
            } else if (queryUtil.hasAdminAuth(node.getFlow().getId(), currentUserId)) {
                // 如果有监控权限，则允许加签
                return;
            }
        }
        throw new NotAllowInviteException(node.getFlow().getId());
    }

    /**
     * 验证加签用户是否已是办理人
     *
     * @param deliverNode      节点转交信息
     * @param wfiDeliverIdFrom 来自的转交信息id
     * @author JonnyJiang
     * @date 2020/6/5 10:30
     */

    private void checkUserExists(DeliverNode deliverNode, String wfiDeliverIdFrom) {
        List<String> existUserIds = deliverNode.getDeliverUsers().stream().filter(o -> userIds.contains(o.getUserId())).map(DeliverUser::getUserId).collect(Collectors.toList());
        if (existUserIds.size() > 0) {
            List<SysUserDO> existUsers = iUser.getUsersByIds(existUserIds);
            if (existUsers.size() != existUserIds.size()) {
                for (String userId : existUserIds) {
                    if (existUsers.stream().noneMatch(o -> o.getId().equals(userId))) {
                        throw new SysUserNotFoundByIdException(userId);
                    }
                }
            } else {
                throw new DeliverUserExistException(wfiDeliverIdFrom, existUsers);
            }
        }
    }

    private void setLoopVariable(DelegateExecution execution, String variableName, Object value) {
        execution.setVariableLocal(variableName, value);
    }

    private Integer getLoopVariable(DelegateExecution execution, String variableName) {
        Object value = execution.getVariableLocal(variableName);
        DelegateExecution parent = execution.getParent();
        while (value == null && parent != null) {
            value = parent.getVariableLocal(variableName);
            parent = parent.getParent();
        }
        return (Integer) (value != null ? value : 0);
    }

    private void createInstances(TaskEntityImpl unfinishedTaskEntity, List<String> userIds) {
        /**
         * 代码逻辑来自activiti-engine\src\main\java\org\activiti\engine\impl\bpmn\behavior\ParallelMultiInstanceBehavior.java
         */
        ExecutionEntity parentExecutionEntity = unfinishedTaskEntity.getExecution().getParent();
        // 实例总数
        int nrOfInstances = getLoopVariable(parentExecutionEntity, NUMBER_OF_INSTANCES) + userIds.size();
//        int nrOfCompletedInstances = getLoopVariable(parentExecutionEntity, NUMBER_OF_COMPLETED_INSTANCES);
        // 活动实例总数
        int nrOfActiveInstances = getLoopVariable(parentExecutionEntity, NUMBER_OF_ACTIVE_INSTANCES) + userIds.size();

        setLoopVariable(parentExecutionEntity, NUMBER_OF_INSTANCES, nrOfInstances);
        setLoopVariable(parentExecutionEntity, NUMBER_OF_ACTIVE_INSTANCES, nrOfActiveInstances);

        CommandContext commandContext = Context.getCommandContext();
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        FlowElement activity = unfinishedTaskEntity.getExecution().getCurrentFlowElement();
        for (int i = 0; i < userIds.size(); i++) {
            ExecutionEntity concurrentExecution = executionEntityManager.createChildExecution(parentExecutionEntity);
            concurrentExecution.setCurrentFlowElement(activity);
            concurrentExecution.setActive(true);
            concurrentExecution.setScope(false);
            Integer loopCounter = nrOfInstances - userIds.size() + i;
            setLoopVariable(concurrentExecution, collectionElementIndexVariable, loopCounter);
            setLoopVariable(concurrentExecution, ManualNode.ASSIGNEE, userIds.get(i));
            Context.getAgenda().planContinueMultiInstanceOperation(concurrentExecution);
        }
    }

    /**
     * 更新节点对应的UserTask的办理人
     *
     * @param node                 节点
     * @param unfinishedTaskEntity 节点任务
     * @param userIds              增加的办理人列表
     * @author JonnyJiang
     * @date 2020/6/5 10:33
     */

    private void updateAssignList(Node node, TaskEntityImpl unfinishedTaskEntity, List<String> userIds) {
        Execution unfinishedExecution = unfinishedTaskEntity.getExecution();
        ExecutionEntity unfinishedExecutionEntity = (ExecutionEntity) unfinishedExecution;
        BpmnModel bpmnModel = queryUtil.getBpmnModel(unfinishedTaskEntity.getProcessDefinitionId());
        UserTask userTask = BpmnUtil.getUserTaskByNodeId(node.getFlow(), bpmnModel, node);
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = userTask.getLoopCharacteristics();
        String assigneeListVariableName = multiInstanceLoopCharacteristics.getInputDataItem();
        List<String> assigneeList = (List<String>) unfinishedExecutionEntity.getVariable(assigneeListVariableName);
        assigneeList.addAll(userIds);
        unfinishedExecutionEntity.setVariable(assigneeListVariableName, assigneeList);
    }

    private void updateDeliverInfo(WfiDeliverDO wfiDeliver, DeliverInfo deliverInfo, DeliverNode deliverNode, List<String> userIds, String currentUserId) {
        DeliverUser currentDeliverUser = deliverNode.getDeliverUserByUserId(currentUserId);
        List<SysUserDO> newUsers = iUser.getUsersByIds(userIds);
        for (String userId : userIds) {
            Optional<SysUserDO> sysUserOpt = newUsers.stream().filter(o -> o.getId().equals(userId)).findFirst();
            if (sysUserOpt.isPresent()) {
                SysUserDO user = sysUserOpt.get();
                DeliverUser deliverUser = new DeliverUser();
                deliverUser.setUserType(UserType.Assistant.getValue());
                deliverUser.setUserId(user.getId());
                deliverUser.setRealName(user.getRealName());
                deliverUser.setInvite(IntegerUtils.TRUE_VALUE);
                // 加签时，将加签用户的部门、组织、集团信息都使用当前用户的，在多实例并行判断中作为同一实例
                deliverUser.setDepartmentId(currentDeliverUser.getDepartmentId());
                deliverUser.setOrganizationId(currentDeliverUser.getOrganizationId());
                deliverUser.setGroupId(currentDeliverUser.getGroupId());
                deliverNode.addDeliverUser(deliverUser);
            } else {
                throw new SysUserNotFoundByIdException(userId);
            }
        }
        wfiDeliverService.updateDeliverInfo(wfiDeliver.getId(), deliverInfo);
        wfiDeliver.setDeliverInfo(JSONObject.toJSONString(deliverInfo));
    }
}