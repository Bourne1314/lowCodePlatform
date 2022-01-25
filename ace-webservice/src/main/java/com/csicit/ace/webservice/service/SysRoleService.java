package com.csicit.ace.webservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import org.springframework.transaction.annotation.Transactional;


/**
 * 角色管理 实例对象访问接口
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysRoleService extends IService<SysRoleDO> {


}
