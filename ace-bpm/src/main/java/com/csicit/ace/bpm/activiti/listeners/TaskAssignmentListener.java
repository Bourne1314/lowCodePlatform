package com.csicit.ace.bpm.activiti.listeners;

import com.csicit.ace.bpm.ListenerScanner;
import com.csicit.ace.bpm.delegate.FlowEventType;
import com.csicit.ace.bpm.delegate.args.TaskAssignmentEventArgs;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Event;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.pojo.vo.wfd.NodeEvent;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

import java.util.List;

/**
 * 任务分配监听
 *
 * @author JonnyJiang
 * @date 2019/11/6 10:36
 */
public class TaskAssignmentListener extends AbstractTaskListener {
    @Override
    protected TaskEventType getTaskEventType() {
        return TaskEventType.Assignment;
    }

    @Override
    protected void notify(Node node, DeliverInfo deliverInfo, TaskEntityImpl taskEntity, WfiFlowDO wfiFlow) {
        for (NodeEvent event : node.getEvents()) {
            if (com.csicit.ace.bpm.delegate.TaskEventType.Assignment.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getTaskListenerInstance(com.csicit.ace.bpm.delegate.TaskEventType.Assignment, event.getEventClass());
                if (listener instanceof com.csicit.ace.bpm.delegate.TaskAssignmentListener) {
                    ((com.csicit.ace.bpm.delegate.TaskAssignmentListener) listener).notify(new TaskAssignmentEventArgs(taskEntity.getId(), node, taskEntity.getOriginalAssignee(), taskEntity.getAssignee()));
                }
            }
        }
    }
}