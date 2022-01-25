package com.csicit.ace.report.core.flash;

import com.stimulsoft.flex.events.StiWebLoadDesignerFx;
import com.stimulsoft.flex.events.StiWebSwitchAction;
import com.stimulsoft.web.servlet.StiHttpParam;
import com.stimulsoft.web.servlet.StiServlet;
import com.stimulsoft.web.utils.StiConstants;
import com.stimulsoft.web.utils.StiWebExportReportUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shanwj
 * @date 2019/10/15 18:05
 */
public class AceStiDesignerFxServlet extends StiServlet {
    private static final long serialVersionUID = -217930185760915501L;

    public AceStiDesignerFxServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processing(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processing(request, response);
    }

    private void processing(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding(StiConstants.ENCODING.value);
            String paramStr = request.getParameter(StiConstants.STIMULSOFT_CLIENT_KEY.value);
            if (paramStr == null) {
                (new AceStiWebLoadDesignerWrapper()).run(request, response);
            } else {
                StiHttpParam param = StiHttpParam.valueOf(paramStr);
                this.processingParam(request, response, param);
            }
        } catch (Exception var5) {
            this.error(response, var5);
        }

    }

    private void processingParam(HttpServletRequest request, HttpServletResponse response, StiHttpParam param) throws Exception {
        switch(param) {
            case DesignerFx:
                (new StiWebLoadDesignerFx()).run(request, response);
                break;
            case LoadReport:
                this.runAction(request, response, StiWebSwitchAction.performNew(StiHttpParam.LoadDesignerReport, request));
                break;
            case ExportReport:
                StiWebExportReportUtil.export(request, response);
                break;
            default:
                this.runAction(request, response, StiWebSwitchAction.performNew(param, request));
        }

    }
}
