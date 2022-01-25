package com.csicit.ace.common.AppUpgradeJaxb;


import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "dataVersion", "type", "username", "password", "url", "driverName", "major", "appId",
        "traceId"})
public class GroupDatasourceDetail {
    /**
     * 数据源名称
     */
    private String name;
    /**
     * 数据版本
     */
    private Integer dataVersion;
    /**
     * 数据库类型
     */
    private String type;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 路径
     */
    private String url;
    /**
     * 驱动名称
     */
    private String driverName;
    /**
     * 是否为主数据源 1是0不是
     */
    private Integer major;

    /**
     * 应用id
     */
    private String appId;
    /**
     * 跟踪ID
     */
    private String traceId;

    public GroupDatasourceDetail() {
        super();
    }

    public GroupDatasourceDetail(String name, Integer dataVersion, String type, String username, String password, String
            url, String driverName, Integer major, String appId, String traceId) {
        this.name = name;
        this.dataVersion = dataVersion;
        this.type = type;
        this.username = username;
        this.password = password;
        this.url = url;
        this.driverName = driverName;
        this.major = major;
        this.appId = appId;
        this.traceId = traceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
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
