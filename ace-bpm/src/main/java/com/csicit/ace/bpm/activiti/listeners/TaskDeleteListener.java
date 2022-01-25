package com.csicit.ace.bpm.activiti.listeners;

import com.csicit.ace.bpm.ListenerScanner;
import com.csicit.ace.bpm.delegate.args.TaskDeleteEventArgs;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.pojo.vo.wfd.NodeEvent;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

import java.util.List;

/**
 * 任务删除监听
 *
 * @author JonnyJiang
 * @date 2019/11/6 10:38
 */
public class TaskDeleteListener extends AbstractTaskListener {
    @Override
    protected TaskEventType getTaskEventType() {
        return TaskEventType.Delete;
    }

    @Override
    protected void notify(Node node, DeliverInfo deliverInfo, TaskEntityImpl taskEntity, WfiFlowDO wfiFlow) {
        for (NodeEvent event : node.getEvents()) {
            if (com.csicit.ace.bpm.delegate.TaskEventType.Delete.isEquals(event.getEventType())) {
                Object listener = ListenerScanner.getTaskListenerInstance(com.csicit.ace.bpm.delegate.TaskEventType.Delete, event.getEventClass());
                if (listener instanceof com.csicit.ace.bpm.delegate.TaskDeleteListener) {
                    ((com.csicit.ace.bpm.delegate.TaskDeleteListener) listener).notify(new TaskDeleteEventArgs(node, taskEntity.getId(), taskEntity.getAssignee(), taskEntity.getOwner()));
                }
            }
        }
    }
}