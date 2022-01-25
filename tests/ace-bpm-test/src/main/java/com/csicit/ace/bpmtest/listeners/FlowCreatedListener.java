package com.csicit.ace.bpmtest.listeners;

import com.csicit.ace.bpm.delegate.args.FlowCreatedEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/8 8:45
 */
public class FlowCreatedListener implements com.csicit.ace.bpm.delegate.FlowCreatedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowCreatedListener.class);

    @Override
    public void notify(FlowCreatedEventArgs args) {
        LOGGER.info("FlowCreatedListener notified");
    }
}
