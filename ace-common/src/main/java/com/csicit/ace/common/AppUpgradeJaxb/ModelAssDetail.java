package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"name", "modelId", "colNames", "refColNames", "refModelId", "refModelName", "delAction", "updAction",
        "createTime", "traceId"})
public class ModelAssDetail {
    /**
     * 关联名
     */
    private String name;
    /**
     * 表Id
     */
    private String modelId;
    /**
     * 数据列名
     */
    private String colNames;
    /**
     * 引用外键列名
     */
    private String refColNames;
    /**
     * 引用表id
     */
    private String refModelId;
    /**
     * 参照表明
     */
    private String refModelName;
    /**
     * 删除动作
     */
    private String delAction;
    /**
     * 更新动作
     */
    private String updAction;
    /**
     * 创建时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar createTime;
    /**
     * 跟踪ID
     */
    private String traceId;

    public ModelAssDetail() {
        super();
    }

    public ModelAssDetail(String name, String modelId, String colNames, String refColNames, String refModelId, String
            refModelName, String delAction, String updAction, XMLGregorianCalendar createTime, String traceId) {
        this.name = name;
        this.modelId = modelId;
        this.colNames = colNames;
        this.refColNames = refColNames;
        this.refModelId = refModelId;
        this.refModelName = refModelName;
        this.delAction = delAction;
        this.updAction = updAction;
        this.createTime = createTime;
        this.traceId = traceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getColNames() {
        return colNames;
    }

    public void setColNames(String colNames) {
        this.colNames = colNames;
    }

    public String getRefColNames() {
        return refColNames;
    }

    public void setRefColNames(String refColNames) {
        this.refColNames = refColNames;
    }

    public String getRefModelId() {
        return refModelId;
    }

    public void setRefModelId(String refModelId) {
        this.refModelId = refModelId;
    }

    public String getRefModelName() {
        return refModelName;
    }

    public void setRefModelName(String refModelName) {
        this.refModelName = refModelName;
    }

    public String getDelAction() {
        return delAction;
    }

    public void setDelAction(String delAction) {
        this.delAction = delAction;
    }

    public String getUpdAction() {
        return updAction;
    }

    public void setUpdAction(String updAction) {
        this.updAction = updAction;
    }
    @XmlTransient
    public XMLGregorianCalendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(XMLGregorianCalendar createTime) {
        this.createTime = createTime;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
