package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"modelId", "tabColName", "objColName", "caption", "dataType", "dataPrecision", "dataScale", "nullable",
        "defaultValue", "syscol", "createTime", "pkFlg", "traceId"})
public class ModelColDetail {
    /**
     * 模型名
     */
    private String modelId;
    /**
     * 表列名
     */
    private String tabColName;
    /**
     * 对象列名
     */
    private String objColName;
    /**
     * 列标题
     */
    private String caption;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 列精度
     */
    private Integer dataPrecision;
    /**
     * 列标度
     */
    private Integer dataScale;
    /**
     * 能否为空
     */
    private Integer nullable;
    /**
     * 缺省值
     */
    private String defaultValue;
    /**
     * 是否系统字段
     */
    private Integer syscol;
    /**
     * 创建时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar createTime;
    /**
     * 是否是主键，0否,1是
     */
    private Integer pkFlg;
    /**
     * 跟踪ID
     */
    private String traceId;

    public ModelColDetail() {
        super();
    }

    public ModelColDetail(String modelId, String tabColName, String objColName, String caption, String dataType,
                          Integer dataPrecision, Integer dataScale, Integer nullable, String defaultValue, Integer
                                  syscol, XMLGregorianCalendar createTime, Integer pkFlg, String traceId) {
        this.modelId = modelId;
        this.tabColName = tabColName;
        this.objColName = objColName;
        this.caption = caption;
        this.dataType = dataType;
        this.dataPrecision = dataPrecision;
        this.dataScale = dataScale;
        this.nullable = nullable;
        this.defaultValue = defaultValue;
        this.syscol = syscol;
        this.createTime = createTime;
        this.pkFlg = pkFlg;
        this.traceId = traceId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getTabColName() {
        return tabColName;
    }

    public void setTabColName(String tabColName) {
        this.tabColName = tabColName;
    }

    public String getObjColName() {
        return objColName;
    }

    public void setObjColName(String objColName) {
        this.objColName = objColName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getDataPrecision() {
        return dataPrecision;
    }

    public void setDataPrecision(Integer dataPrecision) {
        this.dataPrecision = dataPrecision;
    }

    public Integer getDataScale() {
        return dataScale;
    }

    public void setDataScale(Integer dataScale) {
        this.dataScale = dataScale;
    }

    public Integer getNullable() {
        return nullable;
    }

    public void setNullable(Integer nullable) {
        this.nullable = nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getSyscol() {
        return syscol;
    }

    public void setSyscol(Integer syscol) {
        this.syscol = syscol;
    }
    @XmlTransient
    public XMLGregorianCalendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(XMLGregorianCalendar createTime) {
        this.createTime = createTime;
    }

    public Integer getPkFlg() {
        return pkFlg;
    }

    public void setPkFlg(Integer pkFlg) {
        this.pkFlg = pkFlg;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
