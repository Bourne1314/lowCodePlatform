package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"appId", "templateKey", "remark", "dataVersion", "traceId"})
public class CodeTemplateDetail {

    /**
     * 应用ID
     */
    private String appId;
    /**
     * 模板标识
     */
    private String templateKey;
    /**
     * 模板描述
     */
    private String remark;
    /**
     * 数据版本
     */
    private Integer dataVersion;
    /**
     * 跟踪ID
     */
    private String traceId;

    public CodeTemplateDetail() {
        super();
    }

    public CodeTemplateDetail(String appId, String templateKey, String remark, Integer dataVersion, String traceId) {
        this.appId = appId;
        this.templateKey = templateKey;
        this.remark = remark;
        this.dataVersion = dataVersion;
        this.traceId = traceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
