package com.csicit.ace.report.core.flash.tag.base;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.text.MessageFormat;

/**
 * @author shanwj
 * @date 2019/10/15 15:39
 */
public class AceStiBaseTag extends TagSupport {
    private static final long serialVersionUID = 2998768258150513202L;
    private static final String ACCESSKEY_PATTERN = " {0}=\"{1}\"";
    private String style;
    private String styleClass;
    private String styleId;
    private String accesskey;
    private String dir;
    private String lang;
    private String tabindex;
    private String title;

    public AceStiBaseTag() {
    }

    public String getAttributes(StringBuilder handlers) {
        this.prepareAttribute(handlers, "style", this.style);
        this.prepareAttribute(handlers, "class", this.styleClass);
        this.prepareAttribute(handlers, "id", this.styleId);
        this.prepareAttribute(handlers, "accesskey", this.accesskey);
        this.prepareAttribute(handlers, "dir", this.dir);
        this.prepareAttribute(handlers, "lang", this.lang);
        this.prepareAttribute(handlers, "tabindex", this.tabindex);
        this.prepareAttribute(handlers, "title", this.title);
        return handlers.toString();
    }

    protected void prepareAttribute(StringBuilder handlers, String name, Object value) {
        if (value != null) {
            handlers.append(MessageFormat.format(" {0}=\"{1}\"", name, value));
        }

    }

    public void release() {
        super.release();
        this.style = null;
        this.styleClass = null;
        this.styleId = null;
        this.accesskey = null;
        this.dir = null;
        this.lang = null;
        this.tabindex = null;
        this.title = null;
    }

    public int doEndTag() throws JspException {
        return 6;
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return this.styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyleId() {
        return this.styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getAccesskey() {
        return this.accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getDir() {
        return this.dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTabindex() {
        return this.tabindex;
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
