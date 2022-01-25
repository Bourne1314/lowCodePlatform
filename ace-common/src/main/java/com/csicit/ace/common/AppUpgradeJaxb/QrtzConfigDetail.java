package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"config", "traceId"})
public class QrtzConfigDetail {
    /**
     * 任务配置
     */
    private String config;
    /**
     * 跟踪ID
     */
    private String traceId;


    public QrtzConfigDetail() {
        super();
    }

    public QrtzConfigDetail(String config, String traceId) {
        this.config = config;
        this.traceId = traceId;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
