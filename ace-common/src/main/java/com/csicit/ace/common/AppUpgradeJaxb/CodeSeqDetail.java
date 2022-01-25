package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"appId", "bizTag", "partValueTag", "maxNum", "step", "remark", "resetMode", "lastResetTime",
        "enableFixNumLen", "numLength", "dataVersion", "traceId"})
public class CodeSeqDetail {

    /**
     * 应用ID
     */
    private String appId;
    /**
     * 序列主要标识
     */
    private String  bizTag;
    /**
     * 序列次要标识
     */
    private String  partValueTag;
    /**
     * 已发放的最大编号
     */
    private Long maxNum;
    /**
     * 号段长度
     */
    private Integer step;
    /**
     * 数字序列描述
     */
    private String remark;
    /**
     * 序列重置模式
     * 0 不重置、 1 按年重置、2 按月重置、3 按天重置
     */
    private Integer resetMode;
    /**
     * 上次重置序列时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar lastResetTime;
    /**
     * 是否启用固定数字位数 0否1是
     */
    private Integer enableFixNumLen;
    /**
     * 固定位数
     */
    private Integer numLength;
    /**
     * 数据版本
     */
    private Integer dataVersion;
    /**
     * 跟踪ID
     */
    private String traceId;

    public CodeSeqDetail() {
        super();
    }

    public CodeSeqDetail(String appId, String bizTag, String partValueTag, Long maxNum, Integer step, String remark,
                         Integer resetMode, XMLGregorianCalendar lastResetTime, Integer enableFixNumLen, Integer numLength,
                         Integer dataVersion, String traceId) {
        this.appId = appId;
        this.bizTag = bizTag;
        this.partValueTag = partValueTag;
        this.maxNum = maxNum;
        this.step = step;
        this.remark = remark;
        this.resetMode = resetMode;
        this.lastResetTime = lastResetTime;
        this.enableFixNumLen = enableFixNumLen;
        this.numLength = numLength;
        this.dataVersion = dataVersion;
        this.traceId = traceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getBizTag() {
        return bizTag;
    }

    public void setBizTag(String bizTag) {
        this.bizTag = bizTag;
    }

    public String getPartValueTag() {
        return partValueTag;
    }

    public void setPartValueTag(String partValueTag) {
        this.partValueTag = partValueTag;
    }

    public Long getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Long maxNum) {
        this.maxNum = maxNum;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getResetMode() {
        return resetMode;
    }

    public void setResetMode(Integer resetMode) {
        this.resetMode = resetMode;
    }

    @XmlTransient
    public XMLGregorianCalendar getLastResetTime() {
        return lastResetTime;
    }

    public void setLastResetTime(XMLGregorianCalendar lastResetTime) {
        this.lastResetTime = lastResetTime;
    }

    public Integer getEnableFixNumLen() {
        return enableFixNumLen;
    }

    public void setEnableFixNumLen(Integer enableFixNumLen) {
        this.enableFixNumLen = enableFixNumLen;
    }

    public Integer getNumLength() {
        return numLength;
    }

    public void setNumLength(Integer numLength) {
        this.numLength = numLength;
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
