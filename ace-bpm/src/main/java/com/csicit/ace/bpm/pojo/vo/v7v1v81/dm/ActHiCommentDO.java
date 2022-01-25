package com.csicit.ace.bpm.pojo.vo.v7v1v81.dm;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author JonnyJiang
 * @date 2020/2/11 10:26
 */
@Data
@TableName("ACT_HI_COMMENT")
public class ActHiCommentDO {
    @TableField("ID_")
    private String id;
    @TableField("TYPE_")
    private String type;
    @TableField("TIME_")
    private Date time;
    @TableField("USER_ID_")
    private String userId;
    @TableField("TASK_ID_")
    private String taskId;
    @TableField("PROC_INST_ID_")
    private String procInstId;
    @TableField("ACTION_")
    private String action;
    @TableField("MESSAGE_")
    private String message;
    @TableField("FULL_MSG_")
    private String fullMsg;
}