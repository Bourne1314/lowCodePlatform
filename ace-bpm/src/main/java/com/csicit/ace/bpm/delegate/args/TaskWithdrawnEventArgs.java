package com.csicit.ace.bpm.delegate.args;

import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;

/**
 * 撤回后事件参数
 *
 * @author JonnyJiang
 * @date 2020/3/19 15:07
 */
public class TaskWithdrawnEventArgs {
    private Node node;
    private WfiFlowDO wfiFlow;

    public TaskWithdrawnEventArgs(Node node, WfiFlowDO wfiFlow) {
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