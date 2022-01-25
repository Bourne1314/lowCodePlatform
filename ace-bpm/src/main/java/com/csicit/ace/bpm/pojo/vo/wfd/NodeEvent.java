package com.csicit.ace.bpm.pojo.vo.wfd;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author JonnyJiang
 * @date 2019/9/3 11:57
 */
public class NodeEvent implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 事件类型
     */
    private Integer eventType;
    /**
     * 事件类
     */
    private String eventClass;
    /**
     * 序号
     */
    private Integer sortIndex;
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

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getEventClass() {
        return eventClass;
    }

    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

}
