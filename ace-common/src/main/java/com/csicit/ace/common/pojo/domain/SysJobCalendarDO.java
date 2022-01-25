package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 工作日表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 08:12:02
 */
@Data
@TableName("SYS_JOB_CALENDAR")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysJobCalendarDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 状态 (0为工作日，1为休息日)
     */
    private Integer state;
    /**
     * 业务单元ID
     */
    private String organizationId;
    /**
     * 年
     */
    private String year;
    /**
     * 月
     */
    private String month;
    /**
     * 日
     */
    private String day;

}
