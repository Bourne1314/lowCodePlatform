package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.bpm.pojo.vo.NodeVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author JonnyJiang
 * @date 2020/5/25 19:05
 */
@Data
@TableName("WFI_COMMENT")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfiCommentDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 评论人id
     */
    private String commentUserId;
    /**
     * 评论人
     */
    private String commentUser;
    /**
     * 回复评论id
     */
    private String replyCommentId;
    /**
     * 流程实例id
     */
    private String flowId;
    /**
     * 评论时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 评论内容
     */
    private String commentText;
    /**
     * 应用标识
     */
    private String appId;
    /**
     * 被回复人ID
     */
    private String replyUserId;
    /**
     * 被回复人
     */
    private String replyUser;
    /**
     * 评论人身份
     */
    private Integer userType;
    /**
     * 回复评论
     */
    @TableField(exist = false)
    private WfiCommentDO replyComment;
    /**
     * 节点信息
     */
    @TableField(exist = false)
    private NodeVo node;
}
