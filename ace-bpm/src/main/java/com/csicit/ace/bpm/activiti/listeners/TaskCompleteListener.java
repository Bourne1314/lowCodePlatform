package com.csicit.ace.bpm.activiti.listeners;

import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

/**
 * 任务结束监听
 *
 * @author JonnyJiang
 * @date 2019/10/10 20:48
 */
public class TaskCompleteListener extends AbstractTaskListener {
    @Override
    protected TaskEventType getTaskEventType() {
        return TaskEventType.Complete;
    }

    @Override
    protected void notify(Node node, DeliverInfo deliverInfo, TaskEntityImpl taskEntity, WfiFlowDO wfiFlow) {
        // 在允许结转之后办理的办理人主办，设置主办人

        // 任务结束后要发消息
    }
}