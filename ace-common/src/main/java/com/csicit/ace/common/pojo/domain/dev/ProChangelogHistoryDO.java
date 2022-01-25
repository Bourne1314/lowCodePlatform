package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.Id;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 * changlog历史信息表
 *
 * @author shanwj
 * @date 2019/11/25 11:05
 */
@Data
@TableName("PRO_CHANGELOG_HISTORY")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProChangelogHistoryDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * changeset对象
     */
    private String logValue;
    /**
     * 是否发布过，0否1是
     */
    private Integer publishTag;
    /**
     * 发布版本
     */
    private Integer publishVersion;
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 数据源ID
     */
    private String dsId;
    /**
     * 替换主数据源后，变为无用数据，0无效，1有效
     */
    @TableField("IS_USE_LESS")
    private Integer useLess;
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
