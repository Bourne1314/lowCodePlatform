package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysUserLoginDO;
import com.csicit.ace.common.utils.server.R;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户登录 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:14:41
 */
@Transactional
public interface SysUserLoginService extends IService<SysUserLoginDO> {
    /**
     * 获取最新一次登录记录
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/4/15 14:48
     */
    SysUserLoginDO getLatestLogin(String userId);

    /**
     * 管理员登录平台，首页显示内容
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/15 14:48
     */
    R getLoginInfo();
}
