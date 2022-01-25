package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"pid", "cid", "createUser", "createTime",
         "updateTime", "remark","dataVersion","traceId"})
public class RoleRelationDetail {

    /**
     * 上级角色
     */
    private String pid;
    /**
     * 下级角色
     */
    private String cid;

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
     * 数据版本
     */
    private Integer dataVersion;
    /**
     * 跟踪ID
     */
    private String traceId;

    public RoleRelationDetail() {
        super();
    }

    public RoleRelationDetail(String pid, String cid, String createUser, XMLGregorianCalendar createTime,
                              XMLGregorianCalendar updateTime, String remark, Integer dataVersion, String traceId) {
        this.pid = pid;
        this.cid = cid;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.dataVersion = dataVersion;
        this.traceId = traceId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
