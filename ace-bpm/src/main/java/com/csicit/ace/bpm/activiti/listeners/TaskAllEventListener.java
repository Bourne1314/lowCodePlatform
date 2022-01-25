package com.csicit.ace.bpm.activiti.listeners;

import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

/**
 * 任务所有事件监听
 *
 * @author JonnyJiang
 * @date 2019/11/6 10:39
 */
public class TaskAllEventListener extends AbstractTaskListener {
    @Override
    protected TaskEventType getTaskEventType() {
        return TaskEventType.AllEvent;
    }

    @Override
    protected void notify(Node node, DeliverInfo deliverInfo, TaskEntityImpl taskEntity, WfiFlowDO wfiFlow) {

    }
}
