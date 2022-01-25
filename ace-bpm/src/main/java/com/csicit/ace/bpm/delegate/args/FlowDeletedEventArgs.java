package com.csicit.ace.bpm.delegate.args;

import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;

/**
 * @author JonnyJiang
 * @date 2019/11/7 20:08
 */
public class FlowDeletedEventArgs {
    private final Flow flow;
    private final WfiFlowDO wfiFlow;
    private final String businessKey;
    private final String deleteReason;

    public FlowDeletedEventArgs(Flow flow, WfiFlowDO wfiFlow, String businessKey, String deleteReason) {

        this.flow = flow;
        this.wfiFlow = wfiFlow;
        this.businessKey = businessKey;
        this.deleteReason = deleteReason;
    }

    public Flow getFlow() {
        return flow;
    }

    public WfiFlowDO getWfiFlow() {
        return wfiFlow;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getDeleteReason() {
        return deleteReason;
    }
}
