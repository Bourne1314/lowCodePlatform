package com.csicit.ace.report.core.flash.tag;

import com.csicit.ace.report.core.flash.tag.base.AceStiBaseIFrameTag;
import com.stimulsoft.web.utils.StiConstants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author shanwj
 * @date 2019/10/15 15:30
 */
public class AceStiDesignerIFrameTag extends AceStiBaseIFrameTag {
    private static final long serialVersionUID = -2626964545285959013L;

    public AceStiDesignerIFrameTag() {
    }

    public int doStartTag() throws JspException {
        try {
            this.setResourceKey(StiConstants.STIMULSOFT_DESIGNER.value);
            this.pageContext.getOut().print(this.generateContent());
            return 0;
        } catch (Exception var2) {
            throw new JspTagException("DesignerIFrameTag: " + var2.getMessage(), var2);
        }
    }
}

