package com.csicit.ace.bpm.pojo.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务-工作委托
 *
 * @author JonnyJiang
 * @date 2019/10/31 9:55
 */
@Data
public class TaskDelegateVO implements Serializable {
    /**
     * 任务id
     */
    private String id;
    /**
     * 工作文号
     */
    private String workNo;
    /**
     * 任务名称
     */
    private String nodeName;
    /**
     * 任务名称
     */
    private String flowInstanceId;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 被委托人ID
     */
    private String delegateId;
    /**
     * 委托人ID
     */
    private String deleUserId;
    /**
     * 发起人ID
     */
    private String starterId;
    /**
     * 被委托人姓名
     */
    private String delegate;
    /**
     * 委托时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date delegateStartTime;

}