package com.csicit.ace.report.core.service.impl;

import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.data.persistent.mapper.ReportInfoMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.report.core.service.ReportService;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/8/7 14:29
 */
@Service("reportServiceR")
public class ReportServiceImpl extends BaseServiceImpl<ReportInfoMapper, ReportInfoDO> implements ReportService {
    @Override
    public void updateReportMrt(String id, String mrtStr) {
        ReportInfoDO reportInfoDO = getById(id);
        reportInfoDO.setMrtStr(mrtStr);
        updateById(reportInfoDO);
    }
}
