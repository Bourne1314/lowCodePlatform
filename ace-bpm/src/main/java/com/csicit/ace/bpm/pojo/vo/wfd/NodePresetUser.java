package com.csicit.ace.bpm.pojo.vo.wfd;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author JonnyJiang
 * @date 2020/8/25 11:54
 */
public class NodePresetUser {
    /**
     * 排序号
     */
    private Integer sortIndex;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户身份
     */
    private Integer userType;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 所属流程节点
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private transient Node node;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
