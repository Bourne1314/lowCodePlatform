package com.csicit.ace.bpm.enums;

/**
 * @author JonnyJiang
 * @date 2019/7/10 20:02
 */
public enum AllowPassMode {
    /**
     * 没有限制
     */
    None(0),
    /**
     * 当所有人已签署
     */
    AllSign(1),
    /**
     * 当指定人员已全部签署
     */
    SpecifiedUserAllSign(2),
    /**
     * 当多于指定人数已签署
     */
    ExceedCount(3),
    /**
     * 当已签署人数所占百分比大于指定值
     */
    ExceedPercent(4);

    private Integer value;

    AllowPassMode(Integer value) {
        this.value = value;
    }

    public Boolean isEquals(Integer allowPassMode) {
        return this.value.equals(allowPassMode);
    }
}
