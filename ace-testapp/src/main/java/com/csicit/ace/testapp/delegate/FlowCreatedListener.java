package com.csicit.ace.testapp.delegate;

import com.csicit.ace.bpm.delegate.args.FlowCreatedEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author JonnyJiang
 * @date 2021/10/8 18:52
 */
@Component
public class FlowCreatedListener implements com.csicit.ace.bpm.delegate.FlowCreatedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowCreatedListener.class);
    @Override
    public void notify(FlowCreatedEventArgs args) {
        LOGGER.info("------------------------------->FlowCreatedListener notify");
    }
}
