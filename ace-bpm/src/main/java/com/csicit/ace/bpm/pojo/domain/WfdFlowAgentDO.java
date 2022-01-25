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

/**
 * 工作代办规则 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */
@Data
@TableName("WFD_FLOW_AGENT")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdFlowAgentDO implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 启用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 是否启用 0不，1启用
     */
    private Integer enabled;

    /**
     * 流程ID
     */
    private String flowId;

    /**
     * 流程名称
     */
    @TableField(exist = false)
    private String flowName;

    /**
     * 原用户ID
     */
    private String originUserId;

    /**
     * 代办用户ID
     */
    private String agentUserId;

    /**
     * 代办用户名称
     */
    private String agentUserName;

}
