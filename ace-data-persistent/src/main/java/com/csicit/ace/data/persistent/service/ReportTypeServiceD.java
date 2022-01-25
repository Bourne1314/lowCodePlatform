package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.ReportTypeDetail;
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
public interface ReportTypeServiceD extends IBaseService<ReportTypeDO> {

    /**
     * 应用升级时，业务类型更新
     *
     * @param reportTypeDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 16:15
     */
    boolean reportTypeUpdate(List<ReportTypeDetail> reportTypeDetails, String appId);
}
