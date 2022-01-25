package com.csicit.ace.report.core.flash.tag;

import com.csicit.ace.report.core.flash.tag.base.AceStiBaseLinkTag;
import com.stimulsoft.web.utils.StiConstants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author shanwj
 * @date 2019/10/15 15:29
 */
public class AceStiDesignerLinkTag extends AceStiBaseLinkTag {
    private static final long serialVersionUID = 8798195794159719443L;

    public AceStiDesignerLinkTag() {
    }

    public int doStartTag() throws JspException {
        try {
            this.setResourceKey(StiConstants.STIMULSOFT_DESIGNER.value);
            this.pageContext.getOut().print(this.generateContent());
            return 0;
        } catch (Exception var2) {
            throw new JspTagException("DesignerLinkTag: " + var2.getMessage(), var2);
        }
    }
}