package com.csicit.ace.bpm.exception;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.utils.JsonUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class FoundMoreThenOneEffectiveWfdVFlowByFlowIdException extends BpmSystemException {
    private final String flowId;
    private final LocalDateTime date;
    private final List<WfdVFlowDO> wfdVFlows;

    public FoundMoreThenOneEffectiveWfdVFlowByFlowIdException(String flowId, LocalDateTime date, List<WfdVFlowDO> wfdVFlows) {
        super(BpmErrorCode.S00095, LocaleUtils.getFoundMoreThenOneEffectiveWfdVFlow());
        this.flowId = flowId;
        this.date = date;
        this.wfdVFlows = wfdVFlows;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flowId);
        args.put("date", date);
        args.put("wfdVFlows", JSONObject.toJSON(wfdVFlows));
    }
}
