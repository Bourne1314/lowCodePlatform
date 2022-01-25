package com.csicit.ace.bpm.pojo.vo;

import com.csicit.ace.bpm.exception.DeliverUserNotFoundByUserIdException;
import com.csicit.ace.common.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 转交分支
 *
 * @author JonnyJiang
 * @date 2019/9/24 16:41
 */
public class DeliverNode implements Serializable {
    /**
     * 分支id
     */
    private String nodeId;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 办理人员
     */
    private List<DeliverUser> deliverUsers = new ArrayList<>();
    /**
     * 节点自由流步骤id，当nodeType为free时有效
     */
    private String nodeFreeStepId;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public List<DeliverUser> getDeliverUsers() {
        if (deliverUsers == null) {
            deliverUsers = new ArrayList<>();
        } else {
            deliverUsers.sort(Comparator.comparing(DeliverUser::getUserId));
        }
        return deliverUsers;
    }

    public void addDeliverUser(DeliverUser deliverUser) {
        this.deliverUsers.add(deliverUser);
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setDeliverUsers(List<DeliverUser> deliverUsers) {
        this.deliverUsers = deliverUsers;
    }

    public DeliverUser getDeliverUserByUserId(String userId) {
        for (DeliverUser deliverUser : getDeliverUsers()) {
            if (StringUtils.equals(userId, deliverUser.getUserId())) {
                return deliverUser;
            }
        }
        throw new DeliverUserNotFoundByUserIdException(nodeId, userId);
    }

    public String getNodeFreeStepId() {
        return nodeFreeStepId;
    }

    public void setNodeFreeStepId(String nodeFreeStepId) {
        this.nodeFreeStepId = nodeFreeStepId;
    }
}
