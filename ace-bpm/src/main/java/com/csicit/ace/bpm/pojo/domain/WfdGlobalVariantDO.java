package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 全局变量 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 11:46:07
 */
@Data
@TableName("WFD_GLOBAL_VARIANT")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdGlobalVariantDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 变量名
     */
    private String name;
    /**
     * 变量标题
     */
    private String caption;
    /**
     * 类型
     */
    private String dataType;
    /**
     * 取值表达式
     */
    private String valueExpression;
    /**
     * 初始值
     */
    private String defaultValue;
    /**
     * 所属流程
     */
    private String appId;

}
