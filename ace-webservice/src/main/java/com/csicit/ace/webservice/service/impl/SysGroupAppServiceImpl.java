package com.csicit.ace.webservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.webservice.mapper.SysGroupAppMapper;
import com.csicit.ace.webservice.service.SysGroupAppService;
import org.springframework.stereotype.Service;

/**
 * 集团应用库管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysGroupAppService")
public class SysGroupAppServiceImpl extends ServiceImpl<SysGroupAppMapper, SysGroupAppDO> implements
        SysGroupAppService {


}
