package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/21 8:52
 */
@Transactional
public interface SysUserServiceD extends IService<SysUserDO> {
}
