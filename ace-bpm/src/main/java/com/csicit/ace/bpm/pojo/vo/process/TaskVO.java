package com.csicit.ace.bpm.pojo.vo.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 办理过程-任务信息
 *
 * @author JonnyJiang
 * @date 2019/11/27 11:01
 */
@Data
public class TaskVO implements Serializable {
    /**
     * 任务id
     */
    private String id;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date claimTime;
    /**
     * 办结时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 主办人姓名
     */
    private String hostRealName;

    /**
     * 主办人主键
     */
    private String hostUserId;

    /**
     * 拥有人姓名
     */
    private String ownerRealName;

    /**
     * 拥有人主键
     */
    private String ownerId;

    /**
     * 被委托人主键
     */
    private String delegatedUserId;

    /**
     * 被委托人姓名
     */
    private String delegatedRealName;
    /**
     * 转交信息
     */
    private DeliverVO deliver;

    /**
     * 转交节点信息
     */
    private String deliverNodeNames;

    /**
     * 办理状态
     */
    private String state;
}
