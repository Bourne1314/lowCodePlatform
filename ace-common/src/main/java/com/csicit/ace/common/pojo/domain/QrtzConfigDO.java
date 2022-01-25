package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 批处理任务配置 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-16 09:59:40
 */
@Data
@TableName("QRTZ_CONFIG")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QrtzConfigDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 任务配置
     */
    private String config;
    /**
     * 跟踪ID
     */
    private String traceId;
}
