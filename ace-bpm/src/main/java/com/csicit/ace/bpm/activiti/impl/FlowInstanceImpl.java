package com.csicit.ace.bpm.activiti.impl;

import com.csicit.ace.bpm.FlowInstance;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.Date;

/**
 * @author JonnyJiang
 * @date 2019/9/5 15:59
 */
public class FlowInstanceImpl implements FlowInstance {
    private ProcessInstance processInstance;

    public FlowInstanceImpl(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    @Override
    public String getId() {
        return processInstance.getId();
    }

    @Override
    public String getCode() {
        return processInstance.getProcessDefinitionName();
    }

    @Override
    public String getBusinessKey() {
        return processInstance.getBusinessKey();
    }

    @Override
    public String getStarterId() {
        return processInstance.getStartUserId();
    }

    @Override
    public Date getEndTime() {
        // 正在执行中的任务，办结时间为空
        return null;
    }

    @Override
    public Date getStartTime() {
        return processInstance.getStartTime();
    }
}
