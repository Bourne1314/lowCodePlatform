package com.csicit.ace.bpmtest.listeners;

import com.csicit.ace.bpm.delegate.args.FlowDeletedEventArgs;
import com.csicit.ace.bpm.delegate.args.FlowDeletingEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/6 14:29
 */
public class FlowDeletingListener implements com.csicit.ace.bpm.delegate.FlowDeletingListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowDeletingListener.class);

    @Override
    public void notify(FlowDeletingEventArgs args) {
        LOGGER.info("FlowDeletingListener notified");
    }
}
