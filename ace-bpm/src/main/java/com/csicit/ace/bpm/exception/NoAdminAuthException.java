package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/2 10:04
 */
public class NoAdminAuthException extends BpmSystemException {
    private final Flow flow;

    public NoAdminAuthException(Flow flow) {
        super(BpmErrorCode.S00038, LocaleUtils.getNoAdminAuth());
        this.flow = flow;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", flow.getId());
    }
}
