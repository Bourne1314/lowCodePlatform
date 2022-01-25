package com.csicit.ace.report.core.flash.tag;

import com.csicit.ace.report.core.flash.tag.base.AceStiBaseFrameTag;
import com.stimulsoft.web.utils.StiConstants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author shanwj
 * @date 2019/10/15 15:47
 */
public class AceStiViewerFrameTag extends AceStiBaseFrameTag {
    private static final long serialVersionUID = -176938547966163475L;

    public AceStiViewerFrameTag() {
    }

    public int doStartTag() throws JspException {
        try {
            this.setResourceKey(StiConstants.STIMULSOFT_VIEWER.value);
            this.pageContext.getOut().print(this.generateContent());
            return 0;
        } catch (Exception var2) {
            throw new JspTagException("ViewerFrameTag: " + var2.getMessage(), var2);
        }
    }
}
