package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JonnyJiang
 * @date 2020/12/9 19:41
 */
public class DeliverUsersNotContainsWaitForPassUserException extends BpmSystemException {
    private String wfiFlowId;
    private final Node node;
    private final String realNames;

    public DeliverUsersNotContainsWaitForPassUserException(String wfiFlowId, Node node, String realNames) {
        super(BpmErrorCode.S00088, LocaleUtils.getDeliverUsersNotContainsWaitForPassUser(node.getName(),realNames));
        this.wfiFlowId = wfiFlowId;
        this.node = node;
        this.realNames = realNames;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("wfiFlowId", wfiFlowId);
        args.put("nodeId", node.getId());
        args.put("nodeName", node.getName());
        args.put("nodeCode", node.getCode());
        args.put("users", realNames);
    }
}