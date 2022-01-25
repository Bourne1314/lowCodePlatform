package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/10/10 17:15
 */
public class LinkToNodeNotFoundException extends BpmSystemException {
    private String linkId;

    public LinkToNodeNotFoundException(String linkId) {
        super(BpmErrorCode.S00068, LocaleUtils.getLinkToNodeNotFound(linkId));
        this.linkId = linkId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("linkId", linkId);
    }
}
