package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysAuthScopeOrgDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统管理-用户有效权限-授权组织表 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysAuthScopeOrgService extends IService<SysAuthScopeOrgDO> {

    /**
     * 获取集团或应用管理员受控于的业务单元ID
     * @param userId
     * @return 
     * @author yansiyang
     * @date 2019/7/9 9:58
     */
    List<String> getOrgIdsByUserId(String userId);
}
