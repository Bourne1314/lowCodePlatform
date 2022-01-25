package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"name", "roleExplain", "roleType", "appId",
        "traceId", "dataVersion", "createUser", "createTime", "updateTime", "remark"})
public class RoleDetail {
    /**
     * 角色名
     */
    private String name;
    /**
     * 角色说明
     */
    private String roleExplain;
    /**
     * 角色类型(1租户系统管理员,2租户安全保密员,3租户安全审计员)
     * (11集团系统管理员,22集团安全保密员,33集团安全审计员)
     * (111应用系统管理员,222应用安全保密员,333应用安全审计员)
     * (1111开发平台超级管理员,2222项目管理人员,3333项目开发人员)
     */
    private Integer roleType;
    /**
     * 集团应用标识
     */
    private String appId;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 数据版本
     */
    private Integer dataVersion;

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
     * 备注
     */
    private String remark;

    public RoleDetail() {
        super();
    }

    public RoleDetail(String name, String roleExplain, Integer roleType, String appId, String traceId, Integer
            dataVersion, String createUser, XMLGregorianCalendar createTime, XMLGregorianCalendar updateTime, String
            remark) {
        this.name = name;
        this.roleExplain = roleExplain;
        this.roleType = roleType;
        this.appId = appId;
        this.traceId = traceId;
        this.dataVersion = dataVersion;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleExplain() {
        return roleExplain;
    }

    public void setRoleExplain(String roleExplain) {
        this.roleExplain = roleExplain;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
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

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
