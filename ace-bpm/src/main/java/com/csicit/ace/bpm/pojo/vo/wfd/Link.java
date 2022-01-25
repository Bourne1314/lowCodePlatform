package com.csicit.ace.bpm.pojo.vo.wfd;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author JonnyJiang
 * @date 2019/9/3 11:57
 */
public class Link implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 序号
     */
    private Integer sortIndex;
    /**
     * 流出节点id
     */
    private String fromNodeId;
    /**
     * 流入节点id
     */
    private String toNodeId;
    /**
     * 必选路径
     */
    private Integer isMandatory;
    /**
     * 缺省路径
     */
    private Integer isDefault;
    /**
     * 流转条件定义方式
     */
    private Integer conditionMode;
    /**
     * 自定义路由规则
     */
    private String ruleExpression;
    /**
     * 指定结果
     */
    private String result;
    /**
     * 计算方式，按比例或按数量
     */
    private Integer countMode;
    /**
     * 比较运算符
     */
    private Integer operator;
    /**
     * 比例或数量
     */
    private Integer count;
    /**
     * 所属流程
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private transient Flow flow;
    /**
     * 流出节点
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private transient Node fromNode;
    /**
     * 流入节点
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private transient Node toNode;
    /**
     * 是否预设的路径
     */
    private Integer presetedLink;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(String fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public String getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(String toNodeId) {
        this.toNodeId = toNodeId;
    }

    public Integer getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Integer isMandatory) {
        this.isMandatory = isMandatory;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getConditionMode() {
        return conditionMode;
    }

    public void setConditionMode(Integer conditionMode) {
        this.conditionMode = conditionMode;
    }

    public String getRuleExpression() {
        return ruleExpression;
    }

    public void setRuleExpression(String ruleExpression) {
        this.ruleExpression = ruleExpression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getCountMode() {
        return countMode;
    }

    public void setCountMode(Integer countMode) {
        this.countMode = countMode;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public void setFromNode(Node node) {
        this.fromNodeId = node == null ? null : node.getId();
        this.fromNode = node;
    }

    public void setToNode(Node node) {
        this.toNodeId = node == null ? null : node.getId();
        this.toNode = node;
    }

    public void setPresetedLink(Integer presetedLink) {
        this.presetedLink = presetedLink;
    }

    public Integer getPresetedLink() {
        return presetedLink;
    }
}
