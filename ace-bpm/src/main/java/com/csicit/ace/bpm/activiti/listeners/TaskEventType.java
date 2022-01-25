package com.csicit.ace.bpm.activiti.listeners;

/**
 * 任务事件类型
 *
 * @author JonnyJiang
 * @date 2019/11/7 19:49
 */
public enum TaskEventType {
    /**
     * 所有
     */
    AllEvent(0),

    /**
     * 创建
     */
    Create(1),

    /**
     * 分配
     */
    Assignment(2),

    /**
     * 办结前
     */
    Complete(3),

    /**
     * 删除
     */
    Delete(4);


    private Integer value;

    TaskEventType(Integer value) {
        this.value = value;
    }

    public Boolean isEquals(Integer eventType) {
        return this.value.equals(eventType);
    }
}
