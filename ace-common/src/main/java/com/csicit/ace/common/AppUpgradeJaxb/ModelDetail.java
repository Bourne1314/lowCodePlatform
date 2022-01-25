package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"modelName", "tableName", "objectName", "serviceId", "createTime", "remark", "pkName", "traceId",
        "fileExist", "flowExist"})
public class ModelDetail {
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 数据库表实体名
     */
    private String objectName;
    /**
     * 服务iD
     */
    private String serviceId;
    /**
     * 模型名称
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar createTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 主键名称
     */
    private String pkName;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 表单是否存在附件
     */
    private Integer fileExist;
    /**
     * 是否是工作流表单
     */
    private Integer flowExist;

    public ModelDetail() {
        super();
    }

    public ModelDetail(String modelName, String tableName, String objectName, String serviceId, XMLGregorianCalendar
            createTime, String remark, String pkName, String traceId, Integer fileExist, Integer flowExist) {
        this.modelName = modelName;
        this.tableName = tableName;
        this.objectName = objectName;
        this.serviceId = serviceId;
        this.createTime = createTime;
        this.remark = remark;
        this.pkName = pkName;
        this.traceId = traceId;
        this.fileExist = fileExist;
        this.flowExist = flowExist;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    @XmlTransient
    public XMLGregorianCalendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(XMLGregorianCalendar createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Integer getFileExist() {
        return fileExist;
    }

    public void setFileExist(Integer fileExist) {
        this.fileExist = fileExist;
    }

    public Integer getFlowExist() {
        return flowExist;
    }

    public void setFlowExist(Integer flowExist) {
        this.flowExist = flowExist;
    }
}
