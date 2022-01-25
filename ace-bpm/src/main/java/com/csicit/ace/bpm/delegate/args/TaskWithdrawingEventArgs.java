package com.csicit.ace.bpm.delegate.args;

import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;

/**
 * 撤回前事件参数
 *
 * @author JonnyJiang
 * @date 2020/3/19 15:06
 */
public class TaskWithdrawingEventArgs {
    private Node node;
    private WfiFlowDO wfiFlow;

    public TaskWithdrawingEventArgs(Node node, WfiFlowDO wfiFlow) {
        this.node = node;
        this.wfiFlow = wfiFlow;
    }

    public Node getNode() {
        return node;
    }

    public WfiFlowDO getWfiFlow() {
        return wfiFlow;
    }
}