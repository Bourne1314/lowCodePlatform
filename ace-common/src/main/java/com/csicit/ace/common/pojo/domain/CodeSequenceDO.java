package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数字序列
 *
 * @author shanwj
 * @date 2020/5/22 10:21
 */
@Data
@TableName("CODE_SEQUENCE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodeSequenceDO  implements Serializable {
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
     * 序列主要标识
     */
    private String  bizTag;
    /**
     * 序列次要标识
     */
    private String  partValueTag;
    /**
     * 已发放的最大编号
     */
    private Long maxNum;
    /**
     * 号段内已发放的编号值
     */
    @TableField(exist = false)
    private Long current;
    /**
     * 号段长度
     */
    private Integer step;
    /**
     * 数字序列描述
     */
    private String remark;
    /**
     * 序列重置模式
     * 0 不重置、 1 按年重置、2 按月重置、3 按天重置
     */
    private Integer resetMode;
    /**
     * 上次重置序列时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastResetTime;
    /**
     * 是否启用固定数字位数 0否1是
     */
    private Integer enableFixNumLen;
    /**
     * 固定位数
     */
    private Integer numLength;
    /**
     * 数据版本
     */
    @Version
    private Integer dataVersion;
    /**
     * 跟踪ID
     */
    private String traceId;
}
