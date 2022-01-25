package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/20 14:42
 */
public class WorkResultNotFoundException extends BpmSystemException {
    private final Node node;
    private final String workResult;

    public WorkResultNotFoundException(Node node, String workResult) {
        super(BpmErrorCode.S00056, LocaleUtils.getWorkResultNotFound(node.getCode(), node.getName(), workResult));
        this.node = node;
        this.workResult = workResult;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("nodeId", node.getId());
        args.put("nodeCode", node.getCode());
        args.put("nodeName", node.getName());
        args.put("workResult", workResult);
    }
}
