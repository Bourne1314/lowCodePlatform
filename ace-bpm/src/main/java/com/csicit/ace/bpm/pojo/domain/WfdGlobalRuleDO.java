package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 全局规则 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 11:46:06
 */
@Data
@TableName("WFD_GLOBAL_RULE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdGlobalRuleDO implements Serializable {
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
     * 规则名称
     */
    private String name;
    /**
     * 规则描述
     */
    private String description;
    /**
     * 表达式
     */
    private String expression;


}
