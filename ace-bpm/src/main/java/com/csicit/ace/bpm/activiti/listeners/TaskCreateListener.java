package com.csicit.ace.bpm.activiti.listeners;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.ListenerScanner;
import com.csicit.ace.bpm.activiti.TaskVariableName;
import com.csicit.ace.bpm.delegate.args.TaskCreateEventArgs;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.pojo.vo.wfd.NodeEvent;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.utils.StringUtils;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

/**
 * 任务创建监听
 *
 * @author JonnyJiang
 * @date 2019/10/10 20:23
 */
public class TaskCreateListener extends AbstractTaskListener {
    @Override
    protected TaskEventType getTaskEventType() {
        return TaskEventType.Create;
    }

    @Override
    protected void notify(Node node, DeliverInfo deliverInfo, TaskEntityImpl taskEntity, WfiFlowDO wfiFlow) {
        // 指定主办人, 设置主办人
        if (deliverInfo == null) {
            throw new BpmException(LocaleUtils.getDeliverInfoNotFound());
        } else {
            // 如果没有指定任务ID，则使用流程实例ID作为任务的上级ID
            if (StringUtils.isEmpty(deliverInfo.getTaskId())) {
                taskEntity.setVariableLocal(TaskVariableName.PARENT_TASK_ID.getName(), wfiFlow.getId());
            } else {
                taskEntity.setVariableLocal(TaskVariableName.PARENT_TASK_ID.getName(), deliverInfo.getTaskId());
            }
//            taskEntity.setVariableLocal(TaskVariableName.NAME.getName(), node.getName());
//            taskEntity.setVariableLocal(TaskVariableName.CODE.getName(), node.getCode());

//            for (DeliverNode deliverNode :
//                    deliverInfo.getDeliverNodes()) {
//                if (deliverNode.getNodeId().equals(node.getId())) {
//                    if (deliverNode.getDeliverUsers().size() == 0) {
//                        throw new BpmException(LocaleUtils.getNodeUsersNotFound(node.getName(), node.getCode()));
//                    } else {
//                        if (deliverNode.getDeliverUsers().size() == 1) {
//                            String userId = deliverNode.getDeliverUsers().get(0).getUserId();
//                            taskEntity.addUserIdentityLink(userId, IdentityLinkType.ASSIGNEE);
//                            taskService.claim(taskEntity.getId(), userId);
//                        } else {
//                            for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
//                                if (UserType.Host.equals(deliverUser.getUserId())) {
//                                    taskEntity.addUserIdentityLink(deliverUser.getUserId(), IdentityLinkType.ASSIGNEE);
//                                    taskService.claim(taskEntity.getId(), deliverUser.getUserId());
//                                } else {
//                                    taskEntity.addCandidateUser(deliverUser.getUserId());
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
        for (NodeEvent event : node.getEvents()) {
            if (com.csicit.ace.bpm.delegate.TaskEventType.Create.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getTaskListenerInstance(com.csicit.ace.bpm.delegate.TaskEventType.Create, event.getEventClass());
                if (listener instanceof com.csicit.ace.bpm.delegate.TaskCreateListener) {
                    ((com.csicit.ace.bpm.delegate.TaskCreateListener) listener).notify(new TaskCreateEventArgs(node, deliverInfo, wfiFlow));
                }
            }
        }
    }
}