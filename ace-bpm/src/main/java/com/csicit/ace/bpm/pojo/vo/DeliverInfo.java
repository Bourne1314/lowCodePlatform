package com.csicit.ace.bpm.pojo.vo;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.exception.DeliverNodeNotFoundByNodeIdException;
import com.csicit.ace.bpm.utils.LocaleUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 转交信息
 *
 * @author JonnyJiang
 * @date 2019/9/24 16:39
 */
@Data
public class DeliverInfo implements Serializable {
    /**
     * 任务id
     * 从开始节点流转时为空
     */
    private String taskId;

    /**
     * 办理意见
     */
    private String opinion;
    /**
     * 办理结果
     */
    private String[] workResultOptions;
    /**
     * 操作类型
     */
    private Integer operationType;
    /**
     * 拟转交到的节点
     */
    private List<DeliverNode> deliverNodes = new ArrayList<>();
    /**
     * 驳回的任务id（当操作类型为驳回后被终止时生效）
     */
    private String rejectTaskId;
    /**
     * 自由流节点步骤id（自由流节点提交时，由程序写入）
     */
    private String nodeFreeStepId;

    public List<DeliverNode> getDeliverNodes() {
        if (deliverNodes == null) {
            deliverNodes = new ArrayList<>();
        }
        return deliverNodes;
    }

    public void addDeliverNode(DeliverNode deliverNode) {
        this.deliverNodes.add(deliverNode);
    }

    public DeliverNode getDeliverNodeByNodeId(String nodeId) {
        for (DeliverNode deliverNode : getDeliverNodes()) {
            if (deliverNode.getNodeId().equals(nodeId)) {
                return deliverNode;
            }
        }
        throw new DeliverNodeNotFoundByNodeIdException(nodeId);
    }

    public void setDeliverNodeByNodeId(DeliverNode deliverNode, String nodeId) {
        List<DeliverNode> newDeliverNodes = new ArrayList<>();
        for (DeliverNode deliverNodeT : getDeliverNodes()) {
            if (deliverNode.getNodeId().equals(nodeId)) {
                newDeliverNodes.add(deliverNode);
            } else {
                newDeliverNodes.add(deliverNodeT);
            }
        }
    }
}