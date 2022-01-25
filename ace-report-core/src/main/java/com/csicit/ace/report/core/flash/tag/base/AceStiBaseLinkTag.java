package com.csicit.ace.report.core.flash.tag.base;


import java.text.MessageFormat;

/**
 * @author shanwj
 * @date 2019/10/15 15:43
 */
public class AceStiBaseLinkTag extends AceStiBaseStiTag {
    private static final long serialVersionUID = -946997149379823567L;
    private static final String CONTENT = "<a href=\"{0}\" {1}>{2}</a>";
    private String coords;
    private String name;
    private String shape;
    private String target;
    private String text;

    public AceStiBaseLinkTag() {
    }

    protected String generateContent() {
        StringBuilder handlers = new StringBuilder();
        this.prepareAttribute(handlers, "coords", this.coords);
        this.prepareAttribute(handlers, "name", this.name);
        this.prepareAttribute(handlers, "shape", this.shape);
        this.prepareAttribute(handlers, "target", this.target);
        this.prepareAttribute(handlers, "text", this.text);
        super.getAttributes(handlers);
        return MessageFormat.format("<a href=\"{0}\" {1}>{2}</a>", this.generateHref(), handlers.toString(), this.text);
    }

    public void release() {
        super.release();
        this.coords = null;
        this.name = null;
        this.shape = null;
        this.target = null;
        this.text = null;
    }

    public String getCoords() {
        return this.coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShape() {
        return this.shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}

