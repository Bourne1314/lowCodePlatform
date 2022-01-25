package com.csicit.ace.bpm.activiti.impl.historic;

import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.activiti.AbstractTaskInstance;
import com.csicit.ace.bpm.exception.WfiFlowNotFoundByIdException;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.common.config.SpringContextUtils;
import org.activiti.engine.history.HistoricTaskInstance;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JonnyJiang
 * @date 2019/9/27 14:56
 */
public class TaskInstanceImpl extends AbstractTaskInstance {
    private HistoricTaskInstance task;

    public TaskInstanceImpl(HistoricTaskInstance task, WfiFlowDO wfiFlow) {
        super(task, wfiFlow);
        this.task = task;
    }

    public static List<TaskInstance> castToBpmTask(List<HistoricTaskInstance> tasks) {
        WfiFlowService wfiFlowService = SpringContextUtils.getBean(WfiFlowService.class);
        Collection<WfiFlowDO> wfiFlows = wfiFlowService.listByIds(tasks.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toList()));
        List<TaskInstance> l = new ArrayList<>();
        for (HistoricTaskInstance task : tasks) {
            Optional<WfiFlowDO> wfiFlow = wfiFlows.stream().filter(o -> o.getId().equals(task.getProcessInstanceId())).findFirst();
            if (wfiFlow.isPresent()) {
                l.add(new TaskInstanceImpl(task, wfiFlow.get()));
            } else {
                throw new WfiFlowNotFoundByIdException(task.getProcessInstanceId());
            }
        }
        return l;
    }

    @Override
    public Date getEndTime() {
        return task.getEndTime();
    }
}
