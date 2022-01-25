package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "appId", "url", "traceId"})
public class MsgTypeExtendDetail {
    /**
     * 类型名称
     */
    private String name;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 消息实现地址
     */
    private String url;
    /**
     * 跟踪ID
     */
    private String traceId;
    public MsgTypeExtendDetail() {
        super();
    }

    public MsgTypeExtendDetail(String name, String appId, String url, String traceId) {
        this.name = name;
        this.appId = appId;
        this.url = url;
        this.traceId = traceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
