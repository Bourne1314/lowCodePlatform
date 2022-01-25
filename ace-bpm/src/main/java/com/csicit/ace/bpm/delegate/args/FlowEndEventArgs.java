package com.csicit.ace.bpm.delegate.args;

import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;

/**
 * @author JonnyJiang
 * @date 2019/11/7 20:08
 */
public class FlowEndEventArgs {
    /**
     * 流程定义版本
     */
    private Flow flow;
    /**
     * 结束节点
     */
    private Node node;
    /**
     * 流程实例ID
     */
    private String flowInstanceId;
    /**
     * 业务标识
     */
    private String businessKey;
    /**
     * 结果
     */
    private String result;

    public FlowEndEventArgs(Flow flow, Node node, String flowInstanceId, String businessKey, String result) {
        this.flow = flow;
        this.node = node;
        this.flowInstanceId = flowInstanceId;
        this.businessKey = businessKey;
        this.result = result;
    }

    public Flow getFlow() {
        return flow;
    }

    public Node getNode() {
        return node;
    }

    public String getFlowInstanceId() {
        return flowInstanceId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getResult() {
        return result;
    }
}
