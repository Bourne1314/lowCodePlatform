package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "code", "eventName", "displayMode", "displayContent", "displayTime", "displayDuration",
        "visualIds", "noticeMode", "appId", "traceId"})
public class BladeVisualMsgDetail {
    /**
     * 名称
     */
    private String name;
    /**
     * 标识
     */
    private String code;
    /**
     * 事件名
     */
    private String eventName;
    /**
     * 显示模式（0即时触发，1定时触发）
     */
    private Integer displayMode;
    /**
     * 显示内容
     */
    private String displayContent;
    /**
     * 显示时刻
     */
    private String displayTime;
    /**
     * 显示时长
     */
    private Integer displayDuration;
    /**
     * 大屏Ids(以英文逗号分隔)
     */
    private String visualIds;
    /**
     * 通知模式(0通知，1警告)
     */
    private Integer noticeMode;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 跟踪ID
     */
    private String traceId;

    public BladeVisualMsgDetail() {
        super();
    }

    public BladeVisualMsgDetail(String name, String code, String eventName, Integer displayMode, String
            displayContent, String
                                        displayTime, Integer displayDuration, String visualIds, Integer noticeMode,
                                String appId, String traceId) {
        this.name = name;
        this.code = code;
        this.eventName = eventName;
        this.displayMode = displayMode;
        this.displayContent = displayContent;
        this.displayTime = displayTime;
        this.displayDuration = displayDuration;
        this.visualIds = visualIds;
        this.noticeMode = noticeMode;
        this.appId = appId;
        this.traceId = traceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Integer displayMode) {
        this.displayMode = displayMode;
    }

    public String getDisplayContent() {
        return displayContent;
    }

    public void setDisplayContent(String displayContent) {
        this.displayContent = displayContent;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public Integer getDisplayDuration() {
        return displayDuration;
    }

    public void setDisplayDuration(Integer displayDuration) {
        this.displayDuration = displayDuration;
    }

    public String getVisualIds() {
        return visualIds;
    }

    public void setVisualIds(String visualIds) {
        this.visualIds = visualIds;
    }

    public Integer getNoticeMode() {
        return noticeMode;
    }

    public void setNoticeMode(Integer noticeMode) {
        this.noticeMode = noticeMode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
