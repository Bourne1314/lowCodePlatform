package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"name", "appId", "sortIndex", "parentId", "sortPath", "orgAdmin", "userGroupAdmin", "code",
        "traceId", "dataVersion", "createUser", "createTime", "updateTime", "remark"})
public class AuthDetail {
    /**
     * 权限名称
     */
    private String name;
    /**
     * 集团应用标识
     */
    private String appId;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 父节点id
     */
    private String parentId;
    /**
     * 排序路径
     */
    private String sortPath;
    /**
     * 是否按组织管控
     */
    private Integer orgAdmin;
    /**
     * 是否按用户组管控
     */
    private Integer userGroupAdmin;
    /**
     * 权限标识
     */
    private String code;
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

    public AuthDetail() {
        super();
    }

    public AuthDetail(String name, String appId, Integer sortIndex, String parentId, String sortPath, Integer
            orgAdmin, Integer userGroupAdmin, String code, String traceId, Integer dataVersion, String createUser,
                      XMLGregorianCalendar createTime
            , XMLGregorianCalendar updateTime, String remark) {
        this.name = name;
        this.appId = appId;
        this.sortIndex = sortIndex;
        this.parentId = parentId;
        this.sortPath = sortPath;
        this.orgAdmin = orgAdmin;
        this.userGroupAdmin = userGroupAdmin;
        this.code = code;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSortPath() {
        return sortPath;
    }

    public void setSortPath(String sortPath) {
        this.sortPath = sortPath;
    }

    public Integer getOrgAdmin() {
        return orgAdmin;
    }

    public void setOrgAdmin(Integer orgAdmin) {
        this.orgAdmin = orgAdmin;
    }

    public Integer getUserGroupAdmin() {
        return userGroupAdmin;
    }

    public void setUserGroupAdmin(Integer userGroupAdmin) {
        this.userGroupAdmin = userGroupAdmin;
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
