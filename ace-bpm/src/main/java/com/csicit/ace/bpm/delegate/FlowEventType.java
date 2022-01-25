package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * 流程事件类型
 *
 * @author JonnyJiang
 * @date 2019/11/6 10:43
 */
public enum FlowEventType {
    /**
     * 创建前
     */
    Creating(0),
    /**
     * 结束
     */
    Ended(1),
    /**
     * 删除前
     */
    Deleting(2),
    /**
     * 删除后
     */
    Deleted(3),
    /**
     * 创建后
     */
    Created(4);

    private Integer value;

    FlowEventType(Integer value) {
        this.value = value;
    }

    public Boolean isEquals(Integer eventType) {
        return this.value.equals(eventType);
    }

    public static FlowEventType getFlowEventType(Integer eventType) {
        if (Creating.isEquals(eventType)) {
            return Creating;
        } else if (Ended.isEquals(eventType)) {
            return Ended;
        } else if (Deleting.isEquals(eventType)) {
            return Deleting;
        } else if (Deleted.isEquals(eventType)) {
            return Deleted;
        } else if (Created.isEquals(eventType)) {
            return Created;
        } else {
            throw new BpmException(LocaleUtils.getUnsupportedFlowEventType(eventType));
        }
    }
}