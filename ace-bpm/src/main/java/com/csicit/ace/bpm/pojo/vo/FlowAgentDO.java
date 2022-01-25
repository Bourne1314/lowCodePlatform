package com.csicit.ace.bpm.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 工作代办规则 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowAgentDO {

    /**
     * 启用时间
     */
    private String beginTime;

    /**
     * 启用时间是否不限
     */
    private Integer beginTimeFlag;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 结束时间是否不限
     */
    private Integer endTimeFlag;

    /**
     * 是否启用 0不，1启用
     */
    private Integer enabled;

    /**
     * 前台页面选择的流程ID
     */
    private String flowId;

    /**
     * 前台页面选择的流程类型
     */
    private String flowType;

    /**
     * 代办用户ID
     */
    private String agentUserId;

    /**
     * 代办用户名称
     */
    private String agentUserName;

    /**
     * 应用ID
     */
    private String appId;

}
