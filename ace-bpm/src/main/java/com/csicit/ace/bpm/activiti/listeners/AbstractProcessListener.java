package com.csicit.ace.bpm.activiti.listeners;

import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.activiti.ProcessVariableName;
import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/6 11:13
 */
public abstract class AbstractProcessListener implements ExecutionListener {
    protected SecurityUtils securityUtils = SpringContextUtils.getBean(SecurityUtils.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProcessListener.class);

    /**
     * 获取流程事件类型
     *
     * @return 流程事件类型
     * @author JonnyJiang
     * @date 2019/11/6 11:32
     */

    protected abstract ProcessEventType getProcessEventType();
//
//    private void invokeCustomEvents(Flow flow, FlowInstance flowInstance, Map<String, Object> variables) {
//        for (Event event : flow.getEvents()) {
//            if (getFlowEventType().isEquals(event.getEventType())) {
//                if (StringUtils.isNotEmpty(event.getClassName())) {
//                    try {
//                        Class<?> classFlowListener = Class.forName(event.getClassName());
//                        Object objFlowListener = classFlowListener.newInstance();
//                        if (objFlowListener instanceof FlowListener) {
//                            FlowListener flowListener = (FlowListener) objFlowListener;
//                            flowListener.notify(getFlowEventType(), flow, flowInstance, variables);
//                        }
//                    } catch (Exception e) {
//                        LOGGER.error(e.getMessage());
//                        throw new BpmException(e.getMessage());
//                    }
//                }
//            }
//        }
//    }

    @Override
    public void notify(DelegateExecution delegateExecution) {
        LOGGER.debug("process instance id: " + delegateExecution.getProcessInstanceId());
        if (delegateExecution.getVariables().containsKey(ProcessVariableName.Invalid.getName())) {
            if (IntegerUtils.isTrue((Integer) delegateExecution.getVariables().get(ProcessVariableName.Invalid.getName()))) {
                LOGGER.debug("process instance invalid");
                return;
            }
        }
        if (delegateExecution instanceof ExecutionEntityImpl) {
            ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) delegateExecution;
            BpmAdapter bpmAdapter = SpringContextUtils.getBean(BpmAdapter.class);
            RepositoryService repositoryService = SpringContextUtils.getBean(RepositoryService.class);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(securityUtils.getAppName()).processDefinitionId(executionEntity.getProcessDefinitionId()).singleResult();
            if (processDefinition == null) {
                throw new BpmException(LocaleUtils.getProcessDefinitionNotFoundById(executionEntity.getProcessDefinitionId()));
            }
            Boolean isMain = false;
            if (delegateExecution.getVariables().containsKey(ProcessVariableName.IS_MAIN.getName())) {
                isMain = (Boolean) delegateExecution.getVariables().get(ProcessVariableName.IS_MAIN.getName());
            } else {
                delegateExecution.setVariable(ProcessVariableName.IS_MAIN.getName(), false);
            }
            WfdVFlowDO wfdVFlow = bpmAdapter.getEffectiveWfdVFlowByCode(processDefinition.getName());
            Flow flow = FlowUtils.getFlow(wfdVFlow.getModel());
            notify(isMain, wfdVFlow, flow, executionEntity);
//            invokeCustomEvents(flow, new FlowInstanceImpl(executionEntity.getProcessInstance()), delegateExecution.getVariables());
        }
    }

    protected void notify(Boolean isMain, WfdVFlowDO wfdVFlow, Flow flow, ExecutionEntityImpl executionEntity) {

    }
}
