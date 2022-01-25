package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/6/9 8:29
 */
public class UnsupportedMultiInstanceModeException extends BpmSystemException {
    private Node node;

    public UnsupportedMultiInstanceModeException(Node node) {
        super(BpmErrorCode.S00026, LocaleUtils.getUnsupportedMultiInstanceMode());
        this.node = node;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeId", node.getId());
        args.put("nodeName", node.getName());
        args.put("nodeCode", node.getCode());
        args.put("multiInstanceMode", node.getMultiInstanceMode());
    }
}
