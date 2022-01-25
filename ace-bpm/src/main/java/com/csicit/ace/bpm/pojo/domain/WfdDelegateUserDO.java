package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 被委托人
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2020/2/19 15:16
 */
@Data
@TableName("WFD_DELEGATE_USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdDelegateUserDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 委托规则主键
     */
    private String ruleId;

    /**
     * 被委托人主键
     */
    private String userId;

    /**
     * 被委托人姓名
     */
    private String realName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
