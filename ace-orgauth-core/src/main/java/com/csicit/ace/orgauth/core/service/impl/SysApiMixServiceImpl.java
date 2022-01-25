package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysApiMixDO;
import com.csicit.ace.data.persistent.mapper.SysApiMixMapper;
import com.csicit.ace.orgauth.core.service.SysApiMixService;
import org.springframework.stereotype.Service;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/9/23 14:25
 */
@Service("sysApiMixServiceO")
public class SysApiMixServiceImpl extends ServiceImpl<SysApiMixMapper, SysApiMixDO> implements SysApiMixService {
}
