package com.csicit.ace.report.core.flash.tag.base;

import java.text.MessageFormat;

/**
 * @author shanwj
 * @date 2019/10/15 15:42
 */
public class AceStiBaseIFrameTag extends AceStiBaseStiTag {
    private static final long serialVersionUID = -176938547966163475L;
    private static final String SCROLLING_DEF = "auto";
    private static final String FRAMEBORDER_DEF = "1";
    private static final String DOES_NOT_SUPPORT_IFRAMES = "Ваш браузер не поддерживает плавающие фреймы!";
    private static final String CONTENT = "<iframe src=\"{0}\" {1} >Ваш браузер не поддерживает плавающие фреймы!</iframe>";
    private String align;
    private String frameborder = "1";
    private String height;
    private String hspace;
    private String marginheight;
    private String marginwidth;
    private String name;
    private String scrolling = "auto";
    private String vspace;
    private String width;

    public AceStiBaseIFrameTag() {
    }

    protected String generateContent() {
        StringBuilder handlers = new StringBuilder();
        this.prepareAttribute(handlers, "align", this.align);
        this.prepareAttribute(handlers, "frameborder", this.frameborder);
        this.prepareAttribute(handlers, "height", this.height);
        this.prepareAttribute(handlers, "hspace", this.hspace);
        this.prepareAttribute(handlers, "marginheight", this.marginheight);
        this.prepareAttribute(handlers, "marginwidth", this.marginwidth);
        this.prepareAttribute(handlers, "name", this.name);
        this.prepareAttribute(handlers, "scrolling", this.scrolling);
        this.prepareAttribute(handlers, "vspace", this.vspace);
        this.prepareAttribute(handlers, "width", this.width);
        super.getAttributes(handlers);
        return MessageFormat.format("<iframe src=\"{0}\" {1} >浏览器不支持IFrame!</iframe>", this.generateHref(), handlers.toString());
    }

    public void release() {
        super.release();
        this.align = null;
        this.frameborder = "1";
        this.height = null;
        this.hspace = null;
        this.marginheight = null;
        this.marginwidth = null;
        this.name = null;
        this.scrolling = "auto";
        this.vspace = null;
        this.width = null;
    }

    public String getAlign() {
        return this.align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getFrameborder() {
        return this.frameborder;
    }

    public void setFrameborder(String frameborder) {
        this.frameborder = frameborder;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHspace() {
        return this.hspace;
    }

    public void setHspace(String hspace) {
        this.hspace = hspace;
    }

    public String getMarginheight() {
        return this.marginheight;
    }

    public void setMarginheight(String marginheight) {
        this.marginheight = marginheight;
    }

    public String getMarginwidth() {
        return this.marginwidth;
    }

    public void setMarginwidth(String marginwidth) {
        this.marginwidth = marginwidth;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScrolling() {
        return this.scrolling;
    }

    public void setScrolling(String scrolling) {
        this.scrolling = scrolling;
    }

    public String getVspace() {
        return this.vspace;
    }

    public void setVspace(String vspace) {
        this.vspace = vspace;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}

