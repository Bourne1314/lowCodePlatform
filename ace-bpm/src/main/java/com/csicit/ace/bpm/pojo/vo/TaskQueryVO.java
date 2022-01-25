package com.csicit.ace.bpm.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JonnyJiang
 * @date 2019/10/31 9:55
 */
@Data
public class TaskQueryVO implements Serializable {
    /**
     * taskId
     */
    private String id;

    /**
     * flowInstanceId
     */
    private String flowInstanceId;

    /**
     * 工作文号
     */
    private String workNo;

    /**
     * 步骤名称
     */
    private String name;

    /**
     * 办理人
     */
    private String assignee;

    /**
     * 办理人姓名
     */
    private String assigneeRealName;
    /**
     * 发起人姓名
     */
    private String starterRealName;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;


}