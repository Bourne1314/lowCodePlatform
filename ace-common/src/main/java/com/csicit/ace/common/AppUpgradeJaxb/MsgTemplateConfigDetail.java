package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"templateId", "open", "url", "tid", "title", "content", "type", "microAppName",
        "microAppId", "traceId"})
public class MsgTemplateConfigDetail {
    /**
     * 小程序消息模板id
     */
    private String templateId;
    /**
     * 是否启用 0不启用1启用
     */
    private Integer open;
    /**
     * 链接路径 如果启用并且有值得情况下使用当前路径否则使用平台默认路径
     */
    private String url;
    /**
     * 平台消息模板id
     */
    private String tid;
    /**
     * 小程序消息模板标题
     */
    private String title;
    /**
     * 小程序消息模板内容
     */
    private String content;
    /**
     * 消息信使类型
     */
    private String type;
    /**
     * 小程序名称
     */
    private String microAppName;
    /**
     * 小程序标识
     */
    private String microAppId;
    /**
     * 跟踪ID
     */
    private String traceId;

    public MsgTemplateConfigDetail() {
        super();
    }

    public MsgTemplateConfigDetail(String templateId, Integer open, String url, String tid, String title, String
            content, String type, String microAppName, String microAppId, String traceId) {
        this.templateId = templateId;
        this.open = open;
        this.url = url;
        this.tid = tid;
        this.title = title;
        this.content = content;
        this.type = type;
        this.microAppName = microAppName;
        this.microAppId = microAppId;
        this.traceId = traceId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMicroAppName() {
        return microAppName;
    }

    public void setMicroAppName(String microAppName) {
        this.microAppName = microAppName;
    }

    public String getMicroAppId() {
        return microAppId;
    }

    public void setMicroAppId(String microAppId) {
        this.microAppId = microAppId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
