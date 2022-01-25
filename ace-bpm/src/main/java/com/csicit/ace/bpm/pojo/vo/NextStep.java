package com.csicit.ace.bpm.pojo.vo;

import com.csicit.ace.bpm.exception.*;
import com.csicit.ace.bpm.pojo.vo.wfd.Link;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.pojo.vo.wfd.NodeFreeStep;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.bpm.utils.WfdCollectionUtils;
import com.csicit.ace.common.config.SpringContextUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * 后续办理步骤
 *
 * @author JonnyJiang
 * @date 2019/9/24 18:01
 */
public class NextStep implements Serializable {
    @JsonIgnore
    private transient Node node;
    @JsonIgnore
    private transient Integer secretLevel;

    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * 步骤名称
     */
    private String name;
    /**
     * 允许手动选人
     */
    private Integer enableManualSelParticipant;
    /**
     * 单人办理
     */
    private Integer enableHostOnly;
    /**
     * 主办模式
     */
    private Integer hostMode;
    /**
     * 默认主办人
     */
    private String hostId;
    /**
     * 必选路径
     */
    private Integer mandatoryLink;
    /**
     * 缺省路径
     */
    private Integer defaultLink;
    /**
     * 自由流步骤id，nodeType为free时有值
     */
    private String nodeFreeStepId;
    /**
     * 自动选人结果（需启用自动选人）
     */
    private List<StepUser> nextStepUsers;
    /**
     * 强制办理顺序（主要供前端显示使用）
     */
    private Integer forceSequence;
    public NextStep(Link link, Integer secretLevel) {
        this.mandatoryLink = link.getIsMandatory();
        this.defaultLink = link.getIsDefault();
        this.secretLevel = secretLevel;
        Node toNode = link.getToNode();
        if (toNode == null) {
            throw new LinkToNodeNotFoundException(link.getId());
        }
        init(toNode);
    }

    public NextStep(NodeFreeStep nodeFreeStep, Integer secretLevel) {
        this.nodeFreeStepId = nodeFreeStep.getId();
        this.secretLevel = secretLevel;
        init(nodeFreeStep.getNode());
    }

    /**
     * 初始化
     *
     * @param toNode 目标节点，不能为空
     * @author JonnyJiang
     * @date 2020/11/23 16:43
     */

    private void init(Node toNode) {
        this.nodeId = toNode.getId();
        this.node = toNode;
        this.nodeType = toNode.getNodeType();
        this.name = toNode.getName();
        this.enableManualSelParticipant = toNode.getEnableManualSelParticipant();
        this.enableHostOnly = toNode.getEnableHostOnly();
        this.hostMode = toNode.getHostMode();
        this.hostId = toNode.getHostId();
        this.forceSequence = toNode.getForceSequence();
        WfdCollectionUtils wfdCollectionUtils = SpringContextUtils.getBean(WfdCollectionUtils.class);
        nextStepUsers = FlowUtils.listStepUsersByNode(wfdCollectionUtils, node, true, secretLevel);
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEnableManualSelParticipant() {
        return enableManualSelParticipant;
    }

    public void setEnableManualSelParticipant(Integer enableManualSelParticipant) {
        if (enableManualSelParticipant == null) {
            enableManualSelParticipant = 0;
        }
        this.enableManualSelParticipant = enableManualSelParticipant;
    }

    public Integer getEnableHostOnly() {
        return enableHostOnly;
    }

    public void setEnableHostOnly(Integer enableHostOnly) {
        if (enableHostOnly == null) {
            enableHostOnly = 0;
        }
        this.enableHostOnly = enableHostOnly;
    }

    public Integer getHostMode() {
        return hostMode;
    }

    public void setHostMode(Integer hostMode) {
        this.hostMode = hostMode;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public List<StepUser> getNextStepUsers() {
        return nextStepUsers;
    }

    public void setNextStepUsers(List<StepUser> nextStepUsers) {
        this.nextStepUsers = nextStepUsers;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Integer getMandatoryLink() {
        return mandatoryLink;
    }

    public void setMandatoryLink(Integer mandatoryLink) {
        if (mandatoryLink == null) {
            mandatoryLink = 0;
        }
        this.mandatoryLink = mandatoryLink;
    }

    public Integer getDefaultLink() {
        return defaultLink;
    }

    public void setDefaultLink(Integer defaultLink) {
        if (defaultLink == null) {
            defaultLink = 0;
        }
        this.defaultLink = defaultLink;
    }

    public String getNodeFreeStepId() {
        return nodeFreeStepId;
    }

    public void setNodeFreeStepId(String nodeFreeStepId) {
        this.nodeFreeStepId = nodeFreeStepId;
    }

    public Integer getForceSequence() {
        return forceSequence;
    }

    public void setForceSequence(Integer forceSequence) {
        this.forceSequence = forceSequence;
    }
}
