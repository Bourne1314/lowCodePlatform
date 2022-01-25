package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JonnyJiang
 * @date 2019/9/5 17:54
 */
@Data
@TableName("WFI_FLOW")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfiFlowDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 流程实例id
     */
    @TableId
    private String id;

    /**
     * 流程模型
     */
    private String model;

    /**
     * 流程发布id
     */
    private String vFlowId;

    /**
     * 业务标识
     */
    private String businessKey;

    /**
     * 工作文号
     */
    private String flowNo;
    /**
     * 流程id
     */
    private String flowId;
    /**
     * 流程标识
     */
    private String flowCode;
    /**
     * 应用标识
     */
    private String appId;
}
