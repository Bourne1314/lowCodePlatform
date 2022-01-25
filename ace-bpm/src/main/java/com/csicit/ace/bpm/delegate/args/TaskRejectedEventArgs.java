package com.csicit.ace.bpm.delegate.args;

import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;

/**
 * 任务驳回后参数
 *
 * @author JonnyJiang
 * @date 2019/12/27 8:17
 */
public class TaskRejectedEventArgs {
    private Node node;
    private DeliverInfo deliverInfo;
    private WfiFlowDO wfiFlow;

    public TaskRejectedEventArgs(Node node, DeliverInfo deliverInfo, WfiFlowDO wfiFlow) {
        this.node = node;
        this.deliverInfo = deliverInfo;
        this.wfiFlow = wfiFlow;
    }

    public Node getNode() {
        return node;
    }

    public DeliverInfo getDeliverInfo() {
        return deliverInfo;
    }

    public WfiFlowDO getWfiFlow() {
        return wfiFlow;
    }
}
