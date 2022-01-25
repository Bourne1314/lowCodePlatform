package com.csicit.ace.report.core.h5;

import com.stimulsoft.base.mail.StiMailProperties;
import com.stimulsoft.base.system.StiGuid;
import com.stimulsoft.report.StiReport;
import com.stimulsoft.webviewer.StiWebViewerHelper;
import com.stimulsoft.webviewer.StiWebViewerOptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.net.URL;

/**
 * @author shanwj
 * @date 2019/9/12 11:28
 */
public class AceStiWebViewerTag extends TagSupport {
    private static final long serialVersionUID = 6210815439396590673L;
    private StiReport report;
    private StiWebViewerOptions options;
    private StiMailProperties mailProperties;
    private String viewerID;

    public AceStiWebViewerTag() {
    }

    public int doStartTag() throws JspException {
        try {
            StiWebViewerHelper helper = new StiWebViewerHelper();
            HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
            URL contextURL = RequestUtils.getReportURL(request);
            this.pageContext.getOut().print(helper.getWebViewer(this.viewerID, this.options, this.mailProperties, contextURL, request, this.report, this.pageContext.getServletContext()));
            return 0;
        } catch (Exception var4) {
            throw new JspTagException("StiWebViewerTag: " + var4.getMessage(), var4);
        }
    }

    public void release() {
        super.release();
    }

    public StiReport getReport() {
        return this.report;
    }

    public void setReport(StiReport report) {
        this.report = report;
    }

    public StiWebViewerOptions getOptions() {
        return this.options;
    }

    public void setOptions(StiWebViewerOptions options) {
        this.options = options;
    }

    public String getViewerID() {
        return this.viewerID;
    }

    public void setViewerID(String viewerID) {
        this.viewerID = viewerID + StiGuid.newGuidStringPlain();
    }

    public StiMailProperties getMailProperties() {
        return this.mailProperties;
    }

    public void setMailProperties(StiMailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }
}