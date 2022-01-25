package com.csicit.ace.bpm.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/12/11 11:33
 */
@Data
public class RejectInfo implements Serializable {
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 驳回原因
     */
    private String rejectReason;
    /**
     * 驳回到任务列表
     */
    List<TaskRejectTo> taskRejectTos;

    public List<TaskRejectTo> getTaskRejectTos() {
        if (taskRejectTos == null) {
            taskRejectTos = new ArrayList<>();
        }
        return taskRejectTos;
    }
}
