package com.csicit.ace.bpm.enums;

/**
 * 流转条件定义方式（流转条件）
 *
 * @author JonnyJiang
 * @date 2019/7/10 20:02
 */
public enum ConditionMode {
    /**
     * 无条件
     */
    None(0),
    /**
     * 按路径起点步骤办理结果路由
     */
    WorkResult(1),
    /**
     * 按自定义规则路由
     */
    Rule(2);

    private Integer value;

    ConditionMode(Integer value) {
        this.value = value;
    }

    public Boolean isEquals(Integer conditionMode) {
        return this.value.equals(conditionMode);
    }

    public Integer getValue() {
        return value;
    }
}
