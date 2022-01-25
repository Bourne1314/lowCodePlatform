package com.csicit.ace.report.core.flash;

import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.report.core.service.ReportService;
import com.csicit.ace.report.core.service.impl.ReportServiceImpl;
import com.stimulsoft.flex.StiSaveAction;
import com.stimulsoft.flex.utils.StiOperationResult;
import com.stimulsoft.flex.utils.StiSaveLoadFileReport;

/**
 * MySaveAction.
 * 
 * Copyright Stimulsoft
 * 
 */
public class MySaveAction extends StiSaveAction {
    ReportService reportService = SpringContextUtils.getBean(ReportServiceImpl.class);
    @Override
    public StiOperationResult save(String report, String reportName, boolean newReportFlag) {
        reportService.updateReportMrt(reportName,report);
        return new StiSaveLoadFileReport().save(report, reportName, newReportFlag);
    }
}
