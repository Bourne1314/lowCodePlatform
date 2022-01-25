package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 流程定义 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 11:43:23
 */
@Data
@TableName("WFD_V_FLOW")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdVFlowDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 版本
     */
    private Integer flowVersion;
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
     * 流程id
     */
    private String flowId;

    /**
     * 是否最新
     */
    @TableField("IS_LATEST")
    private Integer latest;

    /**
     * 流程模型
     */
    private String model;
    /**
     * BPMN
     */
    private String bpmn;
    /**
     * 数据表名
     */
    private String formDataTable;

    /**
     * 数据源id
     */
    private String formDataSourceId;

    /**
     * 发起权限
     */
    private String initAuthId;

    /**
     * 版本生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime versionBeginDate;

    /**
     * 版本失效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime versionEndDate;

    /**
     * 是否已使用
     */
    @TableField("IS_USED")
    private Integer used;
    /**
     * 应用标识
     */
    private String appId;
    /**
     * 流程引擎中的流程定义id
     */
    private String processDefinitionId;
}
