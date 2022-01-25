package com.csicit.ace.bpm.enums;

/**
 * 流入模式
 *
 * @author JonnyJiang
 * @date 2019/7/10 20:02
 */
public enum FlowInMode {
    /**
     * 等待所有流入分支路径抵达激活步骤实例
     */
    All(0),
    /**
     * 有任一分支抵达就激活次步骤实例
     */
    Any(1),
    /**
     * 每一分支抵达都重新激活此步骤的新实例
     */
    Every(2);

    private Integer value;

    FlowInMode(Integer value) {
        this.value = value;
    }

    public Boolean isEquals(Integer flowInMode) {
        return this.value.equals(flowInMode);
    }

    public Integer getValue() {
        return value;
    }
}
