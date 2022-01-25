package com.csicit.ace.bpm.enums;

/**
 * @author JonnyJiang
 * @date 2020/6/12 14:20
 */
public enum NodeRejectTo {
    First(0),
    Last(1),
    Specific(2);

    private Integer value;

    NodeRejectTo(Integer value) {
        this.value = value;
    }

    public boolean isEquals(Integer rejectTo) {
        return this.value.equals(rejectTo);
    }
}
