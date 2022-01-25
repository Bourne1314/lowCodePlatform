package com.csicit.ace.bpm.activiti.listeners;

/**
 * @author JonnyJiang
 * @date 2019/11/7 19:49
 */
public enum ProcessEventType {
    /**
     * 开始
     */
    Start(0),

    /**
     * 结束
     */
    End(1);

    private Integer value;

    ProcessEventType(Integer value) {
        this.value = value;
    }

    public Boolean isEquals(Integer eventType) {
        return this.value.equals(eventType);
    }
}
