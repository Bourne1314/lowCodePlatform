package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 历史流程实例
 *
 * @author JonnyJiang
 * @date 2019/9/9 20:04
 */
@Data
@TableName("WFD_FLOW_INST_HI")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdFlowInstHiDO implements Serializable {
    /**
     * 流程实例id
     */
    @Id
    private String id;
    /**
     * 流程实例id
     */
    private String flowInstId;
    /**
     * 流程模型
     */
    private String model;
    /**
     * 版本
     */
    private String hisVersion;
    /**
     * 版本产生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hisTime;
    /**
     * 版本描述
     */
    private String hisDescription;
    /**
     * 版本产生的任务id
     */
    private String hisTaskId;
    /**
     * 流程引擎模型
     */
    private String engineModel;
}
