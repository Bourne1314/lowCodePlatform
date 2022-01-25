package com.csicit.ace.report.core.service.impl;

import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.data.persistent.mapper.SysAuthMixMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.report.core.service.SysAuthMixService;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/11/19 16:51
 */
@Service("sysAuthMixServiceR")
public class SysAuthMixServiceImpl extends BaseServiceImpl<SysAuthMixMapper, SysAuthMixDO> implements SysAuthMixService {
}
