package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/2/25 9:56
 */
@Data
@TableName("WFD_DELEGATE_WORK")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdDelegateWorkDO implements Serializable {

    public WfdDelegateWorkDO() {

    }

    public WfdDelegateWorkDO(String appId, String userId, String delegateUserId, String delegateRealName, String taskId, String taskName, String nodeName, String processId) {
        this.appId = appId;
        this.userId = userId;
        this.delegateUserId = delegateUserId;
        this.delegateRealName = delegateRealName;
        this.taskId = taskId;
        this.taskName = taskName;
        this.nodeName = nodeName;
        this.processId = processId;
        this.createTime = LocalDateTime.now();

    }

    public WfdDelegateWorkDO(String appId, String userId, String delegateUserId, String delegateRealName, String taskId, String taskName, String nodeName, String processId
            , String flowNo, String wfdCategoryId, String wfdId) {
        this.appId = appId;
        this.userId = userId;
        this.delegateUserId = delegateUserId;
        this.delegateRealName = delegateRealName;
        this.taskId = taskId;
        this.taskName = taskName;
        this.nodeName = nodeName;
        this.processId = processId;
        this.flowNo = flowNo;
        this.wfdId = wfdId;
        this.wfdCategoryId = wfdCategoryId;
        this.createTime = LocalDateTime.now();

    }

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 委托人ID
     */
    private String userId;

    /**
     * 委托人
     */
    @TableField(exist = false)
    private SysUserDO user;

    /**
     * 被委托人ID
     */
    private String delegateUserId;

    /**
     * 被委托人姓名
     */
    private String delegateRealName;


    /**
     * 任务ID
     */
    private String taskId;

    /**
     * nodeName
     */
    private String nodeName;

    /**
     * NodeId
     */
    private String taskName;

    /**
     * 流程实例ID
     */
    private String processId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 工作摘要文号
     */
    private String flowNo;

    /**
     * 流程类别id
     */
    private String wfdCategoryId;

    /**
     * 流程定义Id
     */
    private String wfdId;

}
