package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.ComponentRegisterDetail;
import com.csicit.ace.common.pojo.domain.SysComponentRegisterDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组件注册 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:24:50
 */
@Transactional
public interface SysComponentRegisterServiceD extends IBaseService<SysComponentRegisterDO> {
    /**
     * 应用升级， 更新组件注册
     *
     * @param componentRegisters
     * @param appId
     * @return
     * @author zuogang
     * @date 2020/8/10 14:08
     */
    boolean componentRegisterUpdate(List<ComponentRegisterDetail> componentRegisters, String appId);
}
