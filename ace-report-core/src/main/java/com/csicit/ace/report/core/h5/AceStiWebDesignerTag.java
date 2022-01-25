package com.csicit.ace.report.core.h5;


import com.stimulsoft.webdesigner.StiWebDesignerHelper;
import com.stimulsoft.webdesigner.StiWebDesignerOptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.net.URL;


/**
 * @author shanwj
 * @date 2019/9/12 8:52
 */
public class AceStiWebDesignerTag extends TagSupport {

    private static final long serialVersionUID = 6210815439396590673L;
    private AceStiWebDesigerHandler handler;
    private StiWebDesignerOptions options;
    private String designerID;

    public AceStiWebDesignerTag() {
    }

    public int doStartTag() throws JspException {
        try {
            StiWebDesignerHelper helper = new StiWebDesignerHelper();
            HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
            URL contextURL = RequestUtils.getReportURL(request);
            this.pageContext.getOut().print(helper.getWebDesigner(this.designerID, this.options, contextURL, request, this.handler, this.pageContext.getServletContext()));
            return 0;
        } catch (Exception var4) {
            throw new JspTagException("AceStiWebDesignerTag: " + var4.getMessage(), var4);
        }
    }

    public void release() {
        super.release();
    }

    public AceStiWebDesigerHandler getHandler() {
        return this.handler;
    }

    public void setHandler(AceStiWebDesigerHandler handler) {
        this.handler = handler;
    }

    public StiWebDesignerOptions getOptions() {
        return this.options;
    }

    public void setOptions(StiWebDesignerOptions options) {
        this.options = options;
    }

    public String getDesignerID() {
        return this.designerID;
    }

    public void setDesignerID(String designerID) {
        this.designerID = designerID;
    }
}
