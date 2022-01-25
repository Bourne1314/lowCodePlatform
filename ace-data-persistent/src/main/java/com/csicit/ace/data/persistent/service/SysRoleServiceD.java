package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.AppUpgradeJaxb.RoleDetail;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * 角色管理 实例对象访问接口
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysRoleServiceD extends IService<SysRoleDO> {
    /**
     * 应用升级时，角色更新
     *
     * @param roleDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 15:31
     */
    boolean roleUpdate(List<RoleDetail> roleDetails, String appId);
}
