package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.pojo.domain.dev.ProServiceDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务管理 实例对象访问接口
 *
 * @author shanwj
 * @date 2019/11/25 11:10
 */
@Transactional
public interface ProServiceServiceD extends IBaseService<ProServiceDO> {

    /**
     * 应用升级时，模型更新
     * @param appUpgrade	
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 15:19
     */
    boolean modelUpdate(AppUpgrade appUpgrade);
}
