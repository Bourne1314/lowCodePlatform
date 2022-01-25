package com.csicit.ace.bpmtest.listeners;

import com.csicit.ace.bpm.delegate.args.TaskCompletedEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/6 14:29
 */
public class TaskCompletedListener implements com.csicit.ace.bpm.delegate.TaskCompletedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskCompletedListener.class);

    @Override
    public void notify(TaskCompletedEventArgs args) {
        LOGGER.info("TaskCompletedListener notified");
    }
}
