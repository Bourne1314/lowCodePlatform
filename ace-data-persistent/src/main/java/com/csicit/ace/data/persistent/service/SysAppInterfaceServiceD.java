package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.AppInterfaceDetail;
import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 应用接口信息表 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-03 09:03:59
 */
@Transactional
public interface SysAppInterfaceServiceD extends IBaseService<SysAppInterfaceDO> {

    /**
     * 应用升级时，接口更新
     *
     * @param appUpgrade
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 17:05
     */
    boolean interfaceUpdate(AppUpgrade appUpgrade);
}
