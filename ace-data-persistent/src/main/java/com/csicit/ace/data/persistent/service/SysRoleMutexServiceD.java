package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.AppUpgradeJaxb.RoleMutexDetail;
import com.csicit.ace.common.pojo.domain.SysRoleMutexDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色互斥关系管理 实例对象访问接口
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysRoleMutexServiceD extends IService<SysRoleMutexDO> {
    /**
     * 应用升级时，互斥角色更新
     *
     * @param roleMutexDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 16:03
     */
    boolean roleMutexUpdate(List<RoleMutexDetail> roleMutexDetails, String appId);
}
