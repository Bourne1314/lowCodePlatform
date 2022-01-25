package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.data.persistent.mapper.SysGroupAppMapper;
import com.csicit.ace.orgauth.core.service.SysGroupAppService;
import org.springframework.stereotype.Service;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/14 16:34
 */
@Service("sysGroupAppServiceO")
public class SysGroupAppServiceImpl extends ServiceImpl<SysGroupAppMapper, SysGroupAppDO> implements SysGroupAppService {
}
