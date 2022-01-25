package com.csicit.ace.bpm.enums;

/**
 * 流出模式
 *
 * @author JonnyJiang
 * @date 2019/7/10 20:02
 */
public enum FlowOutMode {
    /**
     * 所有符合路由条件的分支并发流出
     */
    Mode0(0),
    /**
     * 选择首条符合路由条件分支单独流出（按分支序号排列）
     */
    Mode1(1),
    /**
     * 手动选择流出分支
     */
    Mode2(2);

    private Integer value;

    FlowOutMode(Integer value) {
        this.value = value;
    }

    public Boolean isEquals(Integer flowOutMode) {
        return this.value.equals(flowOutMode);
    }

    public Integer getValue() {
        return value;
    }
}