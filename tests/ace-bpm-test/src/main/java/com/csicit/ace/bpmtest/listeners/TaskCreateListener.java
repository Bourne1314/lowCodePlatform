package com.csicit.ace.bpmtest.listeners;

import com.csicit.ace.bpm.delegate.args.TaskCreateEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/6 14:29
 */
public class TaskCreateListener implements com.csicit.ace.bpm.delegate.TaskCreateListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskCreateListener.class);

    @Override
    public void notify(TaskCreateEventArgs args) {
        LOGGER.info("TaskCreateListener notified");
    }
}
