package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"authId", "authName", "roleId", "sign", "revoker", "lvId", "createUser", "createTime",
        "updateTime", "remark", "traceId"})
public class AuthRoleVDetail {
    /**
     * 权限主键
     */
    private String authId;
    /**
     * 权限名称
     */
    private String authName;
    /**
     * 角色主键
     */
    private String roleId;
    /**
     * 签名
     */
    private String sign;
    /**
     * 是否禁用
     */
    private Integer revoker;
    /**
     * 角色授权版本控制主键
     */
    private String lvId;
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

    public AuthRoleVDetail() {
        super();
    }

    public AuthRoleVDetail(String authId, String authName, String roleId, String sign, Integer revoker, String lvId,
                           String createUser, XMLGregorianCalendar createTime, XMLGregorianCalendar updateTime,
                           String remark, String traceId) {
        this.authId = authId;
        this.authName = authName;
        this.roleId = roleId;
        this.sign = sign;
        this.revoker = revoker;
        this.lvId = lvId;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.traceId = traceId;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getRevoker() {
        return revoker;
    }

    public void setRevoker(Integer revoker) {
        this.revoker = revoker;
    }

    public String getLvId() {
        return lvId;
    }

    public void setLvId(String lvId) {
        this.lvId = lvId;
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
