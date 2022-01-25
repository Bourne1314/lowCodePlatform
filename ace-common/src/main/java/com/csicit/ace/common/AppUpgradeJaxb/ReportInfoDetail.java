package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"appId", "typeId", "name", "auth", "isPublic", "refreshType", "showItem", "isAutoFlip",
        "mrtStr", "remarks", "datasourceId", "traceId", "dataVersion"})
public class ReportInfoDetail {
    /**
     * 应用id
     */
    private String appId;
    /**
     * 报表类型id
     */
    private String typeId;
    /**
     * 报表名称
     */
    private String name;
    /**
     * 查看权限集合
     */
    private String auth;
    /**
     * 公开报表（0不公开1公开）开放报表，无需登录查看
     */
    private Integer isPublic;
    /**
     * 自动刷新类型（0不自动刷新，1定时刷新，2推送事假定时刷新）
     */
    private Integer refreshType;
    /**
     * 查看选项
     */
    private String showItem;
    /**
     * 自动翻页（0启用 1不启用）
     */
    private Integer isAutoFlip;
    /**
     * 报表字符串
     */
    private String mrtStr;
    /**
     * 描述
     */
    private String remarks;
    /**
     * 数据源id，多数据源用','分开
     */
    private String datasourceId;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 数据版本
     */
    private Integer dataVersion;

    public ReportInfoDetail() {
        super();
    }

    public ReportInfoDetail(String appId, String typeId, String name, String auth, Integer isPublic, Integer
            refreshType, String showItem, Integer isAutoFlip, String mrtStr, String remarks, String datasourceId,
                            String traceId, Integer dataVersion) {
        this.appId = appId;
        this.typeId = typeId;
        this.name = name;
        this.auth = auth;
        this.isPublic = isPublic;
        this.refreshType = refreshType;
        this.showItem = showItem;
        this.isAutoFlip = isAutoFlip;
        this.mrtStr = mrtStr;
        this.remarks = remarks;
        this.datasourceId = datasourceId;
        this.traceId = traceId;
        this.dataVersion = dataVersion;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getRefreshType() {
        return refreshType;
    }

    public void setRefreshType(Integer refreshType) {
        this.refreshType = refreshType;
    }

    public String getShowItem() {
        return showItem;
    }

    public void setShowItem(String showItem) {
        this.showItem = showItem;
    }

    public Integer getIsAutoFlip() {
        return isAutoFlip;
    }

    public void setIsAutoFlip(Integer isAutoFlip) {
        this.isAutoFlip = isAutoFlip;
    }

    public String getMrtStr() {
        return mrtStr;
    }

    public void setMrtStr(String mrtStr) {
        this.mrtStr = mrtStr;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
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
