package com.csicit.ace.bpm.pojo.vo.wfd;

import com.alibaba.fastjson.annotation.JSONField;
import com.csicit.ace.bpm.pojo.vo.DeliverUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/11/23 15:54
 */
public class NodeFreeStep implements Serializable {
    /**
     * 步骤编号
     */
    private Integer stepNo;
    /**
     * 主键
     */
    private String id;
    /**
     * 主办模式
     */
    private Integer hostMode;
    /**
     * 经办人
     */
    private List<DeliverUser> deliverUsers = new ArrayList<>();
    /**
     * 所属流程节点
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private transient Node node;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getHostMode() {
        return hostMode;
    }

    public void setHostMode(Integer hostMode) {
        this.hostMode = hostMode;
    }

    public List<DeliverUser> getDeliverUsers() {
        if (deliverUsers == null) {
            deliverUsers = new ArrayList<>();
        }
        return deliverUsers;
    }

    public void setDeliverUsers(List<DeliverUser> deliverUsers) {
        this.deliverUsers = deliverUsers;
    }

    public Integer getStepNo() {
        return stepNo;
    }

    public void setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
