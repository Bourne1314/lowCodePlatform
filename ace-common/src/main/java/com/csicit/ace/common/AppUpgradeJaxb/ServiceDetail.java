package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "appId", "packageName", "enableAuth", "enableLog", "createServiceFlg", "ipPort",
        "nacosServerAddr", "redisDataBase", "redisHost", "redisPort", "redisPassword", "proInfoId", "isDelete",
        "traceId", "maintainStaffs"})
public class ServiceDetail {

    /**
     * 应用名称
     */
    private String name;
    /**
     * 应用标识
     */
    private String appId;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 是否启用权限 1启用0不启用
     */
    private Integer enableAuth;
    /**
     * 是否启用日志 1启用0不启用
     */
    private Integer enableLog;
    /**
     * 是否生成过服务，0否，1是
     */
    private Integer createServiceFlg;
    /**
     * 应用发布服务端口
     */
    private String ipPort;
    /**
     * Nacos服务地址
     */
    private String nacosServerAddr;
    /**
     * Redis数据库索引
     */
    private Integer redisDataBase;
    /**
     * Redis服务器地址
     */
    private String redisHost;
    /**
     * Redis连接端口
     */
    private String redisPort;
    /**
     * Redis连接密码
     */
    private String redisPassword;
    /**
     * 项目ID
     */
    private String proInfoId;
    /**
     * 是否删除，0否，1是
     */
    private Integer isDelete;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 项目维护人员ID(多人使用;分割)
     */
    private String maintainStaffs;

    public ServiceDetail() {
        super();
    }

    public ServiceDetail(String name, String appId, String packageName, Integer enableAuth, Integer
            enableLog, Integer createServiceFlg, String ipPort, String nacosServerAddr, Integer redisDataBase, String
                                 redisHost, String redisPort, String redisPassword, String proInfoId, Integer
            isDelete, String traceId,
                         String maintainStaffs) {
        this.name = name;
        this.appId = appId;
        this.packageName = packageName;
        this.enableAuth = enableAuth;
        this.enableLog = enableLog;
        this.createServiceFlg = createServiceFlg;
        this.ipPort = ipPort;
        this.nacosServerAddr = nacosServerAddr;
        this.redisDataBase = redisDataBase;
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
        this.proInfoId = proInfoId;
        this.isDelete = isDelete;
        this.traceId = traceId;
        this.maintainStaffs = maintainStaffs;
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getEnableAuth() {
        return enableAuth;
    }

    public void setEnableAuth(Integer enableAuth) {
        this.enableAuth = enableAuth;
    }

    public Integer getEnableLog() {
        return enableLog;
    }

    public void setEnableLog(Integer enableLog) {
        this.enableLog = enableLog;
    }

    public Integer getCreateServiceFlg() {
        return createServiceFlg;
    }

    public void setCreateServiceFlg(Integer createServiceFlg) {
        this.createServiceFlg = createServiceFlg;
    }

    public String getIpPort() {
        return ipPort;
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public String getNacosServerAddr() {
        return nacosServerAddr;
    }

    public void setNacosServerAddr(String nacosServerAddr) {
        this.nacosServerAddr = nacosServerAddr;
    }

    public Integer getRedisDataBase() {
        return redisDataBase;
    }

    public void setRedisDataBase(Integer redisDataBase) {
        this.redisDataBase = redisDataBase;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public String getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(String redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public String getProInfoId() {
        return proInfoId;
    }

    public void setProInfoId(String proInfoId) {
        this.proInfoId = proInfoId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getMaintainStaffs() {
        return maintainStaffs;
    }

    public void setMaintainStaffs(String maintainStaffs) {
        this.maintainStaffs = maintainStaffs;
    }
}
