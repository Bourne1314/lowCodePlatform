package com.csicit.ace.bpmtest.listeners;

import com.csicit.ace.bpm.delegate.args.FlowDeletedEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/6 14:29
 */
public class FlowDeletedListener implements com.csicit.ace.bpm.delegate.FlowDeletedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowDeletedListener.class);

    @Override
    public void notify(FlowDeletedEventArgs args) {
        LOGGER.info("FlowDeletedListener notified");
    }
}
