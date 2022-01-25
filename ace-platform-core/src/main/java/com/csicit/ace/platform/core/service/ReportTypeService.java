package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.ReportTypeDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报表类别 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-07 08:52:49
 */
@Transactional
public interface ReportTypeService extends IBaseService<ReportTypeDO> {
    /**
     * 新增
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2020/6/9 17:13
     */
    boolean saveReportType(ReportTypeDO instance);

    /**
     * 修改
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2020/6/9 17:13
     */
    boolean updateReportType(ReportTypeDO instance);

    /**
     * 删除
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2020/6/9 17:13
     */
    boolean removeReportType(List<String> ids);

}
