package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 流程-转交信息
 *
 * @author JonnyJiang
 * @date 2019/10/23 19:38
 */
@Data
@TableName("WFI_DELIVER")
public class WfiDeliverDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 转交信息
     */
    private String deliverInfo;
    /**
     * 流程实例ID
     */
    private String flowId;
    /**
     * 转交人
     */
    private String userId;
    /**
     * 转交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliverTime;

    /**
     * 转交信息对象
     */
    @TableField(exist = false)
    private DeliverInfo deliverInfoClass;
}