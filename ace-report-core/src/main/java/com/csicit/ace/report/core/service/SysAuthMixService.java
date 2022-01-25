package com.csicit.ace.report.core.service;

import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 有效权限 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysAuthMixService extends IBaseService<SysAuthMixDO> {

}
