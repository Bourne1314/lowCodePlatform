package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JonnyJiang
 * @date 2020/6/28 14:34
 */
@Data
@TableName("WFI_TASK_PENDING")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfiTaskPendingDO implements Serializable {
    /**
     * id
     */
    @Id
    @TableId(type = IdType.UUID)
    public String id;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 流程实例id
     */
    private String flowId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 用户身份
     */
    private Integer userType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 办理人id
     */
    private String userId;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 流程标识
     */
    private String flowCode;
}
