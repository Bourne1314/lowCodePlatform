package com.csicit.ace.bpmtest.listeners;

import com.csicit.ace.bpm.delegate.args.FlowEndEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/6 14:29
 */
public class FlowEndedListener implements com.csicit.ace.bpm.delegate.FlowEndedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.csicit.ace.bpmtest.listeners.FlowEndedListener.class);

    @Override
    public void notify(FlowEndEventArgs args) {
        LOGGER.info("FlowEndedListener notified");
    }
}
