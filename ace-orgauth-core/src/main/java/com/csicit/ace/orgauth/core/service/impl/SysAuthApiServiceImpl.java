package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysAuthApiDO;
import com.csicit.ace.data.persistent.mapper.SysAuthApiMapper;
import com.csicit.ace.orgauth.core.service.SysAuthApiService;
import org.springframework.stereotype.Service;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/9/23 18:14
 */
@Service("sysAuthApiServiceO")
public class SysAuthApiServiceImpl extends ServiceImpl<SysAuthApiMapper, SysAuthApiDO> implements SysAuthApiService {
}
