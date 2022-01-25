package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.QrtzConfigDetail;
import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 批处理任务配置 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-16 09:59:40
 */
@Transactional
public interface QrtzConfigServiceD extends IBaseService<QrtzConfigDO> {

    /**
     * 应用升级时，定时任务更新
     *
     * @param qrtzConfigDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 14:10
     */
    boolean qrtzCongfigUpdate(List<QrtzConfigDetail> qrtzConfigDetails, String appId);
}
