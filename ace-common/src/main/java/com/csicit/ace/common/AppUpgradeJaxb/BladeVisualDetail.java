package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"title", "backgroundInfo", "category", "password", "createUser", "createTime", "updateTime", "updateUser",
        "status", "beDeleted", "detail", "component", "appId", "authId", "traceId"})
public class BladeVisualDetail {
    /**
     * 大屏标题
     */
    private String title;
    /**
     * 大屏背景
     */
    private String backgroundInfo;
    /**
     * 业务类型ID
     */
    private String category;
    /**
     * 发布密码
     */
    private String password;
    /**
     * 创建人id
     */
    private String createUser;
    /**
     * 创建时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar createTime;
    /**
     * 最后一次修改时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar updateTime;
    /**
     * 修改人
     */
    private String updateUser;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 逻辑删除 0 否 1是
     */
    private Integer beDeleted;
    /**
     * 配置json
     */
    private String detail;
    /**
     * 组件json
     */
    private String component;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 权限标识
     */
    private String authId;
    /**
     * 跟踪ID
     */
    private String traceId;

    public BladeVisualDetail() {
        super();
    }

    public BladeVisualDetail(String title, String backgroundInfo, String category, String password, String
            createUser, XMLGregorianCalendar createTime, XMLGregorianCalendar updateTime, String updateUser, Integer
            status, Integer beDeleted, String detail, String component, String appId, String authId, String traceId) {
        this.title = title;
        this.backgroundInfo = backgroundInfo;
        this.category = category;
        this.password = password;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.status = status;
        this.beDeleted = beDeleted;
        this.detail = detail;
        this.component = component;
        this.appId = appId;
        this.authId = authId;
        this.traceId = traceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackgroundInfo() {
        return backgroundInfo;
    }

    public void setBackgroundInfo(String backgroundInfo) {
        this.backgroundInfo = backgroundInfo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    @XmlTransient
    public XMLGregorianCalendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(XMLGregorianCalendar createTime) {
        this.createTime = createTime;
    }
    @XmlTransient
    public XMLGregorianCalendar getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(XMLGregorianCalendar updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBeDeleted() {
        return beDeleted;
    }

    public void setBeDeleted(Integer beDeleted) {
        this.beDeleted = beDeleted;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
