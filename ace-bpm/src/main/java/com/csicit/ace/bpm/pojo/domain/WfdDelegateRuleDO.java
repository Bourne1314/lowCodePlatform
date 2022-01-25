package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 委托规则
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2020/2/19 14:59
 */
@Data
@TableName("WFD_DELEGATE_RULE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdDelegateRuleDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 委托人ID
     */
    private String delegateUserId;

    /**
     * 工作名称
     */
    private String processName;

    /**
     * 工作主键
     */
    private String processId;

    /**
     * 工作类型
     */
    @TableField(exist = false)
    private String processType;

    /**
     * 是否启用
     */
    private Integer usedFlag;
    /**
     * 是否启用字符串
     */
    @TableField(exist = false)
    private String usedFlagStr;

    /**
     * 有效日期起始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 有效日期截止
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 被委托人
     */
    @TableField(exist = false)
    private List<WfdDelegateUserDO> delegatedUsers;

    /**
     * 被委托人姓名
     */
    @TableField(exist = false)
    private String delegatedUserNames;

    /**
     * 委托人姓名
     */
    @TableField(exist = false)
    private String userName;
}
