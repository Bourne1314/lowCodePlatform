package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"versionNo", "lastVersion", "versionBeginTime", "versionEndTime", "versionBeginUserId",
        "versionEndUserId", "roleId", "appId", "traceId"})
public class AuthRoleLvDetail {
    /**
     * 版本编号
     */
    private Integer versionNo;
    /**
     * 是否最新版本
     */
    private Integer lastVersion;
    /**
     * 版本启用时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar versionBeginTime;
    /**
     * 版本停用时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar versionEndTime;
    /**
     * 版本启用人主键
     */
    private String versionBeginUserId;
    /**
     * 版本停用人主键
     */
    private String versionEndUserId;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 跟踪ID
     */
    private String traceId;

    public AuthRoleLvDetail() {
        super();
    }

    public AuthRoleLvDetail(Integer versionNo, Integer lastVersion, XMLGregorianCalendar versionBeginTime,
                            XMLGregorianCalendar versionEndTime, String versionBeginUserId, String versionEndUserId,
                            String roleId, String appId, String traceId) {
        this.versionNo = versionNo;
        this.lastVersion = lastVersion;
        this.versionBeginTime = versionBeginTime;
        this.versionEndTime = versionEndTime;
        this.versionBeginUserId = versionBeginUserId;
        this.versionEndUserId = versionEndUserId;
        this.roleId = roleId;
        this.appId = appId;
        this.traceId = traceId;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public Integer getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(Integer lastVersion) {
        this.lastVersion = lastVersion;
    }
    @XmlTransient
    public XMLGregorianCalendar getVersionBeginTime() {
        return versionBeginTime;
    }

    public void setVersionBeginTime(XMLGregorianCalendar versionBeginTime) {
        this.versionBeginTime = versionBeginTime;
    }
    @XmlTransient
    public XMLGregorianCalendar getVersionEndTime() {
        return versionEndTime;
    }

    public void setVersionEndTime(XMLGregorianCalendar versionEndTime) {
        this.versionEndTime = versionEndTime;
    }

    public String getVersionBeginUserId() {
        return versionBeginUserId;
    }

    public void setVersionBeginUserId(String versionBeginUserId) {
        this.versionBeginUserId = versionBeginUserId;
    }

    public String getVersionEndUserId() {
        return versionEndUserId;
    }

    public void setVersionEndUserId(String versionEndUserId) {
        this.versionEndUserId = versionEndUserId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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
