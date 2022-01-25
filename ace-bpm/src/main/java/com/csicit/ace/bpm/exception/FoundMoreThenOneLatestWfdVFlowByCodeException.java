package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.List;
import java.util.Map;

public class FoundMoreThenOneLatestWfdVFlowByCodeException extends BpmSystemException {
    private final String code;
    private final List<WfdVFlowDO> wfdVFlows;

    public FoundMoreThenOneLatestWfdVFlowByCodeException(String code, List<WfdVFlowDO> wfdVFlows) {
        super(BpmErrorCode.S00092, LocaleUtils.getFoundMoreThenOneLatestWfdVFlow());
        this.code = code;
        this.wfdVFlows = wfdVFlows;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("code", code);
        args.put("wfdVFlows", wfdVFlows);
    }
}
