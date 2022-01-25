package com.csicit.ace.orgauth.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysUserRoleDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户角色关系 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysUserRoleService extends IService<SysUserRoleDO> {

}


