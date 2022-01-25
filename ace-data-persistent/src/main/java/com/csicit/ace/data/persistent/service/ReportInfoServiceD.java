package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.ReportInfoDetail;
import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报表信息 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-07 08:54:46
 */
@Transactional
public interface ReportInfoServiceD extends IBaseService<ReportInfoDO> {

    /**
     * 应用升级时，报表/仪表盘信息更新
     *
     * @param reportInfoDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 16:47
     */
    boolean reportInfoUpdate(List<ReportInfoDetail> reportInfoDetails, String appId);
}
