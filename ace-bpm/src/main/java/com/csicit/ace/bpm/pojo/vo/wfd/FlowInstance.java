package com.csicit.ace.bpm.pojo.vo.wfd;

import org.activiti.engine.runtime.ProcessInstance;

/**
 * 流程实例
 *
 * @author JonnyJiang
 * @date 2019/9/5 15:51
 */
public class FlowInstance {
    private ProcessInstance processInstance;

    public FlowInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public String getProcessDefinitionKey() {
        return processInstance.getProcessDefinitionKey();
    }

    public String getName() {
        return processInstance.getName();
    }
}
