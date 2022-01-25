package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报表信息 实例对象访问接口
 *
 * @author generator
 * @date 2019-08-07 08:54:46
 * @version V1.0
 */
@Transactional
public interface ReportInfoService extends IBaseService<ReportInfoDO> {
    TreeVO getReportTree(String appId, int type);

    boolean saveReport(ReportInfoDO report);

    boolean updateReport(ReportInfoDO report);

    boolean importMrt(String id, String mrtStr);

    boolean deleteReports(List<String> ids);
}
