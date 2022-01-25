package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @author JonnyJiang
 * @date 2020/8/28 16:56
 */
@Data
@TableName("WFI_ROUTE_PRESET")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfiRoutePresetDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 流程实例ID
     */
    private String flowId;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 预设时间
     */
    private LocalDateTime presetTime;
    /**
     * 预设信息
     */
    private String presetInfo;
}