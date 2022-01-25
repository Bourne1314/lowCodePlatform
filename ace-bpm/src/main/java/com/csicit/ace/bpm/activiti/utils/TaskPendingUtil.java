package com.csicit.ace.bpm.activiti.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.csicit.ace.bpm.activiti.TaskVariableName;
import com.csicit.ace.bpm.activiti.WfiDeliverTasks;
import com.csicit.ace.bpm.enums.HostMode;
import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.exception.DeliverUserSortIndexIsNullException;
import com.csicit.ace.bpm.pojo.domain.WfiDeliverDO;
import com.csicit.ace.bpm.pojo.domain.WfiTaskPendingDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.DeliverNode;
import com.csicit.ace.bpm.pojo.vo.DeliverUser;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.service.WfiDeliverService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;
import liquibase.util.ObjectUtil;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TaskPendingUtil {
    @Autowired
    private QueryUtil queryUtil;
    @Autowired
    private WfiDeliverService wfiDeliverService;

    private WfiTaskPendingDO getNew(Flow flow, UserType userType, HistoricTaskInstance task) {
        WfiTaskPendingDO wfiTaskPending = new WfiTaskPendingDO();
        wfiTaskPending.setId(UUID.randomUUID().toString());
        wfiTaskPending.setTaskId(task.getId());
        wfiTaskPending.setFlowId(task.getProcessInstanceId());
        wfiTaskPending.setAppId(task.getTenantId());
        wfiTaskPending.setUserType(userType.getValue());
        wfiTaskPending.setCreateTime(task.getCreateTime());
        wfiTaskPending.setUserId(task.getAssignee());
        wfiTaskPending.setNodeId(task.getName());
        wfiTaskPending.setFlowCode(flow.getCode());
        return wfiTaskPending;
    }

    public void resolveTaskPending(Collection<WfiTaskPendingDO> wfiTaskPendings, String flowInstanceId, Flow flow) {
        // 流程所有任务
        List<HistoricTaskInstance> tasks = queryUtil.listHistoricTaskInstances(flowInstanceId);
        // 获取任务来自转交信息
        Map<String, Object> wfiDeliverIdFromMap = queryUtil.listTaskVariableValues(tasks, TaskVariableName.WFI_DELIVER_ID_FROM);
        // 获取转交信息
        List<String> wfiDeliverIdFroms = new ArrayList<>();
        wfiDeliverIdFromMap.forEach((k, v) -> {
            wfiDeliverIdFroms.add((String) v);
        });
        Collection<WfiDeliverDO> wfiDelivers = wfiDeliverService.listByIds(wfiDeliverIdFroms);
        // 每次转交产生的任务图
        Map<String, WfiDeliverTasks> wfiDeliverMap = queryUtil.getWfiDeliverMap(wfiDeliverIdFromMap, tasks);
        wfiDeliverMap.forEach((wfiDeliverIdFrom, wfiDeliverTasks) -> {
            if (wfiDeliverTasks.getUnfinishedTasks().size() > 0) {
                // 转交信息
                WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverById(wfiDelivers, wfiDeliverIdFrom);
                DeliverInfo deliverInfo = wfiDeliver.getDeliverInfoClass();
                DeliverNode deliverNode;
                // 如果有未完成的任务，判断当前任务是否处于待办状态
                for (HistoricTaskInstance unfinishedTask : wfiDeliverTasks.getUnfinishedTasks()) {
                    // 任务对应的节点
                    Node node = flow.getNodeById(unfinishedTask.getName());
                    deliverNode = deliverInfo.getDeliverNodeByNodeId(node.getId());
                    if (HostMode.FirstClaim.isEquals(node.getHostMode())) {
                        // 接收后不允许继续办理
                        String userId = StringUtils.isEmpty(unfinishedTask.getOwner()) ? unfinishedTask.getAssignee() : unfinishedTask.getOwner();
                        String claimerId = FlowUtils.getClaimerId(node, deliverNode, userId);
                        if (StringUtils.isEmpty(claimerId)) {
                            // 如果没有被接收，则全部加入待办
                            wfiTaskPendings.add(getNew(flow, UserType.Assistant, unfinishedTask));
                        } else {
                            if (StringUtils.equals(claimerId, userId)) {
                                // 如果是接收人，则加入待办
                                wfiTaskPendings.add(getNew(flow, UserType.Host, unfinishedTask));
                            } else {
                                // 如果不是接收人，且主办人接收后仍允许继续办理，则加入待办
                                if (IntegerUtils.isTrue(node.getEnableAssistAhd())) {
                                    wfiTaskPendings.add(getNew(flow, UserType.Assistant, unfinishedTask));
                                }
                            }
                        }
                    } else if (HostMode.Specified.isEquals(node.getHostMode())) {
                        // 指定主办人，则未完成的任务都是待办
                        String hostId = StringUtils.isEmpty(unfinishedTask.getOwner()) ? unfinishedTask.getAssignee() : unfinishedTask.getOwner();
                        DeliverUser deliverUser = deliverNode.getDeliverUserByUserId(hostId);
                        if (UserType.Host.isEquals(deliverUser.getUserType())) {
                            // 判断是否有前序办理人未办理，如果有，则不加入待办
                            if (queryUtil.allowDeliverHost(node, unfinishedTask.getId(), unfinishedTask.getOwner(), unfinishedTask.getAssignee(), wfiDeliverTasks)) {
                                wfiTaskPendings.add(getNew(flow, UserType.Host, unfinishedTask));
                            }
                        } else {
                            wfiTaskPendings.add(getNew(flow, UserType.Assistant, unfinishedTask));
                        }
                    } else if (HostMode.AllowDeliver.isEquals(node.getHostMode())) {
                        // 允许结转后，加入待办
                        if (queryUtil.allowDeliverHost(node, unfinishedTask.getId(), unfinishedTask.getOwner(), unfinishedTask.getAssignee(), wfiDeliverTasks)) {
                            // 如果以主办人身份允许结转，则加入主办待办
                            wfiTaskPendings.add(getNew(flow, UserType.Host, unfinishedTask));
                        } else {
                            // 如果不允许以主办人身份结转，则加入协办待办
                            wfiTaskPendings.add(getNew(flow, UserType.Assistant, unfinishedTask));
                        }
                    } else if (HostMode.Everybody.isEquals(node.getHostMode())) {
                        // 满足结转条件后，加入待办
                        if (IntegerUtils.isTrue(node.getForceSequence())) {
                            // 判断前面的人是否已办结
                            DeliverUser deliverUserBefore = null;
                            DeliverUser currentDeliverUser = deliverNode.getDeliverUserByUserId(unfinishedTask.getAssignee());
                            if (ObjectUtils.isNull(currentDeliverUser.getSortIndex())) {
                                for (int i = 0; i < deliverNode.getDeliverUsers().size(); i++) {
                                    DeliverUser deliverUser = deliverNode.getDeliverUsers().get(i);
                                    if (StringUtils.equals(unfinishedTask.getAssignee(), deliverUser.getUserId())) {
                                        break;
                                    } else {
                                        if (wfiDeliverTasks.getUnfinishedTasks().stream().anyMatch(t -> StringUtils.equals(t.getAssignee(), deliverUser.getUserId()))) {
                                            deliverUserBefore = deliverUser;
                                            break;
                                        }
                                    }
                                }
                            } else {
                                for (int i = 0; i < deliverNode.getDeliverUsers().size(); i++) {
                                    DeliverUser deliverUser = deliverNode.getDeliverUsers().get(i);
                                    if(deliverUser.getDelegateMap().size() > 0)
                                    {
                                        // 如果已经委托给别人，则不需要判断自己，只需要判断委托给的对象
                                        continue;
                                    }
                                    if(deliverUser.getSortIndex() == null)
                                    {
                                        throw new DeliverUserSortIndexIsNullException(deliverUser.getUserId(), deliverUser.getRealName());
                                    }
                                    if (currentDeliverUser.getSortIndex() > deliverUser.getSortIndex()) {
                                        if (wfiDeliverTasks.getUnfinishedTasks().stream().anyMatch(t -> StringUtils.equals(t.getAssignee(), deliverUser.getUserId()))) {
                                            deliverUserBefore = deliverUser;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (deliverUserBefore != null) {
                                continue;
                            }
                        }
                        if (queryUtil.allowDeliverHost(node, unfinishedTask.getId(), unfinishedTask.getOwner(), unfinishedTask.getAssignee(), wfiDeliverTasks)) {
                            // 如果以主办人身份允许结转，则加入主办待办
                            wfiTaskPendings.add(getNew(flow, UserType.Host, unfinishedTask));
                        } else {
                            // 如果不允许以主办人身份结转，则加入协办待办
                            wfiTaskPendings.add(getNew(flow, UserType.Assistant, unfinishedTask));
                        }
                    }
                }
            }
        });
    }
}