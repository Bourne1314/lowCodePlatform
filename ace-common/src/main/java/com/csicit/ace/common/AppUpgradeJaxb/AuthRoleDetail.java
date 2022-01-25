package com.csicit.ace.common.AppUpgradeJaxb;


import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"authId", "roleId", "sign", "revoker", "createUser", "createTime", "updateTime", "remark",
        "roleName", "authName", "traceId"})
public class AuthRoleDetail {
    /**
     * 权限主键
     */
    private String authId;
    /**
     * 角色主键
     */
    private String roleId;
    /**
     * 签名
     */
    private String sign;
    /**
     * 是否禁用0不1禁用
     */
    private Integer revoker;
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
     * 角色名
     */
    private String roleName;

    /**
     * 权限名
     */
    private String authName;
    /**
     * 跟踪ID
     */
    private String traceId;

    public AuthRoleDetail() {
        super();
    }

    public AuthRoleDetail(String authId, String roleId, String sign, Integer revoker, String createUser,
                          XMLGregorianCalendar createTime
            , XMLGregorianCalendar updateTime, String remark, String roleName, String authName, String traceId) {
        this.authId = authId;
        this.roleId = roleId;
        this.sign = sign;
        this.revoker = revoker;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.roleName = roleName;
        this.authName = authName;
        this.traceId = traceId;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
