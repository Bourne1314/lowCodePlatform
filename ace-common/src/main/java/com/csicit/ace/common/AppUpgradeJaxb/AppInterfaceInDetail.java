package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"interfaceId", "paramKey", "paramDefaultValue", "paramType", "traceId"})
public class AppInterfaceInDetail {
    /**
     * 接口ID
     */
    private String interfaceId;
    /**
     * 键
     */
    private String paramKey;
    /**
     * 值
     */
    private String paramDefaultValue;
    /**
     * 参数类型
     */
    private String paramType;
    /**
     * 跟踪ID
     */
    private String traceId;

    public AppInterfaceInDetail() {
        super();
    }

    public AppInterfaceInDetail(String interfaceId, String paramKey, String paramDefaultValue, String paramType,
                                String traceId) {
        this.interfaceId = interfaceId;
        this.paramKey = paramKey;
        this.paramDefaultValue = paramDefaultValue;
        this.paramType = paramType;
        this.traceId = traceId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamDefaultValue() {
        return paramDefaultValue;
    }

    public void setParamDefaultValue(String paramDefaultValue) {
        this.paramDefaultValue = paramDefaultValue;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
