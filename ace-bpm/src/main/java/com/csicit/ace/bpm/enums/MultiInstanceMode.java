package com.csicit.ace.bpm.enums;

/**
 * 多实例并行模式
 *
 * @author JonnyJiang
 * @date 2020/6/5 15:19
 */
public enum MultiInstanceMode {
    /**
     * 单一实例办理
     */
    Single(0),
    /**
     * 按部门划分启动多个实例并行
     */
    Department(1),
    /**
     * 按业务单元划分启动多个实例并行
     */
    Organization(2),
    /**
     * 按集团划分启动多个实例并行
     */
    Group(3);

    private Integer value;

    MultiInstanceMode(Integer value) {
        this.value = value;
    }

    public boolean isEquals(Integer multiInstanceMode) {
        return value.equals(multiInstanceMode);
    }
}
