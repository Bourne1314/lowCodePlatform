package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 已关注工作
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2020/5/26 15:02
 */
@Data
@TableName("WFI_FOCUSED_WORK")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfiFocusedWorkDO implements Serializable {
    /**
     * 主键  taskId
     */
    private String id;

    /**
     * 应用名
     */
    private String appId;

    /**
     * 用户主键
     */
    private String userId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 最新一次浏览后是否有更新 0 否 1是
     */
    private Integer latestReadFlag;

    /**
     * 最新一次浏览时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latestReadTime;

    /**
     * 流程实例主键
     */
    private String flowInstanceId;

    /**
     * 发起人主键
     */
    private String starterId;

    /**
     * 发起时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 发起人姓名
     */
    @TableField(exist = false)
    private String starterRealName;

    /**
     * 流程状态
     */
    @TableField(exist = false)
    private String state;

    /**
     * 文号
     */
    private String workNo;

    /**
     * 流程定义主键
     */
    private String wfdId;

    /**
     * 流程定义类别主键
     */
    private String wfdCategoryId;

    /**
     * 当前办理人主键
     */
    @TableField(exist = false)
    private String currentActUserId;

    /**
     * 当前办理人姓名
     */
    @TableField(exist = false)
    private String currentActUserRealName;

    /**
     * 当前办理节点名称
     */
    @TableField(exist = false)
    private String currentActName;
}
