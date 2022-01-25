package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlType(propOrder = {"templateId", "templateTitle", "auth", "templateContent", "url", "createUser", "createTime", "dataVersion",
        "appId", "traceId"})
public class MsgTemplateDetail {
    /**
     * 模板id
     */
    private String templateId;
    /**
     * 模板标题
     */
    private String templateTitle;
    /**
     * 模板标题
     */
    private String auth;
    /**
     * 使用权限
     */
    private String templateContent;
    /**
     * 消息连接
     */
    private String url;
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
     * 数据版本
     */
    private Integer dataVersion;

    /**
     * 应用ID
     */
    private String appId;
    /**
     * 跟踪ID
     */
    private String traceId;

    public MsgTemplateDetail() {
        super();
    }

    public MsgTemplateDetail(String templateId, String templateTitle, String auth, String templateContent, String
            url, String createUser, XMLGregorianCalendar createTime, Integer dataVersion, String appId, String
            traceId) {
        this.templateId = templateId;
        this.templateTitle = templateTitle;
        this.auth = auth;
        this.templateContent = templateContent;
        this.url = url;
        this.createUser = createUser;
        this.createTime = createTime;
        this.dataVersion = dataVersion;
        this.appId = appId;
        this.traceId = traceId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
