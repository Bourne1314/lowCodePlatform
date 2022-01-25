package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/12/4 2:43
 */
public class SpecifiedHostNotMatchException extends BpmSystemException {
    private final String wfiFlowId;
    private final String taskId;
    private final Node node;
    private final SysUserDO specifiedHost;
    private final SysUserDO host;

    public SpecifiedHostNotMatchException(String wfiFlowId, String taskId, Node node, SysUserDO specifiedHost, SysUserDO host) {
        super(BpmErrorCode.S00087, LocaleUtils.getSpecifiedHostNotMatch(node.getName(), specifiedHost.getRealName(), host.getRealName()));
        this.wfiFlowId = wfiFlowId;
        this.taskId = taskId;
        this.node = node;
        this.specifiedHost = specifiedHost;
        this.host = host;
    }


    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("wfiFlowId", wfiFlowId);
        args.put("taskId", taskId);
        args.put("nodeId", node.getId());
        args.put("nodeName", node.getName());
        args.put("specifiedHostId", specifiedHost.getId());
        args.put("specifiedHost", specifiedHost.getRealName());
        args.put("hostId", host.getId());
        args.put("host", host.getRealName());
    }
}