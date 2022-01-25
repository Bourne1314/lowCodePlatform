package com.csicit.ace.bpm.pojo.vo.v7v1v81.st;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author JonnyJiang
 * @date 2020/11/26 9:56
 */
@Data
@TableName("ACT_HI_COMMENT")
public class ActHiDetailDO {
    @TableField("ID_")
    private String id;

    @TableField("TYPE_")
    private String type;

    @TableField("PROC_INST_ID_")
    private String procInstId;

    @TableField("EXECUTION_ID_")
    private String executionId;

    @TableField("TASK_ID_")
    private String taskId;

    @TableField("ACT_INST_ID_")
    private String actInstId;

    @TableField("NAME_")
    private String name;

    @TableField("VAR_TYPE_")
    private String varType;

    @TableField("REV_")
    private Integer rev;

    @TableField("TIME_")
    private LocalDateTime time;

    @TableField("BYTEARRAY_ID_")
    private String bytearrayId;

    @TableField("DOUBLE_")
    private Double doubleValue;

    @TableField("LONG_")
    private Long longValue;

    @TableField("TEXT_")
    private String text;

    @TableField("TEXT2_")
    private String text2;
}