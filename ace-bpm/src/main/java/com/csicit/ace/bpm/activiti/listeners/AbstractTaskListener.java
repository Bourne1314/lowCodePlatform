package com.csicit.ace.bpm.activiti.listeners;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.SessionAttribute;
import com.csicit.ace.bpm.activiti.ProcessVariableName;
import com.csicit.ace.bpm.activiti.TaskVariableName;
import com.csicit.ace.bpm.exception.WfiFlowNotFoundByIdException;
import com.csicit.ace.bpm.pojo.domain.WfiDeliverDO;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.service.WfiDeliverService;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author JonnyJiang
 * @date 2019/11/6 11:48
 */
public abstract class AbstractTaskListener implements TaskListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTaskListener.class);

    /**
     * 获取任务事件类型
     *
     * @return 任务事件类型
     * @author JonnyJiang
     * @date 2019/11/6 11:52
     */

    protected abstract TaskEventType getTaskEventType();

    @Override
    public void notify(DelegateTask delegateTask) {
        if (StringUtils.isNotBlank(delegateTask.getOwner())) {
            // 委托不需要转交信息
            LOGGER.debug("process has delegated");
            return;
        }
        if (delegateTask.getVariables().containsKey(ProcessVariableName.Invalid.getName())) {
            if (IntegerUtils.isTrue((Integer) delegateTask.getVariables().get(ProcessVariableName.Invalid.getName()))) {
                LOGGER.debug("process instance invalid");
                return;
            }
        }
        if (delegateTask instanceof TaskEntityImpl) {
            TaskEntityImpl taskEntity = (TaskEntityImpl) delegateTask;
            WfiFlowService wfiFlowService = SpringContextUtils.getBean(WfiFlowService.class);
            WfiFlowDO wfiFlow = wfiFlowService.getById(taskEntity.getProcessInstanceId());
            if (wfiFlow == null) {
                throw new WfiFlowNotFoundByIdException(taskEntity.getProcessInstanceId());
            }
            Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
            Node node = flow.getNodeById(delegateTask.getName());
            notify(node, getDeliverInfo(taskEntity), taskEntity, wfiFlow);
        }
    }

    private DeliverInfo getDeliverInfo(TaskEntityImpl taskEntity) {
        WfiDeliverService wfiDeliverService = SpringContextUtils.getBean(WfiDeliverService.class);
        DeliverInfo deliverInfo = null;
        if (EVENTNAME_CREATE.equals(taskEntity.getEventName()) || EVENTNAME_ASSIGNMENT.equals(taskEntity.getEventName())) {
            HttpSession httpSession = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            Object deliverInfoObj = httpSession.getAttribute(SessionAttribute.TASK_DELIVER_INFO);
            if (deliverInfoObj != null) {
                if (deliverInfoObj instanceof DeliverInfo) {
                    deliverInfo = (DeliverInfo) deliverInfoObj;
                }
            }
            WfiDeliverDO wfiDeliver = null;
            Object wfiDeliverObj = httpSession.getAttribute(SessionAttribute.TASK_WFD_DELIVER);
            if (wfiDeliverObj != null) {
                if (wfiDeliverObj instanceof WfiDeliverDO) {
                    wfiDeliver = (WfiDeliverDO) wfiDeliverObj;
                }
            }
            if (wfiDeliver == null) {
                throw new BpmException(LocaleUtils.getWfiDeliverNotFound());
            } else {
                if (StringUtils.isEmpty(wfiDeliver.getFlowId())) {
                    wfiDeliver.setFlowId(taskEntity.getProcessInstanceId());
                    wfiDeliverService.update(new WfiDeliverDO(), new UpdateWrapper<WfiDeliverDO>().set("FLOW_ID", wfiDeliver.getFlowId()).eq("ID", wfiDeliver.getId()));
                }
            }
            taskEntity.setVariableLocal(TaskVariableName.WFI_DELIVER_ID_FROM.getName(), wfiDeliver.getId());
        } else {
            String deliverInfoId = (String) taskEntity.getVariablesLocal().get(TaskVariableName.WFI_DELIVER_ID_FROM.getName());
            if (StringUtils.isNotEmpty(deliverInfoId)) {
                WfiDeliverDO wfiDeliver = wfiDeliverService.getById(deliverInfoId);
                if (wfiDeliver == null) {
                    throw new BpmException(LocaleUtils.getWfiDeliverNotFoundById(deliverInfoId));
                }
                deliverInfo = JSONObject.parseObject(wfiDeliver.getDeliverInfo(), DeliverInfo.class);
            }
        }
        if (deliverInfo == null) {
            throw new BpmException(LocaleUtils.getDeliverInfoNotFound());
        }
        return deliverInfo;
    }

//    private DeliverInfo getDeliverInfoFromVariables(TaskEntityImpl taskEntity) {
//        String deliverInfoId;
//        /**
//         * 暂时从流程变量中取最新一次的转交信息，未来应该修改为从上一步骤中取
//         */
//        if (taskEntity.getVariablesLocal().containsKey(TaskVariableName.WFI_DELIVER_ID.getName())) {
//            deliverInfoId = (String) taskEntity.getVariablesLocal().get(TaskVariableName.WFI_DELIVER_ID.getName());
//        } else {
//            if (EVENTNAME_CREATE.equals(taskEntity.getEventName())) {
//                Map<String, Object> processVariables = taskEntity.getProcessInstance().getVariables();
//                Boolean isMain = false;
//                if (processVariables.containsKey(ProcessVariableName.IS_MAIN.getName())) {
//                    isMain = (Boolean) processVariables.get(ProcessVariableName.IS_MAIN.getName());
//                }
//                if (isMain) {
//                    deliverInfoId = (String) processVariables.get(TaskVariableName.WFI_DELIVER_ID.getName());
//                } else {
//                    if (taskEntity.getProcessInstance().getSuperExecution() == null) {
//                        throw new BpmException(LocaleUtils.getSuperExecutionNotFound());
//                    } else {
//                        deliverInfoId = (String) taskEntity.getProcessInstance().getSuperExecution().getVariables().get(TaskVariableName.WFI_DELIVER_ID.getName());
//                    }
//                }
//                if (StringUtils.isEmpty(deliverInfoId)) {
//                    throw new BpmException(LocaleUtils.getDeliverInfoNotFound());
//                }
//                taskEntity.setVariableLocal(TaskVariableName.WFI_DELIVER_ID.getName(), deliverInfoId);
//            } else {
//                throw new BpmException(LocaleUtils.getDeliverInfoNotFound());
//            }
//        }
//        DeliverInfo deliverInfo = null;
//        if (StringUtils.isNotEmpty(deliverInfoId)) {
//            WfiDeliverService wfiDeliverService = SpringContextUtils.getBean(WfiDeliverService.class);
//            WfiDeliverDO wfiDeliver = wfiDeliverService.getById(deliverInfoId);
//            if (wfiDeliver == null) {
//                throw new BpmException(LocaleUtils.getWfiDeliverNotFoundById(deliverInfoId));
//            }
//            if (StringUtils.isEmpty(wfiDeliver.getFlowId())) {
//                wfiDeliver.setFlowId(taskEntity.getProcessInstanceId());
//                wfiDeliverService.update(new WfiDeliverDO(), new UpdateWrapper<WfiDeliverDO>().set("FLOW_ID", wfiDeliver.getFlowId()).eq("ID", deliverInfoId));
//            }
//            deliverInfo = JSONObject.parseObject(wfiDeliver.getDeliverInfo(), DeliverInfo.class);
//        }
//        return deliverInfo;
//    }

    protected abstract void notify(Node node, DeliverInfo deliverInfo, TaskEntityImpl taskEntity, WfiFlowDO wfiFlow);
//
//    private void invokeCustomEvents(Node node, DeliverInfo deliverInfo, TaskInstance taskInstance) {
//        for (NodeEvent event : node.getEvents()) {
//            if (getTaskEventType().isEquals(event.getEventType())) {
//                if (StringUtils.isNotEmpty(event.getEventClass())) {
//                    try {
//                        Class<?> classTaskListener = Class.forName(event.getEventClass());
//                        Object objTaskListener = classTaskListener.newInstance();
//                        if (objTaskListener instanceof com.csicit.ace.bpm.delegate.TaskListener) {
//                            com.csicit.ace.bpm.delegate.TaskListener taskListener = (com.csicit.ace.bpm.delegate.TaskListener) objTaskListener;
//                            taskListener.notify(getTaskEventType(), node, taskInstance);
//                        }
//                    } catch (Exception e) {
//                        LOGGER.error(e.getMessage());
//                        throw new BpmException(e.getMessage());
//                    }
//                }
//            }
//        }
//    }
}
