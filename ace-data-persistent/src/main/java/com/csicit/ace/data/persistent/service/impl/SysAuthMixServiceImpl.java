package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.data.persistent.mapper.SysAuthMixMapper;
import com.csicit.ace.data.persistent.service.SysAuthMixService;
import com.csicit.ace.data.persistent.service.SysAuthServiceD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/6/17 8:00
 */
@Service("sysAuthMixServiceO")
public class SysAuthMixServiceImpl extends ServiceImpl<SysAuthMixMapper, SysAuthMixDO>
        implements SysAuthMixService {

    @Autowired
    SysAuthServiceD sysAuthServiceD;

    @Override
    public boolean hasAuthorityWithUserId(String userId, String authId) {
        return count(new QueryWrapper<SysAuthMixDO>().eq("user_id", userId).eq("auth_id", authId)) > 0;
    }

    @Override
    public boolean hasAuthCodeWithUserId(String userId, String authCode, String appId) {
        SysAuthDO sysAuthDO = sysAuthServiceD.getOne(new QueryWrapper<SysAuthDO>()
        .eq("code", authCode).eq("app_id", appId));
        if (Objects.isNull(sysAuthDO)) {
            return false;
        }
        return count(new QueryWrapper<SysAuthMixDO>().eq("user_id", userId).eq("auth_id", sysAuthDO.getId())) > 0;
    }
}
