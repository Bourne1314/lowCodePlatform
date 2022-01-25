package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * 任务事件类型
 *
 * @author JonnyJiang
 * @date 2019/11/6 10:44
 */
public enum TaskEventType {
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
    Completing(3),
    /**
     * 删除
     */
    Delete(4),
    /**
     * 办结后
     */
    Completed(5),
    /**
     * 驳回前
     */
    Rejecting(6),
    /**
     * 驳回后
     */
    Rejected(7),
    /**
     * 撤回前
     */
    Withdrawing(8),
    /**
     * 撤回后
     */
    Withdrawn(9);

    private Integer value;

    TaskEventType(Integer value) {
        this.value = value;
    }

    public static TaskEventType getTaskEventType(Integer taskEventType) {
        if (Create.isEquals(taskEventType)) {
            return Create;
        } else if (Assignment.isEquals(taskEventType)) {
            return Assignment;
        } else if (Completing.isEquals(taskEventType)) {
            return Completing;
        } else if (Delete.isEquals(taskEventType)) {
            return Delete;
        } else if (Completed.isEquals(taskEventType)) {
            return Completed;
        } else if (Rejecting.isEquals(taskEventType)) {
            return Rejecting;
        } else if (Rejected.isEquals(taskEventType)) {
            return Rejected;
        } else if (Withdrawing.isEquals(taskEventType)) {
            return Withdrawing;
        } else if (Withdrawn.isEquals(taskEventType)) {
            return Withdrawn;
        } else {
            throw new BpmException(LocaleUtils.getUnsupportedTaskEventType(taskEventType));
        }
    }

    public Boolean isEquals(Integer eventType) {
        return this.value.equals(eventType);
    }
}