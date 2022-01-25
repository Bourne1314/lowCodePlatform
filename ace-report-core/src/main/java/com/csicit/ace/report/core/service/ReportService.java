package com.csicit.ace.report.core.service;

import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanwj
 * @date 2019/8/7 14:28
 */
@Transactional
public interface ReportService extends IBaseService<ReportInfoDO> {
    void updateReportMrt(String id, String mrtStr);
}
