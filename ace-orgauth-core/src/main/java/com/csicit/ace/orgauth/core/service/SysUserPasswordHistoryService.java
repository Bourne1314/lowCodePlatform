package com.csicit.ace.orgauth.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysUserPasswordHistoryDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 密码修正历史 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/16 16:04
 */
@Transactional
public interface SysUserPasswordHistoryService extends IService<SysUserPasswordHistoryDO> {
}
