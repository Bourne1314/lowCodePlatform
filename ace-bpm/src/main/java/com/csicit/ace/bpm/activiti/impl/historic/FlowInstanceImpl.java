package com.csicit.ace.bpm.activiti.impl.historic;

import com.csicit.ace.bpm.FlowInstance;
import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.activiti.impl.TaskInstanceImpl;
import org.activiti.engine.history.HistoricProcessInstance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/9/27 14:49
 */
public class FlowInstanceImpl implements FlowInstance {
    private HistoricProcessInstance processInstance;

    public FlowInstanceImpl(HistoricProcessInstance processInstance) {
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
        return processInstance.getEndTime();
    }

    @Override
    public Date getStartTime() {
        return processInstance.getStartTime();
    }
}
