package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.pojo.domain.BladeVisualDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 大屏信息数据表 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 09:59:54
 */
@Transactional
public interface BladeVisualServiceD extends IBaseService<BladeVisualDO> {

    /**
     * 应用升级时，大屏更新
     *
     * @param appUpgrade
     * @return boolean
     * @author zuogang
     * @date 2020/8/12 8:25
     */
    boolean bladeVisualUpdate(AppUpgrade appUpgrade);
}
