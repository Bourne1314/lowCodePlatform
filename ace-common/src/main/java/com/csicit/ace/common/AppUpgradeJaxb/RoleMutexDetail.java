package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"roleId", "roleMutexId", "dataVersion", "createUser",
        "createTime", "updateTime", "remark","traceId"})
public class RoleMutexDetail {
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 互斥角色id
     */
    private String roleMutexId;
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
    /**
     * 跟踪ID
     */
    private String traceId;

    public RoleMutexDetail() {
        super();
    }

    public RoleMutexDetail(String roleId, String roleMutexId, Integer dataVersion, String createUser,
                           XMLGregorianCalendar createTime, XMLGregorianCalendar updateTime, String remark,String traceId) {
        this.roleId = roleId;
        this.roleMutexId = roleMutexId;
        this.dataVersion = dataVersion;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.traceId = traceId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleMutexId() {
        return roleMutexId;
    }

    public void setRoleMutexId(String roleMutexId) {
        this.roleMutexId = roleMutexId;
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

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
