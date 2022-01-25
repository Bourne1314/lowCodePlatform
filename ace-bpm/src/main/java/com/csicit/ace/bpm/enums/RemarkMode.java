package com.csicit.ace.bpm.enums;

/**
 * 签署模式
 *
 * @author JonnyJiang
 * @date 2019/9/25 9:58
 */
public enum RemarkMode {
    /**
     * 允许签署（可签可不签）
     */
    NoRestriction(0),
    /**
     * 必须签署
     */
    Required(1),
    /**
     * 禁止签署
     */
    Forbidden(2);

    private Integer value;

    RemarkMode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public Boolean isEquals(Integer remarkMode) {
        return this.value.equals(remarkMode);
    }
}
