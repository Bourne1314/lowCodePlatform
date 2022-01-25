package com.csicit.ace.bpm.exception;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class FoundMoreThenOneEffectiveWfdVFlowByCodeException extends BpmSystemException {
    private final String code;
    private final LocalDateTime date;
    private final List<WfdVFlowDO> wfdVFlows;

    public FoundMoreThenOneEffectiveWfdVFlowByCodeException(String code, LocalDateTime date, List<WfdVFlowDO> wfdVFlows) {
        super(BpmErrorCode.S00094, LocaleUtils.getFoundMoreThenOneEffectiveWfdVFlow());
        this.code = code;
        this.date = date;
        this.wfdVFlows = wfdVFlows;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("code", code);
        args.put("date", date);
        args.put("wfdVFlows", JSONObject.toJSON(wfdVFlows));
    }
}
