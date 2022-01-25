package com.csicit.ace.bpmtest.listeners;

import com.csicit.ace.bpm.delegate.args.TaskDeleteEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/6 14:29
 */
public class TaskDeleteListener implements com.csicit.ace.bpm.delegate.TaskDeleteListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDeleteListener.class);

    @Override
    public void notify(TaskDeleteEventArgs args) {
        LOGGER.info("TaskDeleteListener notified");
    }
}
