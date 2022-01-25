package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"name", "value", "sortIndex", "appId", "groupId", "scope", "type", "longValue",
        "updateType", "traceId", "dataVersion", "createUser", "createTime", "updateTime", "remark"})
public class SysConfigDetail {

    /**
     * 属性名
     */
    private String name;
    /**
     * 属性值
     */
    private String value;
    /**
     * 排序值
     */
    private Integer sortIndex;
    /**
     * 集团应用标识
     */
    private String appId;
    /**
     * 集团标识
     */
    private String groupId;
    /**
     * 配置范围 1 租户 2集团 3应用
     */
    private Integer scope;
    /**
     * 值类型
     */
    private String type;
    /**
     * 长型值
     */
    private String longValue;
    /**
     * 配置项更新策略 0立即更新 1重启更新
     */
    private Integer updateType;
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

    public SysConfigDetail() {
        super();
    }

    public SysConfigDetail(String name, String value, Integer sortIndex, String appId, String groupId, Integer scope,
                           String type, String longValue, Integer updateType, String traceId, Integer dataVersion,
                           String createUser, XMLGregorianCalendar createTime, XMLGregorianCalendar updateTime,
                           String remark) {
        this.name = name;
        this.value = value;
        this.sortIndex = sortIndex;
        this.appId = appId;
        this.groupId = groupId;
        this.scope = scope;
        this.type = type;
        this.longValue = longValue;
        this.updateType = updateType;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLongValue() {
        return longValue;
    }

    public void setLongValue(String longValue) {
        this.longValue = longValue;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
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
