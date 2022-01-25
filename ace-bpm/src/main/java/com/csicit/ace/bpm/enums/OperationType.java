package com.csicit.ace.bpm.enums;

/**
 * 操作类型
 *
 * @author JonnyJiang
 * @date 2019/10/14 18:23
 */
public enum OperationType {
    /**
     * 不进行任何操作
     */
    None(-1),
    /**
     * 转交
     */
    Deliver(0),
    /**
     * 办结
     */
    Complete(1),
    /**
     * 驳回
     */
    Reject(2),
    /**
     * 驳回操作后被停止
     */
    StopAfterReject(3);

    private Integer value;

    OperationType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean isEquals(Integer operationType) {
        return this.value.equals(operationType);
    }
}
