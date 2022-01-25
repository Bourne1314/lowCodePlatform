package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"typeId", "dictValue", "dictName", "sortIndex","appId", "groupId", "scope", "type", "parentId",
        "sortPath", "remark", "traceId", "dataVersion"})
public class DictValueDetail {
    /**
     * 类型编号
     */
    private String typeId;
    /**
     * 字典数据值
     */
    private String dictValue;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 显示顺序
     */
    private Integer sortIndex;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 集团id
     */
    private String groupId;
    /**
     * 配置范围 1租户 2集团 3应用
     */
    private Integer scope;
    /**
     * 类型
     */
    private String type;
    /**
     * 父节点id
     */
    private String parentId;
    /**
     * 排序路径
     */
    private String sortPath;
    /**
     * 备注
     */
    private String remark;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 数据版本
     */
    private Integer dataVersion;

    public DictValueDetail() {
        super();
    }

    public DictValueDetail(String typeId, String dictValue, String dictName, Integer sortIndex, String appId, String
            groupId, Integer scope, String type, String parentId, String sortPath, String remark, String traceId,
                           Integer dataVersion) {
        this.typeId = typeId;
        this.dictValue = dictValue;
        this.dictName = dictName;
        this.sortIndex = sortIndex;
        this.appId = appId;
        this.groupId = groupId;
        this.scope = scope;
        this.type = type;
        this.parentId = parentId;
        this.sortPath = sortPath;
        this.remark = remark;
        this.traceId = traceId;
        this.dataVersion = dataVersion;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
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

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }
}

