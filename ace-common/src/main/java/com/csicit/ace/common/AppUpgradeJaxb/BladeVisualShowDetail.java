package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "visualId", "parentId", "content", "appId", "type", "exhibitionType", "traceId"})
public class BladeVisualShowDetail {

    /**
     * 名称
     */
    private String name;
    /**
     * 大屏ID
     */
    private String visualId;
    /**
     * 父节点
     */
    private String parentId;
    /**
     * json对象
     */
    private String content;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 类型(0为看版,1为项)
     */
    private Integer type;
    /**
     * 显示类型
     */
    private String exhibitionType;
    /**
     * 跟踪ID
     */
    private String traceId;

    public BladeVisualShowDetail() {
        super();
    }

    public BladeVisualShowDetail(String name, String visualId, String parentId, String content, String appId, Integer
            type, String exhibitionType, String traceId) {
        this.name = name;
        this.visualId = visualId;
        this.parentId = parentId;
        this.content = content;
        this.appId = appId;
        this.type = type;
        this.exhibitionType = exhibitionType;
        this.traceId = traceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisualId() {
        return visualId;
    }

    public void setVisualId(String visualId) {
        this.visualId = visualId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getExhibitionType() {
        return exhibitionType;
    }

    public void setExhibitionType(String exhibitionType) {
        this.exhibitionType = exhibitionType;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
