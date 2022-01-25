package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 索引信息 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-06-03 14:49:12
 */
@Data
@TableName("PRO_MODEL_INDEX")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProModelIndexDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 数据表
     */
    private String modelId;
    /**
     * 名称
     */
    private String name;
    /**
     * 包含列
     */
    private String cols;
    /**
     * 是否唯一
     */
    @TableField("IS_UNIQUE")
    private Integer onlyOne;
    /**
     * 模型名称
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 跟踪ID
     */
    private String traceId;
//    /**
//     * 是否是外键添加的索引，0否1是
//     */
//    private Integer assAction;
//        /**
//         * 表名
//         */
//        @TableField(exist = false)
//        private String tableName;
}
