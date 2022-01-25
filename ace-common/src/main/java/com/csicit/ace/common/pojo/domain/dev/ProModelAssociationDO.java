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
import java.util.List;

/**
 * 数据关联定义 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-06-03 14:47:57
 */
@Data
@TableName("PRO_MODEL_ASSOCIATION")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProModelAssociationDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 关联名
     */
    private String name;
    /**
     * 表Id
     */
    private String modelId;
    /**
     * 数据列名
     */
    private String colNames;
    /**
     * 引用外键列名
     */
    private String refColNames;
    /**
     * 引用表id
     */
    private String refModelId;
    /**
     * 参照表明
     */
    @TableField(exist = false)
    private String refModelName;
    /**
     * 删除动作
     */
    private String delAction;
    /**
     * 更新动作
     */
    private String updAction;
//    /**
//     * 是否同步创建索引 0否1是
//     */
//    private Integer createIdx;
//    /**
//     * 索引名
//     */
//    private String indexName;
//    /**
//     * 索引ID
//     */
//    private String indexId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 跟踪ID
     */
    private String traceId;
}
