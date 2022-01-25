package com.csicit.ace.quartz.core.vo;

import com.csicit.ace.quartz.core.utils.CronTriggerType;
import com.csicit.ace.quartz.core.utils.EntryType;
import com.csicit.ace.quartz.core.utils.JobDetailType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务列表详情
 *
 * @author zuogang
 * @date Created in 8:39 2019/8/6
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDetailVO {
    /**
     * 名称
     */
    private String name;

    /**
     * 任务/触发器名称
     */
    private String jobOrTriggerName;

    /**
     * 任务/触发器名称
     */
    private String jobOrTriggerGroup;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 类型 (列表行为任务组设为0,任务设为1,触发器设为2,参数设为3,所有任务设为4)
     */
    private Integer type;

    /**
     * 上次执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime previousFireTime;

    /**
     * 下次执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextFireTime;

    /**
     * 子任务列表
     */
    private List<JobDetailVO> children;

    /**
     * 任务
     */
    private JobDetailType jobDetailType;

    /**
     * 触发器
     */
    private CronTriggerType cronTriggerType;

    /**
     * 触发器
     */
    private EntryType entryType;

    /**
     * 图标
     */
    private String icon;

}
