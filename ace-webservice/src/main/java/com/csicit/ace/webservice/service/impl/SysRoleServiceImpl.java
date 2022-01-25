package com.csicit.ace.webservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.webservice.mapper.SysRoleMapper;
import com.csicit.ace.webservice.service.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * 角色管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleService {


}
