package com.csicit.ace.bpm.activiti;

import org.activiti.engine.history.HistoricTaskInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/7/1 20:24
 */
public class WfiDeliverTasks {
    private String wfiDeliverId;

    /**
     * 已完成任务列表
     */
    private List<HistoricTaskInstance> finishedTasks = new ArrayList<>();
    /**
     * 未完成任务列表
     */
    private List<HistoricTaskInstance> unfinishedTasks = new ArrayList<>();

    public WfiDeliverTasks(String wfiDeliverId) {
        this.wfiDeliverId = wfiDeliverId;
    }

    public void add(HistoricTaskInstance task) {
        if (task.getEndTime() == null) {
            unfinishedTasks.add(task);
        } else {
            finishedTasks.add(task);
        }
    }

    public String getWfiDeliverId() {
        return wfiDeliverId;
    }

    public List<HistoricTaskInstance> getFinishedTasks() {
        return finishedTasks;
    }

    public List<HistoricTaskInstance> getUnfinishedTasks() {
        return unfinishedTasks;
    }
}
