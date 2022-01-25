package com.csicit.ace.bpm.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务-我的工作
 *
 * @author JonnyJiang
 * @date 2019/9/24 17:43
 */
@Data
public class TaskMineVO implements Serializable {
    /**
     * taskId
     */
    private String id;

    /**
     * 工作文号
     */
    private String workNo;

    /**
     * 步骤名称
     */
    private String nodeName;

    /**
     * 发起人
     */
    private String flowStarter;

    /**
     * 流程发起时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date flowStartTime;

    /**
     * 办结时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date flowEndTime;

    /**
     * 任务开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskStartTime;

    /**
     * 流程实例ID
     */
    private String flowInstanceId;

    /**
     * 业务标识
     */
    private String businessKey;

    /**
     * 应用标识
     **/
    private String appId;
}