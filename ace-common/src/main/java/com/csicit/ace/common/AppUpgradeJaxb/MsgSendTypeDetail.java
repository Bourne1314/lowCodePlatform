package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"channelName", "sendMode", "appId", "traceId"})
public class MsgSendTypeDetail {
    /**
     * 频道名称
     */
    private String channelName;

    /**
     * 发送方式
     */
    private String sendMode;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 跟踪ID
     */
    private String traceId;

    public MsgSendTypeDetail() {
        super();
    }

    public MsgSendTypeDetail(String channelName, String sendMode, String appId, String traceId) {
        this.channelName = channelName;
        this.sendMode = sendMode;
        this.appId = appId;
        this.traceId = traceId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSendMode() {
        return sendMode;
    }

    public void setSendMode(String sendMode) {
        this.sendMode = sendMode;
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
