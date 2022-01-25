package com.csicit.ace.bpmtest.listeners;

import com.csicit.ace.bpm.delegate.args.TaskCompletingEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/6 14:29
 */
public class TaskCompletingListener implements com.csicit.ace.bpm.delegate.TaskCompletingListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskCompletingListener.class);

    @Override
    public void notify(TaskCompletingEventArgs args) {
        LOGGER.info("TaskCompletingListener notified");
    }
}
