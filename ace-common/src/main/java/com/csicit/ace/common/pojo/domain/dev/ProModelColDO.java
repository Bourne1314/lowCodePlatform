package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据列 实例对象类
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-04 14:49:22
 */
@Data
@TableName("PRO_MODEL_COL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProModelColDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 模型名
     */
    private String modelId;
    /**
     * 表列名
     */
    private String tabColName;
    /**
     * 对象列名
     */
    private String objColName;
    /**
     * 列标题
     */
    private String caption;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 列精度
     */
    private Integer dataPrecision;
    /**
     * 列标度
     */
    private Integer dataScale;
    /**
     * 能否为空
     */
    private Integer nullable;
    /**
     * 缺省值
     */
    private String defaultValue;
    /**
     * 是否系统字段
     */
    @TableField("IS_SYSCOL")
    private Integer syscol;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 是否是主键，0否,1是
     */
    private Integer pkFlg;
    /**
     * 跟踪ID
     */
    private String traceId;

}
