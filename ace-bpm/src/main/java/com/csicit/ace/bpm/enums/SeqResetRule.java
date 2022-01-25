package com.csicit.ace.bpm.enums;

/**
 * 流水重置规则
 *
 * @author JonnyJiang
 * @date 2019/10/11 15:15
 */
public enum SeqResetRule {
    /**
     * 不重置
     */
    NEVER(0),
    /**
     * 按年重置
     */
    YEAR(1),
    /**
     * 按月重置
     */
    MONTH(2),
    /**
     * 按天重置
     */
    DAY(3);

    private Integer value;

    SeqResetRule(Integer value) {
        this.value = value;
    }

    public Boolean isEquals(Integer seqResetRule) {
        return this.value.equals(seqResetRule);
    }
}
