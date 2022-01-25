package com.csicit.ace.orgauth.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysAuthRoleDO;
import org.springframework.transaction.annotation.Transactional;


/**
 * 权限角色 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysAuthRoleService extends IService<SysAuthRoleDO> {

}
