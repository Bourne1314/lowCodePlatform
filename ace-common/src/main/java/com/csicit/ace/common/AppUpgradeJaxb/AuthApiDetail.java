package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"authId", "apiId", "sign", "traceId", "dataVersion"})
public class AuthApiDetail {
    /**
     * 权限id
     */
    private String authId;
    /**
     * api资源id
     */
    private String apiId;
    /**
     * 签名
     */
    private String sign;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 数据版本
     */
    private Integer dataVersion;

    public AuthApiDetail() {
        super();
    }

    public AuthApiDetail(String authId, String apiId, String sign, String traceId, Integer
            dataVersion) {
        this.authId = authId;
        this.apiId = apiId;
        this.sign = sign;
        this.traceId = traceId;
        this.dataVersion = dataVersion;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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
