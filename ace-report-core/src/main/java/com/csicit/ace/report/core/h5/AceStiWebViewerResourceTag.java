package com.csicit.ace.report.core.h5;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.net.URL;

/**
 * @author shanwj
 * @date 2019/9/12 11:25
 */
public class AceStiWebViewerResourceTag extends TagSupport {
    private static final long serialVersionUID = -6036005528990743118L;
    public int doStartTag() throws JspException {
        try {
            HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
            URL contextURL = RequestUtils.getReportURL(request);
            this.pageContext.getOut().print(this.renderViewerScripts(contextURL));
            return 0;
        } catch (Exception var3) {
            throw new JspTagException("AeViewerIFrameTag: " + var3.getMessage(), var3);
        }
    }

    public String renderViewerScripts(URL context) {
        return "";
    }
}