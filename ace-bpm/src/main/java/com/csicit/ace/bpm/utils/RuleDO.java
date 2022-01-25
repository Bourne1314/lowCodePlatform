package com.csicit.ace.bpm.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/26 9:27
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuleDO {
    /**
     * 类型
     */
    private String type;

    /**
     * 类型
     */
    private String operator;

    /**
     * 被连接的规则部件
     */
    private List<RuleDO> parts;

    /**
     * 逻辑表达式中第一个操作数
     */
    private String var1;

    /**
     * 逻辑表达式中第二个操作数
     */
    private String var2;

    /**
     * 如果第二个运算数是常量，设置value
     */
    private String value;

    /**
     * 子规则id
     */
    private String ruleId;

    /**
     * 是否满足
     */
    private boolean satisfy;

    /**
     * 常量还是变量
     */
    private String operandClass;
}
