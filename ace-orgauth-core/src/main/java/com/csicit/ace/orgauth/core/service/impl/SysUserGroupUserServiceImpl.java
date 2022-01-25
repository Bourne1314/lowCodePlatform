package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysUserGroupUserDO;
import com.csicit.ace.data.persistent.mapper.SysUserGroupUserMapper;
import com.csicit.ace.orgauth.core.service.SysUserGroupUserService;
import org.springframework.stereotype.Service;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/10/15 10:39
 */
@Service("sysUserGroupUserServiceO")
public class SysUserGroupUserServiceImpl extends ServiceImpl<SysUserGroupUserMapper, SysUserGroupUserDO> implements SysUserGroupUserService {
}
