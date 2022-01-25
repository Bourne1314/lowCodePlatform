package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.delegate.TaskEventType;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2021/10/8 22:05
 */
public class TaskListenerNotFoundException extends BpmSystemException {
    /**
     * 任务事件类型
     */
    private final TaskEventType taskEventType;
    /**
     * 任务事件实现类名称
     */
    private final String className;

    public TaskListenerNotFoundException(TaskEventType taskEventType, String className) {
        super(BpmErrorCode.S00105, LocaleUtils.getTaskListenerNotFound());
        this.taskEventType = taskEventType;
        this.className = className;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskEventType", taskEventType);
        args.put("className", className);
    }
}
