package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 编码模板
 *
 * @author shanwj
 * @date 2020/5/22 10:22
 */
@Data
@TableName("CODE_TEMPLATE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodeTemplateDO implements Serializable {

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
     * 模板标识
     */
    private String templateKey;
    /**
     * 模板描述
     */
    private String remark;
    /**
     * 数据版本
     */
    private Integer dataVersion;
    /**
     * 跟踪ID
     */
    private String traceId;
}
