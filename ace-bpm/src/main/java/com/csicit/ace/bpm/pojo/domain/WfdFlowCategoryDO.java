package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 流程类别 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */
@Data
@TableName("WFD_FLOW_CATEGORY")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdFlowCategoryDO implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 类别名称
     */
    private String name;
    /**
     * 序号
     */
    private Integer sortNo;
    /**
     * 应用ID
     */
    private String appId;

}
