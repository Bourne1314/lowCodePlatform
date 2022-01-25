package com.csicit.ace.bpmtest.listeners;

import com.csicit.ace.bpm.delegate.args.TaskAssignmentEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/6 14:29
 */
public class TaskAssignmentListener implements com.csicit.ace.bpm.delegate.TaskAssignmentListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAssignmentListener.class);

    @Override
    public void notify(TaskAssignmentEventArgs args) {
        LOGGER.info("TaskAssignmentListener notified");
    }
}
