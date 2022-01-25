package com.csicit.ace.report.core.flash.tag;

import com.csicit.ace.report.core.flash.tag.base.AceStiBaseIFrameTag;
import com.stimulsoft.web.utils.StiConstants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author shanwj
 * @date 2019/10/15 15:47
 */
public class AceStiViewerIFrameTag extends AceStiBaseIFrameTag {
    private static final long serialVersionUID = 6210815439396590673L;

    public AceStiViewerIFrameTag() {
    }

    public int doStartTag() throws JspException {
        try {
            this.setResourceKey(StiConstants.STIMULSOFT_VIEWER.value);
            this.pageContext.getOut().print(this.generateContent());
            return 0;
        } catch (Exception var2) {
            throw new JspTagException("ViewerIFrameTag: " + var2.getMessage(), var2);
        }
    }
}
