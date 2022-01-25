package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"interfaceId", "paramKey","paramItem", "paramType", "paramLabel", "traceId"})
public class AppInterfaceOutDetail {

    /**
     * 接口ID
     */
    private String interfaceId;
    /**
     * 键
     */
    private String paramKey;
    /**
     * 实体对象
     */
    private String paramItem;

    /**
     * 参数类型
     */
    private String paramType;
    /**
     * 字段注释
     */
    private String paramLabel;
    /**
     * 跟踪ID
     */
    private String traceId;

    public AppInterfaceOutDetail() {
        super();
    }

    public AppInterfaceOutDetail(String interfaceId, String paramKey, String paramItem,String paramType, String paramLabel, String
            traceId) {
        this.interfaceId = interfaceId;
        this.paramKey = paramKey;
        this.paramItem = paramItem;
        this.paramType = paramType;
        this.paramLabel = paramLabel;
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

    public String getParamItem() {
        return paramItem;
    }

    public void setParamItem(String paramItem) {
        this.paramItem = paramItem;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamLabel() {
        return paramLabel;
    }

    public void setParamLabel(String paramLabel) {
        this.paramLabel = paramLabel;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
