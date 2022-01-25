package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"appId", "name", "pageGet", "sqlContent", "createTime", "description", "authId", "dsId",
        "code", "traceId"})
public class AppInterfaceDetail {
    /**
     * 服务iD
     */
    private String appId;
    /**
     * 接口名
     */
    private String name;
    /**
     * 是否分页方法,0否1是
     */
    private Integer pageGet;
    /**
     * sql语句
     */
    private String sqlContent;
    /**
     * 创建时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar createTime;
    /**
     * 描述
     */
    private String description;
    /**
     * 关联权限
     */
    private String authId;
    /**
     * 数据源ID
     */
    private String dsId;
    /**
     * 关联权限
     */
    private String code;
    /**
     * 跟踪ID
     */
    private String traceId;

    public AppInterfaceDetail() {
        super();
    }

    public AppInterfaceDetail(String appId, String name, Integer pageGet, String sqlContent, XMLGregorianCalendar
            createTime, String description, String authId, String dsId, String code, String traceId) {
        this.appId = appId;
        this.name = name;
        this.pageGet = pageGet;
        this.sqlContent = sqlContent;
        this.createTime = createTime;
        this.description = description;
        this.authId = authId;
        this.dsId = dsId;
        this.code = code;
        this.traceId = traceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPageGet() {
        return pageGet;
    }

    public void setPageGet(Integer pageGet) {
        this.pageGet = pageGet;
    }

    public String getSqlContent() {
        return sqlContent;
    }

    public void setSqlContent(String sqlContent) {
        this.sqlContent = sqlContent;
    }
    @XmlTransient
    public XMLGregorianCalendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(XMLGregorianCalendar createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getDsId() {
        return dsId;
    }

    public void setDsId(String dsId) {
        this.dsId = dsId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
