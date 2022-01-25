package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"modelId", "name", "cols", "onlyOne", "createTime", "traceId"})
public class ModelIndexDetail {

    /**
     * 数据表
     */
    private String modelId;
    /**
     * 名称
     */
    private String name;
    /**
     * 包含列
     */
    private String cols;
    /**
     * 是否唯一
     */
    private Integer onlyOne;
    /**
     * 模型名称
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar createTime;
    /**
     * 跟踪ID
     */
    private String traceId;

    public ModelIndexDetail() {
        super();
    }

    public ModelIndexDetail(String modelId, String name, String cols, Integer onlyOne, XMLGregorianCalendar
            createTime, String traceId) {
        this.modelId = modelId;
        this.name = name;
        this.cols = cols;
        this.onlyOne = onlyOne;
        this.createTime = createTime;
        this.traceId = traceId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public Integer getOnlyOne() {
        return onlyOne;
    }

    public void setOnlyOne(Integer onlyOne) {
        this.onlyOne = onlyOne;
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
