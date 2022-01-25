package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.delegate.FlowEventType;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2021/10/8 21:50
 */
public class FlowListenerNotFoundException extends BpmSystemException {
    /**
     * 流程事件类型
     */
    private FlowEventType flowEventType;
    /**
     * 流程事件实现类名称
     */
    private String className;

    public FlowListenerNotFoundException(FlowEventType flowEventType, String className) {
        super(BpmErrorCode.S00104, LocaleUtils.getFlowListenerNotFound());
        this.flowEventType = flowEventType;
        this.className = className;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowEventType", flowEventType);
        args.put("className", className);
    }
}