package com.csicit.ace.report.core.flash;

import com.stimulsoft.base.exception.StiException;
import com.stimulsoft.base.system.MIMEType;
import com.stimulsoft.flex.events.StiLoadWrapper;
import com.stimulsoft.flex.utils.StiHtmlWrapBuilder;
import com.stimulsoft.web.events.StiAbstractWebAction;
import com.stimulsoft.web.servlet.StiHttpParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shanwj
 * @date 2019/10/15 18:06
 */
public class AceStiWebLoadDesignerWrapper extends StiAbstractWebAction {
    public AceStiWebLoadDesignerWrapper() {
    }

    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, StiException {
        response.setContentType(MIMEType.html.type);
        StiHtmlWrapBuilder builder = new StiHtmlWrapBuilder("/report"+request.getRequestURI(), request.getParameterMap(), StiHttpParam.DesignerFx.name());
        (new StiLoadWrapper()).run(builder, request.getInputStream(), response.getOutputStream());
    }
}