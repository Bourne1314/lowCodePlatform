package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"type", "name", "dictExplain", "appId", "groupId", "scope", "traceId", "dataVersion"})
public class DictDetail {
    /**
     * 类型
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 说明
     */
    private String dictExplain;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 集团id
     */
    private String groupId;
    /**
     * 配置范围 1租户 2集团 3应用
     */
    private Integer scope;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 数据版本
     */
    private Integer dataVersion;


    public DictDetail() {
        super();
    }

    public DictDetail(String type, String name, String dictExplain, String appId, String groupId, Integer scope,
                      String traceId, Integer dataVersion) {
        this.type = type;
        this.name = name;
        this.dictExplain = dictExplain;
        this.appId = appId;
        this.groupId = groupId;
        this.scope = scope;
        this.traceId = traceId;
        this.dataVersion = dataVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDictExplain() {
        return dictExplain;
    }

    public void setDictExplain(String dictExplain) {
        this.dictExplain = dictExplain;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
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
