package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "appId", "apiUrl", "apiMethod", "traceId", "dataVersion"})
public class ApiResourceDetail {

    /**
     * api描述
     */
    private String name;
    /**
     * 集团应用标识
     */
    private String appId;
    /**
     * 请求路径
     */
    private String apiUrl;
    /**
     * 请求方式
     */
    private String apiMethod;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 数据版本
     */
    private Integer dataVersion;

    public ApiResourceDetail() {
        super();
    }

    public ApiResourceDetail(String name, String appId, String apiUrl, String apiMethod, String traceId, Integer
            dataVersion) {
        this.name = name;
        this.appId = appId;
        this.apiUrl = apiUrl;
        this.apiMethod = apiMethod;
        this.traceId = traceId;
        this.dataVersion = dataVersion;
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

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }
}
