package com.csicit.ace.bpmtest.listeners;

import com.csicit.ace.bpm.delegate.args.FlowCreatingEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JonnyJiang
 * @date 2019/11/6 14:29
 */
public class FlowCreatingListener implements com.csicit.ace.bpm.delegate.FlowCreatingListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowCreatingListener.class);

    @Override
    public void notify(FlowCreatingEventArgs args) {
        LOGGER.info("FlowCreatingListener notified");
    }
}
