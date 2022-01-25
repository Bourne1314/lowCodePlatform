package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/18 14:01
 */
public class NoAccessToCreateFlowException extends BpmSystemException {
    private Flow flow;

    public NoAccessToCreateFlowException(Flow flow) {
        super(BpmErrorCode.S00005, LocaleUtils.getNoAccessToCreateFlow());
        this.flow = flow;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("flowId", flow.getId());
        args.put("flowCode", flow.getCode());
        args.put("flowName", flow.getName());
    }
}
