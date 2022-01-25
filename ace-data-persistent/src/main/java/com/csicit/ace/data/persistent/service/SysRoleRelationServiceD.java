package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.AppUpgradeJaxb.RoleRelationDetail;
import com.csicit.ace.common.pojo.domain.SysRoleRelationDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色关系管理 实例对象访问接口
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysRoleRelationServiceD extends IService<SysRoleRelationDO> {

    /**
     * 应用升级时，角色关系更新
     *
     * @return
     * @author zuogang
     * @date 2020/8/10 15:40
     */
    boolean roleRelationUpdate(List<RoleRelationDetail> roleRelationDetails, String appId);
}
