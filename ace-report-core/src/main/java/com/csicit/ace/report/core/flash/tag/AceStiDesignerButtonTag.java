package com.csicit.ace.report.core.flash.tag;

import com.csicit.ace.report.core.flash.tag.base.AceStiBaseButtonTag;
import com.stimulsoft.web.utils.StiConstants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author shanwj
 * @date 2019/10/15 15:32
 */
public class AceStiDesignerButtonTag extends AceStiBaseButtonTag {
    private static final long serialVersionUID = -2528481363741835742L;

    public AceStiDesignerButtonTag() {
    }

    public int doStartTag() throws JspException {
        try {
            this.setResourceKey(StiConstants.STIMULSOFT_DESIGNER.value);
            this.pageContext.getOut().print(this.generateContent());
            return 0;
        } catch (Exception var2) {
            throw new JspTagException("DesignerButtonTag: " + var2.getMessage(), var2);
        }
    }
}