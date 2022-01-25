package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"templateId", "sortIndex", "codeType", "elExpress", "staticText", "paramKey", "autoCreateSeqDef", "seqStep",
        "seqResetMode", "enableFixNumLen", "seqNumLength","sequenceBizTag", "remark", "dynPart", "traceId"})
public class CodeTemplatePartDetail {

    /**
     * 模板主键
     */
    private String templateId;
    /**
     * 部件排序
     */
    private Integer sortIndex;
    /**
     * 部件编码类型 0静态文本1el表达式2参数值3数字序列
     */
    private Integer codeType;
    /**
     * el表达式
     */
    private String elExpress;
    /**
     * 静态文本
     */
    private String staticText;
    /**
     * 参数键名
     */
    private String paramKey;
    /**
     * 是否自动创建数字序列
     */
    private Integer autoCreateSeqDef;
    /**
     * 序列步长
     */
    private Integer seqStep;
    /**
     * 序列重置模式
     * 0 不重置、 1 按年重置、2 按月重置、3 按天重置
     */
    private Integer seqResetMode;
    /**
     * 是否启用固定数字位数 0否1是
     */
    private Integer enableFixNumLen;
    /**
     * 规定数字位数
     */
    private Integer seqNumLength;
    /**
     * 关联数字学列主标识
     */
    private String sequenceBizTag;
    /**
     * 模板部件描述
     */
    private String remark;
    /**
     * 数字序列关联部件
     */
    private String dynPart;
    /**
     * 跟踪ID
     */
    private String traceId;

    public CodeTemplatePartDetail() {
        super();
    }

    public CodeTemplatePartDetail(String templateId, Integer sortIndex, Integer codeType, String elExpress, String
            staticText, String paramKey, Integer autoCreateSeqDef, Integer seqStep, Integer seqResetMode, Integer
            enableFixNumLen, Integer seqNumLength, String sequenceBizTag, String remark, String dynPart, String
            traceId) {
        this.templateId = templateId;
        this.sortIndex = sortIndex;
        this.codeType = codeType;
        this.elExpress = elExpress;
        this.staticText = staticText;
        this.paramKey = paramKey;
        this.autoCreateSeqDef = autoCreateSeqDef;
        this.seqStep = seqStep;
        this.seqResetMode = seqResetMode;
        this.enableFixNumLen = enableFixNumLen;
        this.seqNumLength = seqNumLength;
        this.sequenceBizTag = sequenceBizTag;
        this.remark = remark;
        this.dynPart = dynPart;
        this.traceId = traceId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    public String getElExpress() {
        return elExpress;
    }

    public void setElExpress(String elExpress) {
        this.elExpress = elExpress;
    }

    public String getStaticText() {
        return staticText;
    }

    public void setStaticText(String staticText) {
        this.staticText = staticText;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public Integer getAutoCreateSeqDef() {
        return autoCreateSeqDef;
    }

    public void setAutoCreateSeqDef(Integer autoCreateSeqDef) {
        this.autoCreateSeqDef = autoCreateSeqDef;
    }

    public Integer getSeqStep() {
        return seqStep;
    }

    public void setSeqStep(Integer seqStep) {
        this.seqStep = seqStep;
    }

    public Integer getSeqResetMode() {
        return seqResetMode;
    }

    public void setSeqResetMode(Integer seqResetMode) {
        this.seqResetMode = seqResetMode;
    }

    public Integer getEnableFixNumLen() {
        return enableFixNumLen;
    }

    public void setEnableFixNumLen(Integer enableFixNumLen) {
        this.enableFixNumLen = enableFixNumLen;
    }

    public Integer getSeqNumLength() {
        return seqNumLength;
    }

    public void setSeqNumLength(Integer seqNumLength) {
        this.seqNumLength = seqNumLength;
    }

    public String getSequenceBizTag() {
        return sequenceBizTag;
    }

    public void setSequenceBizTag(String sequenceBizTag) {
        this.sequenceBizTag = sequenceBizTag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDynPart() {
        return dynPart;
    }

    public void setDynPart(String dynPart) {
        this.dynPart = dynPart;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
