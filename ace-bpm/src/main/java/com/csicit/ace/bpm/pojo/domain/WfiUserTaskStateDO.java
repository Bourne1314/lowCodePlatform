package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 用户任务办理状态
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/10 8:40
 */
@Data
@TableName("WFI_USER_TASK_STATE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfiUserTaskStateDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户主键
     */
    private String userId;


    /**
     * 任务主键
     */
    private String taskId;


    /**
     * 办理状态
     */
    private String state;

    /**
     * 流程实例id
     */
    private String flowId;
}