package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysUserPasswordHistoryDO;
import com.csicit.ace.data.persistent.mapper.SysUserPasswordHistoryMapper;
import com.csicit.ace.orgauth.core.service.SysUserPasswordHistoryService;
import org.springframework.stereotype.Service;

/**
 * 用户密码修正历史 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/16 16:05
 */
@Service("sysUserPasswordHistoryServiceO")
public class SysUserPasswordHistoryServiceImpl extends ServiceImpl<SysUserPasswordHistoryMapper,
        SysUserPasswordHistoryDO> implements SysUserPasswordHistoryService {
}
