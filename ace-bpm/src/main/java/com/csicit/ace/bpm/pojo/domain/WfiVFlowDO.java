package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * 流程实例版本
 *
 * @author JonnyJiang
 * @date 2020/8/11 15:57
 */
@Data
@TableName("WFI_V_FLOW")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfiVFlowDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 流程实例ID
     */
    private String flowId;
    /**
     * 流程模型
     */
    private String model;
    /**
     * BPMN模型
     */
    private String bpmn;
    /**
     * 流程实例版本
     */
    private Integer flowVersion;
    /**
     * 流程实例版本失效时间
     */
    private LocalDateTime versionEndDate;
}