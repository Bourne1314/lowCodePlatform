package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.pojo.domain.SysAuthRoleLvDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统管理-角色授权版本控制 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:24
 */
@Transactional
public interface SysAuthRoleLvServiceD extends IBaseService<SysAuthRoleLvDO> {
    /**
     * 应用升级时，角色权限更新
     *
     * @param appUpgrade
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 17:42
     */
    boolean authRoleUpdate(AppUpgrade appUpgrade);
}
