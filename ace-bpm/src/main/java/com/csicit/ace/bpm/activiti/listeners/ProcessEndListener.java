package com.csicit.ace.bpm.activiti.listeners;

import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;

/**
 * @author JonnyJiang
 * @date 2019/11/6 10:31
 */
public class ProcessEndListener extends AbstractProcessListener {
    @Override
    protected ProcessEventType getProcessEventType() {
        return ProcessEventType.End;
    }

    @Override
    protected void notify(Boolean isMain, WfdVFlowDO wfdVFlow, Flow flow, ExecutionEntityImpl executionEntity) {

    }
}
