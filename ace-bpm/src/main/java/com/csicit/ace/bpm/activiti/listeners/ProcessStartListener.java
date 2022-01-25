package com.csicit.ace.bpm.activiti.listeners;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.FlowNoGenerator;
import com.csicit.ace.bpm.activiti.ProcessVariableName;
import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.config.SpringContextUtils;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/10/9 19:58
 */
public class ProcessStartListener extends AbstractProcessListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessStartListener.class);

    @Override
    protected ProcessEventType getProcessEventType() {
        return ProcessEventType.Start;
    }

    @Override
    protected void notify(Boolean isMain, WfdVFlowDO wfdVFlow, Flow flow, ExecutionEntityImpl executionEntity) {
        WfiFlowService wfiFlowService = SpringContextUtils.getBean(WfiFlowService.class);
        WfiFlowDO wfiFlow = wfiFlowService.getById(executionEntity.getProcessInstanceId());
        if (wfiFlow == null) {
            wfiFlow = new WfiFlowDO();
            wfiFlow.setId(executionEntity.getProcessInstanceId());
            wfiFlow.setModel(flow.getModel());
            wfiFlow.setVFlowId(wfdVFlow.getId());
            wfiFlow.setFlowId(flow.getId());
            if (isMain) {
                wfiFlow.setBusinessKey(executionEntity.getProcessInstanceBusinessKey());
            } else {
                if (executionEntity.getSuperExecution() == null) {
                    throw new BpmException(LocaleUtils.getSuperExecutionNotFound());
                } else {
                    wfiFlow.setBusinessKey(executionEntity.getSuperExecution().getProcessInstanceBusinessKey());
                    executionEntity.setBusinessKey(wfiFlow.getBusinessKey());
                    executionEntity.setStartUserId(executionEntity.getSuperExecution().getProcessInstance().getStartUserId());
                }
            }
            wfiFlow.setFlowNo(new FlowNoGenerator(flow, executionEntity.getVariables()).generate(wfiFlow.getId()));
            wfiFlow.setFlowCode(flow.getCode());
            wfiFlow.setAppId(securityUtils.getAppName());
            wfiFlowService.save(wfiFlow);
        }
        LOGGER.debug("wfiflow created: " + wfiFlow.getId());
        executionEntity.setVariable(ProcessVariableName.FLOW_NO.getName(), wfiFlow.getFlowNo());
        executionEntity.setVariable(ProcessVariableName.FLOW_FORM_URL.getName(), flow.getFormUrl());
    }
}