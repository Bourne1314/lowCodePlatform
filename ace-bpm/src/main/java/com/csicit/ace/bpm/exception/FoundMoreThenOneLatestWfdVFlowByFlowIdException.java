package com.csicit.ace.bpm.exception;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.List;
import java.util.Map;

public class FoundMoreThenOneLatestWfdVFlowByFlowIdException extends BpmSystemException {
    private final String flowId;
    private final List<WfdVFlowDO> wfdVFlows;

    public FoundMoreThenOneLatestWfdVFlowByFlowIdException(String flowId, List<WfdVFlowDO> wfdVFlows) {
        super(BpmErrorCode.S00090, LocaleUtils.getFoundMoreThenOneLatestWfdVFlow());
        this.flowId = flowId;
        this.wfdVFlows = wfdVFlows;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
        args.put("wfdFlows", JSONObject.toJSON(wfdVFlows));
    }
}