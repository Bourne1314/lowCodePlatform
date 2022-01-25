package com.csicit.ace.bpm.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 驳回到的任务信息
 *
 * @author JonnyJiang
 * @date 2019/12/10 11:09
 */
@Data
public class TaskRejectTo implements Serializable {
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 节点名称
     */
    private String nodeName;
}
