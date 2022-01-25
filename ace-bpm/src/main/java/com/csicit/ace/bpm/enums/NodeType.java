package com.csicit.ace.bpm.enums;

/**
 * 节点类型
 *
 * @author JonnyJiang
 * @date 2019/8/26 14:28
 */
public enum NodeType {
    /**
     * 开始节点
     */
    Start("start"),
    /**
     * 人工节点
     */
    Manual("manual"),
    /**
     * 自由流节点
     */
    Free("free"),
    /**
     * 子流程节点
     */
    Subflow("subflow"),
    /**
     * 结束节点
     */
    End("end");
    /**
     * 节点类型
     */
    private String nodeType;

    NodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Boolean isEquals(String nodeType) {
        return this.nodeType.equals(nodeType);
    }

    public String getValue() {
        return nodeType;
    }
}
