package com.csicit.ace.report.core.flash.tag.base;


import java.text.MessageFormat;

/**
 * @author shanwj
 * @date 2019/10/15 15:43
 */
public class AceStiBaseFrameTag extends AceStiBaseStiTag {
    private static final long serialVersionUID = 4753944365389571974L;
    private static final String SCROLLING_DEF = "auto";
    private static final String CONTENT = "<frame src=\"{0}\" {1} />";
    private String bordercolor;
    private String frameborder;
    private String marginheight;
    private String marginwidth;
    private String name;
    private String noresize;
    private String scrolling = "auto";

    public AceStiBaseFrameTag() {
    }

    protected String generateContent() {
        StringBuilder handlers = new StringBuilder();
        this.prepareAttribute(handlers, "bordercolor", this.bordercolor);
        this.prepareAttribute(handlers, "frameborder", this.frameborder);
        this.prepareAttribute(handlers, "marginheight", this.marginheight);
        this.prepareAttribute(handlers, "marginwidth", this.marginwidth);
        this.prepareAttribute(handlers, "name", this.name);
        this.prepareAttribute(handlers, "noresize", this.noresize);
        this.prepareAttribute(handlers, "scrolling", this.scrolling);
        super.getAttributes(handlers);
        return MessageFormat.format("<frame src=\"{0}\" {1} />", this.generateHref(), handlers.toString());
    }

    public void release() {
        super.release();
        this.bordercolor = null;
        this.frameborder = null;
        this.marginheight = null;
        this.marginwidth = null;
        this.name = null;
        this.noresize = null;
        this.scrolling = "auto";
    }

    public String getBordercolor() {
        return this.bordercolor;
    }

    public void setBordercolor(String bordercolor) {
        this.bordercolor = bordercolor;
    }

    public String getFrameborder() {
        return this.frameborder;
    }

    public void setFrameborder(String frameborder) {
        this.frameborder = frameborder;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoresize() {
        return this.noresize;
    }

    public void setNoresize(String noresize) {
        this.noresize = noresize;
    }

    public String getScrolling() {
        return this.scrolling;
    }

    public void setScrolling(String scrolling) {
        this.scrolling = scrolling;
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
}

