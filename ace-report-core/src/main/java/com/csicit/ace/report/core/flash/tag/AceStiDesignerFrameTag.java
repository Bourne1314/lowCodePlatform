package com.csicit.ace.report.core.flash.tag;

import com.csicit.ace.report.core.flash.tag.base.AceStiBaseFrameTag;
import com.stimulsoft.web.utils.StiConstants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author shanwj
 * @date 2019/10/15 15:31
 */
public class AceStiDesignerFrameTag extends AceStiBaseFrameTag {
    private static final long serialVersionUID = -3953153736606569033L;

    public AceStiDesignerFrameTag() {
    }

    public int doStartTag() throws JspException {
        try {
            this.setResourceKey(StiConstants.STIMULSOFT_DESIGNER.value);
            this.pageContext.getOut().print(this.generateContent());
            return 0;
        } catch (Exception var2) {
            throw new JspTagException("DesignerFrameTag: " + var2.getMessage(), var2);
        }
    }
}
