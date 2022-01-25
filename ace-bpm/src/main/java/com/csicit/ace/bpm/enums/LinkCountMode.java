package com.csicit.ace.bpm.enums;

/**
 * 分支选择票数计算方式
 *
 * @author JonnyJiang
 * @date 2020/5/28 17:12
 */
public enum LinkCountMode {
    /**
     * 按比例
     */
    Proportion(0),
    /**
     * 按数量
     */
    Quantity(1);

    private Integer value;

    LinkCountMode(Integer value) {
        this.value = value;
    }

    public Boolean isEquals(Integer linkCountMode) {
        return this.value.equals(linkCountMode);
    }
}
