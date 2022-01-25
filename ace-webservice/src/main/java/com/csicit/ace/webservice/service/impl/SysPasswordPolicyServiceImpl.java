package com.csicit.ace.webservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysPasswordPolicyDO;
import com.csicit.ace.webservice.mapper.SysPasswordPolicyMapper;
import com.csicit.ace.webservice.service.SysPasswordPolicyService;
import org.springframework.stereotype.Service;

/**
 * 密码策略 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/3 17:45
 */
@Service("systemPasswordPolicyService")
public class SysPasswordPolicyServiceImpl
        extends ServiceImpl<SysPasswordPolicyMapper, SysPasswordPolicyDO>
        implements SysPasswordPolicyService {


}
