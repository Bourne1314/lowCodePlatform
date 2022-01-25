package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "appId", "sort", "type", "parentId", "traceId", "dataVersion"})
public class ReportTypeDetail {
    /**
     * 类别名称
     */
    private String name;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 类别类型 1报表2仪表盘
     */
    private Integer type;
    /**
     * 父节点id
     */
    private String parentId;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 数据版本
     */
    private Integer dataVersion;

    public ReportTypeDetail() {
        super();
    }

    public ReportTypeDetail(String name, String appId, Integer sort, Integer type, String parentId, String traceId,
                            Integer dataVersion) {
        this.name = name;
        this.appId = appId;
        this.sort = sort;
        this.type = type;
        this.parentId = parentId;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
