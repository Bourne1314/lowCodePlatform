package com.csicit.ace.report.core.flash.tag;

import com.csicit.ace.report.core.flash.tag.base.AceStiBaseLinkTag;
import com.stimulsoft.web.utils.StiConstants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author shanwj
 * @date 2019/10/15 15:48
 */
public class AceStiViewerLinkTag extends AceStiBaseLinkTag {
    private static final long serialVersionUID = 6012470318366653464L;

    public AceStiViewerLinkTag() {
    }

    public int doStartTag() throws JspException {
        try {
            this.setResourceKey(StiConstants.STIMULSOFT_VIEWER.value);
            this.pageContext.getOut().print(this.generateContent());
            return 0;
        } catch (Exception var2) {
            throw new JspTagException("ViewerLinkTag: " + var2.getMessage(), var2);
        }
    }
}

