package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"name", "appId", "componentUrl", "authId", "columnOne", "columnTwo", "createTime", "colSpan", "traceId"})
public class ComponentRegisterDetail {

    /**
     * 组件名称
     */
    private String name;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 组件地址路径
     */
    private String componentUrl;
    /**
     * 组件对应的权限ID
     */
    private String authId;

    /**
     * 字段1
     */
    private String columnOne;

    /**
     * 字段2
     */
    private String columnTwo;

    /**
     * 创建时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar createTime;

    /**
     * 在首页所占列数
     */
    private Integer colSpan;
    /**
     * 跟踪ID
     */
    private String traceId;

    public ComponentRegisterDetail() {
        super();
    }

    public ComponentRegisterDetail(String name, String appId, String componentUrl, String authId, String columnOne, String
            columnTwo, XMLGregorianCalendar createTime, Integer colSpan, String traceId) {
        this.name = name;
        this.appId = appId;
        this.componentUrl = componentUrl;
        this.authId = authId;
        this.columnOne = columnOne;
        this.columnTwo = columnTwo;
        this.createTime = createTime;
        this.colSpan = colSpan;
        this.traceId = traceId;
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

    public String getComponentUrl() {
        return componentUrl;
    }

    public void setComponentUrl(String componentUrl) {
        this.componentUrl = componentUrl;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getColumnOne() {
        return columnOne;
    }

    public void setColumnOne(String columnOne) {
        this.columnOne = columnOne;
    }

    public String getColumnTwo() {
        return columnTwo;
    }

    public void setColumnTwo(String columnTwo) {
        this.columnTwo = columnTwo;
    }
    @XmlTransient
    public XMLGregorianCalendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(XMLGregorianCalendar createTime) {
        this.createTime = createTime;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
