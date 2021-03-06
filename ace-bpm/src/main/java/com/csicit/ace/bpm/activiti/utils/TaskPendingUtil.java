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
        // ??????????????????
        List<HistoricTaskInstance> tasks = queryUtil.listHistoricTaskInstances(flowInstanceId);
        // ??????????????????????????????
        Map<String, Object> wfiDeliverIdFromMap = queryUtil.listTaskVariableValues(tasks, TaskVariableName.WFI_DELIVER_ID_FROM);
        // ??????????????????
        List<String> wfiDeliverIdFroms = new ArrayList<>();
        wfiDeliverIdFromMap.forEach((k, v) -> {
            wfiDeliverIdFroms.add((String) v);
        });
        Collection<WfiDeliverDO> wfiDelivers = wfiDeliverService.listByIds(wfiDeliverIdFroms);
        // ??????????????????????????????
        Map<String, WfiDeliverTasks> wfiDeliverMap = queryUtil.getWfiDeliverMap(wfiDeliverIdFromMap, tasks);
        wfiDeliverMap.forEach((wfiDeliverIdFrom, wfiDeliverTasks) -> {
            if (wfiDeliverTasks.getUnfinishedTasks().size() > 0) {
                // ????????????
                WfiDeliverDO wfiDeliver = queryUtil.getWfiDeliverById(wfiDelivers, wfiDeliverIdFrom);
                DeliverInfo deliverInfo = wfiDeliver.getDeliverInfoClass();
                DeliverNode deliverNode;
                // ????????????????????????????????????????????????????????????????????????
                for (HistoricTaskInstance unfinishedTask : wfiDeliverTasks.getUnfinishedTasks()) {
                    // ?????????????????????
                    Node node = flow.getNodeById(unfinishedTask.getName());
                    deliverNode = deliverInfo.getDeliverNodeByNodeId(node.getId());
                    if (HostMode.FirstClaim.isEquals(node.getHostMode())) {
                        // ??????????????????????????????
                        String userId = StringUtils.isEmpty(unfinishedTask.getOwner()) ? unfinishedTask.getAssignee() : unfinishedTask.getOwner();
                        String claimerId = FlowUtils.getClaimerId(node, deliverNode, userId);
                        if (StringUtils.isEmpty(claimerId)) {
                            // ?????????????????????????????????????????????
                            wfiTaskPendings.add(getNew(flow, UserType.Assistant, unfinishedTask));
                        } else {
                            if (StringUtils.equals(claimerId, userId)) {
                                // ????????????????????????????????????
                                wfiTaskPendings.add(getNew(flow, UserType.Host, unfinishedTask));
                            } else {
                                // ????????????????????????????????????????????????????????????????????????????????????
                                if (IntegerUtils.isTrue(node.getEnableAssistAhd())) {
                                    wfiTaskPendings.add(getNew(flow, UserType.Assistant, unfinishedTask));
                                }
                            }
                        }
                    } else if (HostMode.Specified.isEquals(node.getHostMode())) {
                        // ???????????????????????????????????????????????????
                        String hostId = StringUtils.isEmpty(unfinishedTask.getOwner()) ? unfinishedTask.getAssignee() : unfinishedTask.getOwner();
                        DeliverUser deliverUser = deliverNode.getDeliverUserByUserId(hostId);
                        if (UserType.Host.isEquals(deliverUser.getUserType())) {
                            // ????????????????????????????????????????????????????????????????????????
                            if (queryUtil.allowDeliverHost(node, unfinishedTask.getId(), unfinishedTask.getOwner(), unfinishedTask.getAssignee(), wfiDeliverTasks)) {
                                wfiTaskPendings.add(getNew(flow, UserType.Host, unfinishedTask));
                            }
                        } else {
                            wfiTaskPendings.add(getNew(flow, UserType.Assistant, unfinishedTask));
                        }
                    } else if (HostMode.AllowDeliver.isEquals(node.getHostMode())) {
                        // ??????????????????????????????
                        if (queryUtil.allowDeliverHost(node, unfinishedTask.getId(), unfinishedTask.getOwner(), unfinishedTask.getAssignee(), wfiDeliverTasks)) {
                            // ????????????????????????????????????????????????????????????
                            wfiTaskPendings.add(getNew(flow, UserType.Host, unfinishedTask));
                        } else {
                            // ???????????????????????????????????????????????????????????????
                            wfiTaskPendings.add(getNew(flow, UserType.Assistant, unfinishedTask));
                        }
                    } else if (HostMode.Everybody.isEquals(node.getHostMode())) {
                        // ????????????????????????????????????
                        if (IntegerUtils.isTrue(node.getForceSequence())) {
                            // ?????????????????????????????????
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
                                        // ??????????????????????????????????????????????????????????????????????????????????????????
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
                            // ????????????????????????????????????????????????????????????
                            wfiTaskPendings.add(getNew(flow, UserType.Host, unfinishedTask));
                        } else {
                            // ???????????????????????????????????????????????????????????????
                            wfiTaskPendings.add(getNew(flow, UserType.Assistant, unfinishedTask));
                        }
                    }
                }
            }
        });
    }
}