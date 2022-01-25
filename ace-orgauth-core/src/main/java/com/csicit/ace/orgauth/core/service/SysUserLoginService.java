package com.csicit.ace.orgauth.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysUserLoginDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yansiyang
 * @date 2019/4/22 14:02
 * @version v1.0
 */
@Transactional
public interface SysUserLoginService extends IService<SysUserLoginDO> {
    /**
     * 获取最新一次登录记录
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/4/15 14:48
     */
    SysUserLoginDO getLatestLogin(String userId);
}
