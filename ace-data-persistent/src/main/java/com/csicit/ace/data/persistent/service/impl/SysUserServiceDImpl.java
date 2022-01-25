package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.data.persistent.mapper.SysUserMapper;
import com.csicit.ace.data.persistent.service.SysUserServiceD;
import org.springframework.stereotype.Service;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/21 8:53
 */
@Service
public class SysUserServiceDImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements SysUserServiceD {
}
