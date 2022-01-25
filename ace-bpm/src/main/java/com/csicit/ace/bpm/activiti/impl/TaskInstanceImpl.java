package com.csicit.ace.bpm.activiti.impl;

import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.activiti.AbstractTaskInstance;
import com.csicit.ace.bpm.exception.WfiFlowNotFoundByIdException;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.common.config.SpringContextUtils;
import org.activiti.engine.task.Task;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JonnyJiang
 * @date 2019/9/10 10:43
 */
public class TaskInstanceImpl extends AbstractTaskInstance {
    public TaskInstanceImpl(Task task, WfiFlowDO wfiFlow) {
        super(task, wfiFlow);
    }

    public static List<TaskInstance> castToBpmTask(List<Task> tasks) {
        WfiFlowService wfiFlowService = SpringContextUtils.getBean(WfiFlowService.class);
        Collection<WfiFlowDO> wfiFlows = wfiFlowService.listByIds(tasks.stream().map(Task::getProcessInstanceId).collect(Collectors.toList()));
        List<TaskInstance> l = new ArrayList<>();
        for (Task task : tasks) {
            Optional<WfiFlowDO> wfiFlow = wfiFlows.stream().filter(o -> o.getId().equals(task.getProcessInstanceId())).findFirst();
            if (wfiFlow.isPresent()) {
                l.add(new com.csicit.ace.bpm.activiti.impl.TaskInstanceImpl(task, wfiFlow.get()));
            } else {
                throw new WfiFlowNotFoundByIdException(task.getProcessInstanceId());
            }
        }
        return l;
    }

    @Override
    public Date getEndTime() {
        return null;
    }
}