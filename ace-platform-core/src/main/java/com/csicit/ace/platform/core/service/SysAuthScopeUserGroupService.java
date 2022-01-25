package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysAuthScopeUserGroupDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统管理-有效权限-授权用户组表 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysAuthScopeUserGroupService extends IService<SysAuthScopeUserGroupDO> {

}
