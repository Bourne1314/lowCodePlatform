package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

/**
 * 系统配置项 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:53
 */
@Data
@TableName("SYS_CONFIG")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysConfigDO extends AbstractBaseRecordDomain {

    /**
     * 属性名
     */
    private String name;
    /**
     * 属性值
     */
    private String value;
    /**
     * 排序值
     */
    private Integer sortIndex;
    /**
     * 集团应用标识
     */
    private String appId;
    /**
     * 集团标识
     */
    private String groupId;
    /**
     * 配置范围 1 租户 2集团 3应用
     */
    private Integer scope;
    /**
     * 值类型
     */
    private String type;
//
//    /**
//     * 长型值
//     */
//    private String longValue;

    /**
     * 配置项更新策略 0立即更新 1重启更新
     */
    private Integer updateType;

    /**
     * 配置范围的数组
     */
    @TableField(exist = false)
    private Set<Integer> scopes;

    /**
     * 特殊标识
     */
    @TableField(exist = false)
    private String sign;
    /**
     * 跟踪ID
     */
    private String traceId;
}
