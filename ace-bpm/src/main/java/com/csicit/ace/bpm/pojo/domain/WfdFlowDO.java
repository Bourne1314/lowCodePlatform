package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程定义 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */
@Data
@TableName("WFD_FLOW")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdFlowDO implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 流程名称
     */
    private String name;
    /**
     * 流程标识
     */
    private String code;
    /**
     * 流程序号
     */
    private Integer sortNo;
    /**
     * 流程类别
     */
    private String categoryId;
    /**
     * 描述信息
     */
    private String description;
    /**
     * 监控权限
     */
    private String adminAuthId;
    /**
     * 查询权限
     */
    private String queryAuthId;
    /**
     * 发布后已经修改过
     */
    private Integer hasModified;
    /**
     * 修订版本号
     */
    private Integer reviseVersion;

    /**
     * 流程模型
     */
    private String model;

    /**
     * 数据表名
     */
    private String formDataTable;

    /**
     * 数据源id
     */
    private String formDataSourceId;

    /**
     * 是否在编辑
     */
    @TableField(value = "IS_EDITING")
    private Integer editing;

    /**
     * 编辑用户
     */
    private String editingUser;

    /**
     * 允许催办
     */
    private Integer allowUrgeTask;

    /**
     * 一般工作办理时限
     */
    private Integer timeLimitGeneral;
    /**
     * 一般工作办理时限单位
     */
    private String timeLimitUnitG;
    /**
     * 加急工作办理时限
     */
    private Integer timeLimitUrgent;
    /**
     * 加急工作办理时限单位
     */
    private String timeLimitUnitU;
    /**
     * 特急工作办理时限
     */
    private Integer timeLimitExtraUrgent;
    /**
     * 特急工作办理时限单位
     */
    private String timeLimitUnitEu;
    /**
     * 超时处理方式
     */
    private String overTimeMode;
    /**
     * 催办次数
     */
    private Integer overTimeRemindTime;
    /**
     * 催办间隔
     */
    private Integer overTimeRemindIntv;
    /**
     * 催办信息模板
     */
    private String overTimeMsgTemplateCode;
    /**
     * 流入消息模板
     */
    private String flowInMsgTemplate;
    /**
     * 催办处理频道
     */
    private String overTimeMsgType;

    /**
     * 超时提醒消息模板变量
     */
    private List<String> overTimeVariableField;

    /**
     * 最后编辑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastEditTime;

    /**
     * 发起权限
     */
    private String initAuthId;
    /**
     * 流水号
     */
    private Integer seqNo;
    /**
     * 最新创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latestCreateTime;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 是否可编辑
     */
    private Integer editable;
}
