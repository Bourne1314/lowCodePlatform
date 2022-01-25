package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysUserLoginDO;
import com.csicit.ace.data.persistent.mapper.SysUserLoginMapper;
import com.csicit.ace.orgauth.core.service.SysUserLoginService;
import org.springframework.stereotype.Service;

/**
 * 登录日志管理
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Service("sysUserLoginServiceO")
public class SysUserLoginServiceImpl extends ServiceImpl<SysUserLoginMapper, SysUserLoginDO> implements SysUserLoginService {

    /**
     * 获取最新一次登录记录
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/4/15 14:48
     */
    @Override
    public SysUserLoginDO getLatestLogin(String userId) {
        return getOne(new QueryWrapper<SysUserLoginDO>().eq("user_id", userId).orderByDesc("login_time"));
    }
}
