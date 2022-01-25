package com.csicit.ace.bpm.activiti;

import org.activiti.engine.delegate.BaseTaskListener;

/**
 * 节点事件类型
 *
 * @author JonnyJiang
 * @date 2019/10/10 16:02
 */
public enum NodeEventType {
    /**
     * 开始
     */
    CREATE(0, BaseTaskListener.EVENTNAME_CREATE),
    /**
     * 分派
     */
    ASSIGNMENT(1, BaseTaskListener.EVENTNAME_ASSIGNMENT),
    /**
     * 结束
     */
    COMPLETE(2, BaseTaskListener.EVENTNAME_COMPLETE),
    /**
     * 删除
     */
    DELETE(3, BaseTaskListener.EVENTNAME_DELETE),
    /**
     * 所有事件
     */
    ALL_EVENT(4, BaseTaskListener.EVENTNAME_ALL_EVENTS);

    private Integer value;
    private String eventName;

    NodeEventType(Integer value, String eventName) {
        this.value = value;
        this.eventName = eventName;
    }

    public Boolean isEquals(Integer nodeEventType) {
        return this.value.equals(nodeEventType);
    }

    public String getEventName() {
        return eventName;
    }
}
